package org.smartlink.web.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.domain.model.PasswordLoginBody;
import org.smartlink.common.core.enums.LoginType;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.ValidatorUtils;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.common.tenant.helper.TenantHelper;
import org.smartlink.system.domain.SysUser;
import org.smartlink.system.domain.vo.SysClientVo;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.web.domain.vo.LoginVo;
import org.smartlink.web.service.ILoginService;
import org.smartlink.web.service.SysLoginService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements ILoginService {
    private final SysLoginService loginService;
    private final SysUserMapper userMapper;
    public LoginServiceImpl(SysLoginService loginService, SysUserMapper userMapper) {
        this.loginService = loginService;
        this.userMapper = userMapper;
    }

    // app账号密码登录
    public LoginVo applogin(PasswordLoginBody loginBody, SysClientVo client) {
//        PasswordLoginBody loginBody = JsonUtils.parseObject(body, PasswordLoginBody.class);
//        ValidatorUtils.validate(loginBody);
        String tenantId = loginBody.getTenantId();
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        LoginUser loginUser = TenantHelper.dynamic(tenantId, () -> {
            SysUserVo user = loadUserByUsername(username);
            loginService.checkLogin(LoginType.PASSWORD, tenantId, username, () -> !BCrypt.checkpw(password, user.getPassword()));
            // 此处可根据登录用户的数据不同 自行创建 loginUser
            return loginService.buildLoginUser(user);
        });
        loginUser.setClientKey(client.getClientKey());
        loginUser.setDeviceType(client.getDeviceType());
        SaLoginModel model = new SaLoginModel();
        model.setDevice(client.getDeviceType());
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.getTimeout());
        model.setActiveTimeout(client.getActiveTimeout());
        model.setExtra(LoginHelper.CLIENT_KEY, client.getClientId());
        // 生成token
        LoginHelper.login(loginUser, model);

        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        SysUserVo sysUserVo = loadUserByUsername(username);
        loginVo.setNickName(sysUserVo.getNickName());
        return loginVo;
    }


    //app根据手机号获取用户信息
    private SysUserVo loadUserByUsername(String username) {
        SysUserVo user = userMapper.selectVoOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getPhonenumber, username));
        if (ObjectUtil.isNull(user)) {
            log.info("手机用户不存在");
            throw new ServiceException("手机用户不存在");
        }
        return user;
    }
}
