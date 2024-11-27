package org.smartlink.server.zhiyun.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.smartlink.common.core.domain.R;
import org.smartlink.server.zhiyun.service.ZhiYunService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 智云单点登录接口
 *
 * @author: 马旭辉
 */
@RequestMapping("/server/zhiyun/")
@Controller
public class ZhiYunController {
    private final ZhiYunService zhiYunService;


    public ZhiYunController(ZhiYunService zhiYunService) {
        this.zhiYunService = zhiYunService;
    }

    /**
     * 根据智云code获取用户信息，然后登录影像系统
     *
     * @param code 智云带过来的code
     * @return 页面跳转
     */
    @GetMapping("login")
    public RedirectView login(@RequestParam("code") String code,
                              HttpServletRequest request) {

        return this.zhiYunService.login(code, request);
    }

    /**
     * 获取智云的Token
     *
     * @param code code
     * @return token
     */
    @GetMapping("getZhiYunToken")
    public R<String> getZhiYunToken(@RequestParam("code") String code) {
        return null;
    }
}
