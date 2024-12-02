package org.smartlink.server.zhiyun.service.impl;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.http.HttpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.zhiyun.service.ZhiYunService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 智云单点登录
 */
@Slf4j
@Service
public class ZhiYunServiceImpl implements ZhiYunService {


    @Value("${frontEnd.url}")
    private String frontEnd;

    @Value("${zhiyun.baseUrl}")
    private String zhiYunUrl;

    @Value("${zhiyun.getTokenUrl}")
    private String getToken;

    @Value("${zhiyun.getUserInfo}")
    private String getUserInfo;

    @Value("${zhiyun.appKey}")
    private String appKey;


    /**
     * 获取智云URL
     *
     * @return String
     */
    @Override
    public String getZhiYunUrl() {
        /// 线上换成配置的URL
        //String yxUrl = "http://127.0.0.1:8088/server/zhiyun/login";
        String yxUrl = "http://47.97.23.199:28080/documentInfo";

        String eUrl = URLEncodeUtil.encodeAll(yxUrl);

        String redirectUrl = this.zhiYunUrl + "/login?clinet_id=" + this.appKey + "&redirect_url=" + eUrl;

        log.info("获取智云URL----{}", redirectUrl);
        return redirectUrl;
    }

    /**
     * 根据智云code获取用户信息，然后登录影像系统
     *
     * @param code 智云CODE
     * @return String
     */
    @Override
    public RedirectView login(String code, HttpServletRequest request) {
        log.info("从智云获取到的Code为：{},开始跳转URL!", code);
        String newUrl = this.frontEnd + "/documentInfo?businessSerialNo=" + code;
        return new RedirectView(newUrl);
    }


    @Override
    public String getZhiYunToken(String code) {
        log.info("智云code为：{}", code);
        HttpUtil.createGet(this.zhiYunUrl + this.appKey);
        return "";
    }


    @Override
    public String getZhiYunUserInfo(String token) {
        return "";
    }
}
