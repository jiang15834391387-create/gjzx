package org.smartlink.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.NotLoginException;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.apache.poi.ss.formula.functions.T;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.common.core.constant.CacheConstants;
import org.smartlink.common.core.constant.GlobalConstants;
import org.smartlink.common.core.constant.UserConstants;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.model.*;
import org.smartlink.common.core.enums.FileStatusEnumd;
import org.smartlink.common.core.utils.*;
import org.smartlink.common.encrypt.annotation.ApiEncrypt;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.common.social.config.properties.SocialLoginConfigProperties;
import org.smartlink.common.social.config.properties.SocialProperties;
import org.smartlink.common.social.utils.SocialUtils;
import org.smartlink.common.sse.dto.SseMessageDto;
import org.smartlink.common.sse.utils.SseMessageUtils;
import org.smartlink.common.tenant.helper.TenantHelper;
import org.smartlink.system.domain.bo.SysTenantBo;
import org.smartlink.system.domain.bo.SysUserBo;
import org.smartlink.system.domain.vo.SysClientVo;
import org.smartlink.system.domain.vo.SysTenantVo;
import org.smartlink.system.domain.vo.SysUserExportVo;
import org.smartlink.system.service.*;
import org.smartlink.web.domain.bo.ForgetPasswordBo;
import org.smartlink.web.domain.vo.LoginTenantVo;
import org.smartlink.web.domain.vo.LoginVo;
import org.smartlink.web.domain.vo.TenantListVo;
import org.smartlink.web.service.IAuthStrategy;
import org.smartlink.web.service.ILoginService;
import org.smartlink.web.service.SysLoginService;
import org.smartlink.web.service.SysRegisterService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 认证
 *
 * @author Lion Li
 */
@Slf4j
@SaIgnore
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final ISysUserService userService;
    private final ScanImageService scanImageService;
    ////////////////
    private final SocialProperties socialProperties;
    private final SysLoginService loginService;
    private final SysRegisterService registerService;
    private final ISysConfigService configService;
    private final ISysTenantService tenantService;
    private final ISysSocialService socialUserService;
    private final ISysClientService clientService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ILoginService iLoginService;

    /**
     * 登录方法
     *
     * @param body 登录信息
     * @return 结果
     */
    @ApiEncrypt
    @PostMapping("/login")
    public R<LoginVo> login(@RequestBody String body) {
        LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
        ValidatorUtils.validate(loginBody);
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
        // 校验租户
        loginService.checkTenant(loginBody.getTenantId());
        // 登录
        LoginVo loginVo = IAuthStrategy.login(body, client, grantType);
        Long userId = LoginHelper.getUserId();
        scheduledExecutorService.schedule(() -> {
            SseMessageDto dto = new SseMessageDto();
            dto.setMessage("欢迎登录smartlink后台管理系统");
            dto.setUserIds(List.of(userId));
            SseMessageUtils.publishMessage(dto);
        }, 5, TimeUnit.SECONDS);
        return R.ok(loginVo);
    }

    /**
     * 第三方登录请求
     *
     * @param source 登录来源
     * @return 结果
     */
    @GetMapping("/binding/{source}")
    public R<String> authBinding(@PathVariable("source") String source,
                                 @RequestParam String tenantId, @RequestParam String domain) {
        SocialLoginConfigProperties obj = socialProperties.getType().get(source);
        if (ObjectUtil.isNull(obj)) {
            return R.fail(source + "平台账号暂不支持");
        }
        AuthRequest authRequest = SocialUtils.getAuthRequest(source, socialProperties);
        Map<String, String> map = new HashMap<>();
        map.put("tenantId", tenantId);
        map.put("domain", domain);
        map.put("state", AuthStateUtils.createState());
        String authorizeUrl = authRequest.authorize(Base64.encode(JsonUtils.toJsonString(map), StandardCharsets.UTF_8));
        return R.ok("操作成功", authorizeUrl);
    }

    /**
     * 第三方登录回调业务处理 绑定授权
     *
     * @param loginBody 请求体
     * @return 结果
     */
    @PostMapping("/social/callback")
    public R<Void> socialCallback(@RequestBody SocialLoginBody loginBody) {
        // 获取第三方登录信息
        AuthResponse<AuthUser> response = SocialUtils.loginAuth(
                loginBody.getSource(), loginBody.getSocialCode(),
                loginBody.getSocialState(), socialProperties);
        AuthUser authUserData = response.getData();
        // 判断授权响应是否成功
        if (!response.ok()) {
            return R.fail(response.getMsg());
        }
        loginService.socialRegister(authUserData);
        return R.ok();
    }


    /**
     * 取消授权
     *
     * @param socialId socialId
     */
    @DeleteMapping(value = "/unlock/{socialId}")
    public R<Void> unlockSocial(@PathVariable Long socialId) {
        Boolean rows = socialUserService.deleteWithValidById(socialId);
        return rows ? R.ok() : R.fail("取消授权失败");
    }


    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        loginService.logout();
        return R.ok("退出成功");
    }

    /**
     * 用户注册
     */
    @ApiEncrypt
    @PostMapping("/register")
    public R<Void> register(@Validated @RequestBody RegisterBody user) {
        if (!configService.selectRegisterEnabled(user.getTenantId())) {
            return R.fail("当前系统没有开启注册功能！");
        }
        String phone= user.getPhonenumber();
        //redis获取hash里面的值
        String regex = RedisUtils.getCacheMapValue(CacheConstants.SYS_CONFIG_KEYS , "sys.phone.regex");
        if (!StrUtil.isBlankIfStr(phone) && ReUtil.isMatch(regex,phone)) {
            return R.fail("手机号格式错误");
        }
        String key = GlobalConstants.CAPTCHA_CODE_KEY +user.getPhonenumber();
        String smsCode = RedisUtils.getCacheObject(key);
        if (StringUtils.isBlank(smsCode) || !smsCode.equals(user.getSmsCode())) {
            log.info("短信验证码错误 {}", smsCode);
            return R.fail("验证码错误");
        }
        registerService.register(user);
        return R.ok();
    }

    /**
     * 登录页面租户下拉框
     *
     * @return 租户列表
     */
    @GetMapping("/tenant/list")
    public R<LoginTenantVo> tenantList(HttpServletRequest request) throws Exception {
        // 返回对象
        LoginTenantVo result = new LoginTenantVo();
        boolean enable = TenantHelper.isEnable();
        result.setTenantEnabled(enable);
        // 如果未开启租户这直接返回
        if (!enable) {
            return R.ok(result);
        }

        List<SysTenantVo> tenantList = tenantService.queryList(new SysTenantBo());
        List<TenantListVo> voList = MapstructUtils.convert(tenantList, TenantListVo.class);
        try {
            // 如果只超管返回所有租户
            if (LoginHelper.isSuperAdmin()) {
                result.setVoList(voList);
                return R.ok(result);
            }
        } catch (NotLoginException ignored) {
        }

        // 获取域名
        String host;
        String referer = request.getHeader("referer");
        if (StringUtils.isNotBlank(referer)) {
            // 这里从referer中取值是为了本地使用hosts添加虚拟域名，方便本地环境调试
            host = referer.split("//")[1].split("/")[0];
        } else {
            host = new URL(request.getRequestURL().toString()).getHost();
        }
        // 根据域名进行筛选
        List<TenantListVo> list = StreamUtils.filter(voList, vo ->
                StringUtils.equals(vo.getDomain(), host));
        result.setVoList(CollUtil.isNotEmpty(list) ? list : voList);
        return R.ok(result);
    }
    /**
     * @Description:短信登录
     * @Author: Mr.Meng
     * @Date: 2025/1/7
     */
    @ApiEncrypt
    @PostMapping("/sms/login")
    public R<LoginVo> smsLogin(@RequestBody SmsLoginBody loginBody) {
        return loginService.smsLogin(loginBody);
    }
    /**
     * @Description:忘记密码
     * @Author: Mr.Meng
     * @Date: 2025/1/7
     */
    @PostMapping("/forget/password")
    public R<Void> forgetPassword(@RequestBody ForgetPasswordBo forgetPasswordBody) {
        return loginService.forgetPassword(forgetPasswordBody);
    }

    //app账号密码登录
    @PostMapping("/applogin")
    public R<LoginVo> applogin(@RequestBody LoginBody loginBody, PasswordLoginBody loginBodyPass) {
//        LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
//        LoginBody loginBody = JsonUtils.parseObject(body, LoginBody.class);
        ValidatorUtils.validate(loginBody);
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
        // 校验租户
        loginService.checkTenant(loginBody.getTenantId());
        // 登录
        LoginVo loginVo = iLoginService.applogin(loginBodyPass, client);
        loginVo.setNickName(loginVo.getNickName());
        Long userId = LoginHelper.getUserId();
        scheduledExecutorService.schedule(() -> {
            SseMessageDto dto = new SseMessageDto();
            dto.setMessage("欢迎登录");
            dto.setUserIds(List.of(userId));
            SseMessageUtils.publishMessage(dto);
        }, 5, TimeUnit.SECONDS);
        return R.ok(loginVo);
    }

    /**
     * 发票上传
     *
     * @param files 多文件对象
     * @param uploadType 上传类型 0 邮件 1 手动
     */
//    @Log(title = "发票上传", businessType = BusinessType.INSERT)
    @SaIgnore
    @PostMapping("/upload")
    public R<T> upload(@RequestParam(value = "files", required = false) MultipartFile[] files,
                       @RequestParam(value = "uploadType") String uploadType) throws Exception {
        R<T> res = null;
        LoginBody loginBody = new LoginBody();

        List<SysUserExportVo> list = userService.selectUserExportList(new SysUserBo());
        loginBody.setClientId("428a8310cd442757ae699df5d894f051");
        loginBody.setTenantId("000000");
        loginBody.setGrantType("password,sms,social");
        for (SysUserExportVo user : list) {
            PasswordLoginBody loginBodyPass = new PasswordLoginBody();
            loginBodyPass.setUsername(user.getPhonenumber()); // 覆盖式赋值
            loginBodyPass.setPassword("admin123"); // 覆盖式赋值
            loginBodyPass.setTenantId("000000"); // 覆盖式赋值



//                ValidatorUtils.validate(loginBody);
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
                // 校验租户
                loginService.checkTenant(loginBody.getTenantId());
                // 登录
                LoginVo loginVo = iLoginService.applogin(loginBodyPass, client);
                loginVo.setNickName(loginVo.getNickName());
                Long userId = LoginHelper.getUserId();
                scheduledExecutorService.schedule(() -> {
                    SseMessageDto dto = new SseMessageDto();
                    dto.setMessage("欢迎登录");
                    dto.setUserIds(List.of(userId));
                    SseMessageUtils.publishMessage(dto);
                }, 5, TimeUnit.SECONDS);


            if (uploadType.equals("0")){
                List<MultipartFile> fetchFilesFromEmail = scanImageService.fetchFilesFromEmail();
                if (fetchFilesFromEmail.size()!= 0){
                    for (MultipartFile multipart : fetchFilesFromEmail) {
                        res = scanImageService.uploadImage(multipart, uploadType);
                    }
                }
            } else {
                if (files != null && files.length > 0) {
                    for (MultipartFile file : files) {
                        res = scanImageService.uploadImage(file, uploadType);
                        if (res.getMsg().equals(FileStatusEnumd.OCR_FAILED.getDesc())) {
                            continue;  // 跳过当前文件，继续处理下一个
                        }
                    }
                } else {
                    return R.fail("请上传文件!");
                }
            }
            break; // 只执行一次，跳出循环
        }



        return res;
    }


}
