package org.smartlink.web.service.nc.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import org.smartlink.common.core.enums.DeviceType;
import org.smartlink.web.domain.modle.LoginUser;
import org.smartlink.web.helper.LoginHelper;
import org.smartlink.web.service.nc.ExternalTokenService;
import org.smartlink.web.service.nc.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ExternalTokenServiceImpl implements ExternalTokenService {
    @Autowired
    private ISysUserService iSysUserService;
    /**
     * 单点登录
     *
     * @return TokenValue
     */
    @Override
    public String getNccToken(String userId,String userNo) {
        Set<String> perms = new HashSet<String>();
        perms.add("*:*:*");
        LoginUser loginUser = new LoginUser();
        loginUser.setUserType("sys_user");
        if(StrUtil.isNotEmpty(userId)){
            loginUser.setUserId(Long.valueOf(userId));
        }else{
            loginUser.setUserId(1L);
        }
        loginUser.setMenuPermission(perms);
        String userName = iSysUserService.selectUserNameByUserId(userId);
        if(StrUtil.isEmpty(userName)){
            loginUser.setUsername("未知用户");
        }else{
            loginUser.setUsername(userName);
            loginUser.setUserNo(userNo);
        }
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        return StpUtil.getTokenValueByLoginId(loginUser.getLoginId());
    }

    @Override
    public String getBIPToken(String tenantId,String userName,String userCode,String userId) {
        Set<String> perms = new HashSet<String>();
        perms.add("*:*:*");
        LoginUser loginUser = new LoginUser();
        loginUser.setUserType("sys_user");
        loginUser.setUserId(1L);
        loginUser.setMenuPermission(perms);
        if (StrUtil.isBlank(userName)){
            loginUser.setUsername("admin");
        }else{
            loginUser.setUsername(userName);
            loginUser.setUserNo(userCode);
            loginUser.setNccUserId(userId);
        }
        loginUser.setTenantId(tenantId);
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);
        return StpUtil.getTokenValueByLoginId(loginUser.getLoginId());
    }
}
