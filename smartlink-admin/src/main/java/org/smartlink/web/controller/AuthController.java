package org.smartlink.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.codec.Base64;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;


import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.smartlink.common.core.constant.UserConstants;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.model.LoginBody;
import org.smartlink.common.core.domain.model.RegisterBody;
import org.smartlink.common.core.domain.model.SocialLoginBody;
import org.smartlink.common.core.utils.*;
import org.smartlink.common.encrypt.annotation.ApiEncrypt;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.common.social.config.properties.SocialLoginConfigProperties;
import org.smartlink.common.social.config.properties.SocialProperties;
import org.smartlink.common.social.utils.SocialUtils;
import org.smartlink.common.tenant.helper.TenantHelper;
import org.smartlink.common.websocket.dto.WebSocketMessageDto;
import org.smartlink.common.websocket.utils.WebSocketUtils;
import org.smartlink.system.domain.bo.SysTenantBo;
import org.smartlink.system.domain.vo.SysClientVo;
import org.smartlink.system.domain.vo.SysTenantVo;
import org.smartlink.system.service.*;
import org.smartlink.web.domain.vo.LoginTenantVo;
import org.smartlink.web.domain.vo.LoginVo;
import org.smartlink.web.domain.vo.TenantListVo;
import org.smartlink.web.service.IAuthStrategy;
import org.smartlink.web.service.SysLoginService;
import org.smartlink.web.service.SysRegisterService;
import org.smartlink.web.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static me.zhyd.oauth.cache.AuthCacheConfig.timeout;

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

    private final SocialProperties socialProperties;
    private final SysLoginService loginService;
    private final SysRegisterService registerService;
    private final ISysConfigService configService;
    private final ISysTenantService tenantService;
    private final ISysSocialService socialUserService;
    private final ISysClientService clientService;
    private final ScheduledExecutorService scheduledExecutorService;
    private final ISysClientService iSysClientService;
    private final ISysUserService userService;

    @Value("${frontEnd.url}")
    private String frontEndUrl;

    @Value("${runJian.ceShi.url}")
    private  String runJianUrl;

    @Value("${runJian.ceShi.appKey}")
    private  String runJianAppKey;

    @Value("${runJian.ceShi.appSecret}")
    private  String runJianAppSecret;

    @Value("${runJian.ceShi.tokenUrl}")
    private  String tokenUrl;

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
            WebSocketMessageDto dto = new WebSocketMessageDto();
            dto.setMessage("欢迎登录smartlink后台管理系统");
            dto.setSessionKeys(List.of(userId));
            WebSocketUtils.publishMessage(dto);
        }, 3, TimeUnit.SECONDS);
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
        List<SysTenantVo> tenantList = tenantService.queryList(new SysTenantBo());
        List<TenantListVo> voList = MapstructUtils.convert(tenantList, TenantListVo.class);
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
        // 返回对象
        LoginTenantVo vo = new LoginTenantVo();
        vo.setVoList(CollUtil.isNotEmpty(list) ? list : voList);
        vo.setTenantEnabled(TenantHelper.isEnable());
        return R.ok(vo);
    }


    @GetMapping("/getToken")
    public R<LoginVo> getToken() throws IOException {

        //构建请求，获取access_token
        String url = runJianUrl + tokenUrl;
        // 创建HttpClient实例
        HttpClient httpClient = HttpClientBuilder.create().build();
        // 创建POST请求
        HttpPost request = new HttpPost(url);
        // 添加请求头
        request.addHeader("Content-Type", "application/json");

        // 添加请求体（JSON数据）
        String jsonBody = "{\"appKey\": \"" + runJianAppKey + "\",\n" +
                "\"appSecret\": \"" + runJianAppSecret + "\"\n" +
                "}";
        request.setEntity(new StringEntity(jsonBody));
        HttpResponse response = httpClient.execute(request);
        //解析响应为map集合
        Map<String, Object> mapResponse = ResponseUtil.handleResponse(response);
        //获取其中的值
        int errcode = (Integer) mapResponse.get("errcode");
        String errmsg = (String) mapResponse.get("errmsg");
        Map<String, Object> data = (Map<String, Object>) mapResponse.get("data");
        String accessToken = (String) data.get("accessToken");
        long expireIn = (long)(int) data.get("expireIn");
        //将获取的accesstoken存入redis
        RedisUtils.setCacheObject("accessToken", accessToken,Duration.ofMillis(timeout));

        if (errcode != 200) {
            log.info("获取内部文件系统token有误，请检查appKey:{},appSecret:{}", runJianAppKey, runJianAppSecret);
            return R.fail("获取内部文件系统token有误，请检查文件系统是否有误或发送的appKey和appSecret");
        }
        //获取请求是app还是pc
//        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//        String clientKey = httpServletRequest.getHeader("User-Agent");
//        // 通过clientKey查询SysClient对象
//        SysClientVo sysClientVo = iSysClientService.findByClientKey(clientKey);
        //构建返回对象
        LoginVo loginVo = new LoginVo();
        loginVo.setAccessToken(accessToken);
        loginVo.setExpireIn(expireIn);
        return R.ok(loginVo);
    }


    @GetMapping("/getPreviewTaskUrl")
    public R<String> getPreviewTaskUrl(String businessSerialNo) {
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl + "/documentInfo?businessSerialNo=" + businessSerialNo + "&token=" + StpUtil.getTokenValue();
        return R.ok("", s);

    }


    @GetMapping("/getScanTaskUrl")
    public R<String> getScanTaskUrl(String businessSerialNo) {
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl + "/documentScan?businessSerialNo=" + businessSerialNo + "&token=" + StpUtil.getTokenValue();
        return R.ok("", s);

    }


    @GetMapping("/getLocateImagePosition")
    public R<String> getLocateImagePosition(String businessSerialNo, String fileId) {
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl + "/documentInfo?businessSerialNo=" + businessSerialNo + "&fileId=" + fileId + "&token=" + StpUtil.getTokenValue();
        return R.ok("", s);

    }

}
