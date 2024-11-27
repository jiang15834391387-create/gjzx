package org.smartlink.server.zhiyun.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 智云登录Service
 *
 * @author: 马旭辉
 */
public interface ZhiYunService {
    /**
     * 根据智云code获取用户信息，然后登录影像系统
     *
     * @param code 智云CODE
     */
    RedirectView login(String code, HttpServletRequest request);

    /**
     * 获取智云的Token
     *
     * @param code code
     * @return token
     */
    String getZhiYunToken(String code);

    String getZhiYunUserInfo(String token);
}
