package org.smartlink.server.zhiyun.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.smartlink.server.zhiyun.service.ZhiYunService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 智云单点登录
 */
@Service
public class ZhiYunServiceImpl implements ZhiYunService {


    @Value("${frontEnd.url}")
    private String frontEnd;

    /**
     * 根据智云code获取用户信息，然后登录影像系统
     *
     * @param code 智云CODE
     * @return String
     */
    @Override
    public RedirectView login(String code, HttpServletRequest request) {
        String newUrl = this.frontEnd + "/documentInfo?businessSerialNo=" + code;
        return new RedirectView(newUrl);
    }

    @Override
    public String getZhiYunToken(String code) {
        return "";
    }

    @Override
    public String getZhiYunUserInfo(String token) {
        return "";
    }
}
