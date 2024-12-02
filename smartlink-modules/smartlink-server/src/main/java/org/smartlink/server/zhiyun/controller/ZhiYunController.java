package org.smartlink.server.zhiyun.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.smartlink.common.core.domain.R;
import org.smartlink.server.zhiyun.service.ZhiYunService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 智云单点登录接口
 * 账号：85026
 * 秘密：219#yfb@rjgf.com
 *
 * 测试账号：102747
 * 测试密码：219#yfb@rjgf.com
 *
 * @author: 马旭辉
 */
@RequestMapping("/server/zhiyun/")
@RestController
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
        return R.ok(this.zhiYunService.getZhiYunToken(code));
    }
}
