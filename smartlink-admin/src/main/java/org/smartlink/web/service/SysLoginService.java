package org.smartlink.web.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.smartlink.common.core.constant.*;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.dto.RoleDTO;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.domain.model.SmsLoginBody;
import org.smartlink.common.core.enums.LoginType;
import org.smartlink.common.core.enums.TenantStatus;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.exception.user.UserException;
import org.smartlink.common.core.utils.*;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.log.event.LogininforEvent;
import org.smartlink.common.mybatis.helper.DataPermissionHelper;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.common.sse.dto.SseMessageDto;
import org.smartlink.common.sse.utils.SseMessageUtils;
import org.smartlink.common.tenant.exception.TenantException;
import org.smartlink.common.tenant.helper.TenantHelper;
import org.smartlink.system.domain.SysUser;
import org.smartlink.system.domain.bo.SysSocialBo;
import org.smartlink.system.domain.vo.*;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.system.service.*;
import org.smartlink.web.domain.bo.ForgetPasswordBo;
import org.smartlink.web.domain.vo.LoginVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class SysLoginService {

    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${user.password.lockTime}")
    private Integer lockTime;

    private final ISysTenantService tenantService;
    private final ISysPermissionService permissionService;
    private final ISysSocialService sysSocialService;
    private final ISysRoleService roleService;
    private final ISysDeptService deptService;
    private final SysUserMapper userMapper;
    private final ISysClientService clientService;
    private final ScheduledExecutorService scheduledExecutorService;


    /**
     * 绑定第三方用户
     *
     * @param authUserData 授权响应实体
     */
    @Lock4j
    public void socialRegister(AuthUser authUserData) {
        String authId = authUserData.getSource() + authUserData.getUuid();
        // 第三方用户信息
        SysSocialBo bo = BeanUtil.toBean(authUserData, SysSocialBo.class);
        BeanUtil.copyProperties(authUserData.getToken(), bo);
        Long userId = LoginHelper.getUserId();
        bo.setUserId(userId);
        bo.setAuthId(authId);
        bo.setOpenId(authUserData.getUuid());
        bo.setUserName(authUserData.getUsername());
        bo.setNickName(authUserData.getNickname());
        List<SysSocialVo> checkList = sysSocialService.selectByAuthId(authId);
        if (CollUtil.isNotEmpty(checkList)) {
            throw new ServiceException("此三方账号已经被绑定!");
        }
        // 查询是否已经绑定用户
        SysSocialBo params = new SysSocialBo();
        params.setUserId(userId);
        params.setSource(bo.getSource());
        List<SysSocialVo> list = sysSocialService.queryList(params);
        if (CollUtil.isEmpty(list)) {
            // 没有绑定用户, 新增用户信息
            sysSocialService.insertByBo(bo);
        } else {
            // 更新用户信息
            bo.setId(list.get(0).getId());
            sysSocialService.updateByBo(bo);
            // 如果要绑定的平台账号已经被绑定过了 是否抛异常自行决断
            // throw new ServiceException("此平台账号已经被绑定!");
        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            LoginUser loginUser = LoginHelper.getLoginUser();
            if (ObjectUtil.isNull(loginUser)) {
                return;
            }
            if (TenantHelper.isEnable() && LoginHelper.isSuperAdmin()) {
                // 超级管理员 登出清除动态租户
                TenantHelper.clearDynamic();
            }
            recordLogininfor(loginUser.getTenantId(), loginUser.getUsername(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
        } catch (NotLoginException ignored) {
        } finally {
            try {
                StpUtil.logout();
            } catch (NotLoginException ignored) {
            }
        }
    }
    /**
     * 记录登录信息
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLogininfor(String tenantId, String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setTenantId(tenantId);
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }
    /**
     * 构建登录用户
     */
    public LoginUser buildLoginUser(SysUserVo user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setTenantId(user.getTenantId());
        loginUser.setUserId(user.getUserId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUsername(user.getUserName());
        loginUser.setNickname(user.getNickName());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(permissionService.getMenuPermission(user.getUserId()));
        loginUser.setRolePermission(permissionService.getRolePermission(user.getUserId()));
        if (ObjectUtil.isNotNull(user.getDeptId())) {
            Opt<SysDeptVo> deptOpt = Opt.of(user.getDeptId()).map(deptService::selectDeptById);
            loginUser.setDeptName(deptOpt.map(SysDeptVo::getDeptName).orElse(StringUtils.EMPTY));
            loginUser.setDeptCategory(deptOpt.map(SysDeptVo::getDeptCategory).orElse(StringUtils.EMPTY));
        }
        List<SysRoleVo> roles = roleService.selectRolesByUserId(user.getUserId());
        loginUser.setRoles(BeanUtil.copyToList(roles, RoleDTO.class));
        return loginUser;
    }
    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId, String ip) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(ip);
        sysUser.setLoginDate(DateUtils.getNowDate());
        sysUser.setUpdateBy(userId);
        DataPermissionHelper.ignore(() -> userMapper.updateById(sysUser));
    }

    /**
     * 登录校验
     */
    public void checkLogin(LoginType loginType, String tenantId, String username, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数，默认为0 (可自定义限制策略 例如: key + username + ip)
        int errorNumber = ObjectUtil.defaultIfNull(RedisUtils.getCacheObject(errorKey), 0);
        // 锁定时间内登录 则踢出
        if (errorNumber >= maxRetryCount) {
            recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 错误次数递增
            errorNumber++;
            RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
            // 达到规定错误次数 则锁定登录
            if (errorNumber >= maxRetryCount) {
                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数
                recordLogininfor(tenantId, username, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }
    /**
     * 校验租户
     *
     * @param tenantId 租户ID
     */
    public void checkTenant(String tenantId) {
        if (!TenantHelper.isEnable()) {
            return;
        }
        if (TenantConstants.DEFAULT_TENANT_ID.equals(tenantId)) {
            return;
        }
        if (StringUtils.isBlank(tenantId)) {
            throw new TenantException("tenant.number.not.blank");
        }
        SysTenantVo tenant = tenantService.queryByTenantId(tenantId);
        if (ObjectUtil.isNull(tenant)) {
            log.info("登录租户：{} 不存在.", tenantId);
            throw new TenantException("tenant.not.exists");
        } else if (TenantStatus.DISABLE.getCode().equals(tenant.getStatus())) {
            log.info("登录租户：{} 已被停用.", tenantId);
            throw new TenantException("tenant.blocked");
        } else if (ObjectUtil.isNotNull(tenant.getExpireTime())
            && new Date().after(tenant.getExpireTime())) {
            log.info("登录租户：{} 已超过有效期.", tenantId);
            throw new TenantException("tenant.expired");
        }
    }
    /**
     * 短信登录
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    public R<LoginVo> smsLogin(SmsLoginBody loginBody) {
        String phone= loginBody.getPhonenumber();
        //redis获取hash里面的值
        String regex = RedisUtils.getCacheMapValue(CacheConstants.SYS_CONFIG_KEYS , "sys.phone.regex");
        if (!StrUtil.isBlankIfStr(phone) && ReUtil.isMatch(regex,phone)) {
            // 授权类型和客户端id
            String clientId = loginBody.getClientId();
            String grantType = loginBody.getGrantType();
            SysClientVo client = clientService.queryByClientId(clientId);
            // 查询不到 client 或 client 内不包含 grantType
            if (ObjectUtil.isNull(client) || !StringUtils.contains(client.getGrantType(), grantType)) {
                log.info("客户端id: {} 认证类型：{} 异常!.", clientId, grantType);
                return R.fail(MessageUtils.message("auth.grant.type.error"));
            } else if (!UserConstants.NORMAL.equals(client.getStatus())) {
                return R.fail(MessageUtils.message("auth.grant.type.blocked"));
            }
            //根据手机号查询用户信息
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getPhonenumber, phone);
            SysUserVo user = userMapper.selectVoOne(queryWrapper);
            if (ObjectUtil.isNull(user)) {
                return R.fail("手机用户不存在");
            }
            String key = GlobalConstants.CAPTCHA_CODE_KEY + loginBody.getPhonenumber();
            String smsCode = RedisUtils.getCacheObject(key);
            if (StringUtils.isBlank(smsCode) || !smsCode.equals(loginBody.getSmsCode())) {
                log.info("短信验证码错误");
                return R.fail("验证码错误");
            }
            //校验租户
            checkTenant(user.getTenantId());
            //登录
            LoginVo loginVo = IAuthStrategy.login(JsonUtils.toJsonString(loginBody), client, grantType);

            Long userId = LoginHelper.getUserId();
            scheduledExecutorService.schedule(() -> {
                SseMessageDto dto = new SseMessageDto();
                dto.setMessage("欢迎登录");
                dto.setUserIds(List.of(userId));
                SseMessageUtils.publishMessage(dto);
            }, 5, TimeUnit.SECONDS);
            return R.ok(loginVo);
        }else {
            return R.fail("手机号格式不正确");
        }

    }
    /**
     * 忘记密码
     *
     * @param forgetPasswordBody 忘记密码信息
     * @return 结果
     */
    public R<Void> forgetPassword(ForgetPasswordBo forgetPasswordBody) {
        String phone= forgetPasswordBody.getPhonenumber();
        //redis获取hash里面的值
        String regex = RedisUtils.getCacheMapValue(CacheConstants.SYS_CONFIG_KEYS , "sys.phone.regex");
        if (!StrUtil.isBlankIfStr(phone) && ReUtil.isMatch(regex,phone)) {
            //根据手机号查询用户信息
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysUser::getPhonenumber, phone);
            SysUserVo user = userMapper.selectVoOne(queryWrapper);
            if (ObjectUtil.isNull(user)) {
                return R.fail("手机用户不存在");
            }
            String key = GlobalConstants.CAPTCHA_CODE_KEY +phone;
            String smsCode = RedisUtils.getCacheObject(key);
            if (StringUtils.isBlank(smsCode) || !smsCode.equals(forgetPasswordBody.getSmsCode())) {
                log.info("短信验证码错误 {}", smsCode);
                return R.fail("验证码错误");
            }
            user.setPassword(BCrypt.hashpw(forgetPasswordBody.getNewPassword()));
            SysUser users = BeanUtil.toBean(user, SysUser.class);
            int update=userMapper.updaUserPassword(users.getUserId(),users.getPassword());
            //使用自定义sql语句
            if (update > 0) {
                return R.ok();
            } else {
                log.error("修改密码失败，用户 ID：{}", user.getUserId());
                return R.fail("重置密码失败");
            }
        }else {
            return R.fail("手机号格式不正确");
        }
    }
}
