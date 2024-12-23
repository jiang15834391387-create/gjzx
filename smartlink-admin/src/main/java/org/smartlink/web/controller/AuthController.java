package org.smartlink.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.smartlink.common.core.constant.CacheConstants;
import org.smartlink.common.core.constant.UserConstants;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.model.LoginBody;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.domain.model.RegisterBody;
import org.smartlink.common.core.domain.model.SocialLoginBody;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.*;
import org.smartlink.common.encrypt.annotation.ApiEncrypt;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.oss.constant.OssConstant;
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
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.service.*;
import org.smartlink.web.accessToken.AccessTokenVerify;
import org.smartlink.web.domain.vo.LoginTenantVo;
import org.smartlink.web.domain.vo.LoginVo;
import org.smartlink.web.domain.vo.TenantListVo;
import org.smartlink.web.service.IAuthStrategy;
import org.smartlink.web.service.SysLoginService;
import org.smartlink.web.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

    private final SocialProperties socialProperties;
    private final SysLoginService loginService;
    private final SysRegisterService registerService;
    private final ISysConfigService configService;
    private final ISysTenantService tenantService;
    private final ISysSocialService socialUserService;
    private final ISysClientService clientService;
    private final ScheduledExecutorService scheduledExecutorService;

    private final ISysUserService userService;

    @Value("${frontEnd.url}")
    private String frontEndUrl;

    @Value("${zhiyun.baseUrl}")
    private String zhiYunUrl;

    @Value("${zhiyun.getTokenUrl}")
    private String getToken;

    @Value("${zhiyun.getUserInfo}")
    private String getUserInfo;

    @Value("${zhiyun.appKey}")
    private String appKey;

    @Value("${zhiyun.appSecret}")
    private String appSecret;

    @Value("${clientId}")
    private String clientId;

    /**
     * 获取智云的Token
     *
     * @param code code
     * @return token
     */
    @GetMapping("getZhiYunToken")
    public R<JSONObject> getZhiYunToken(@RequestParam("code") String code) {
        log.info("智云code为：{}", code);
        String url = this.zhiYunUrl + this.getToken + "?appKey=" + this.appKey + "&appSecret=" + this.appSecret + "&code=" + code;
        log.info("请求完整URL:{}", url);
        final HttpRequest httpRequest = HttpUtil.createGet(url);
        try (HttpResponse execute = httpRequest.execute()) {
            final String body = execute.body();
            log.info("智云原始返回结果为：{}", body);
            final JSONObject result = JSONUtil.parseObj(body);
            // 登录一下，然后set进去返回一个token
            result.set("token", this.getYxToken("admin"));
            // 返回
            return R.ok(result);
        } catch (Exception e) {
            throw new ServiceException("获取智云token失败");
        }
    }


    private String getYxToken(String userName) {
        log.info("clientId:{}", this.clientId);
        SysClientVo client = clientService.queryByClientId(this.clientId);

        SysUserVo user = userService.selectUserByUserName(userName);
        LoginUser loginUser = loginService.buildLoginUser(user);
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
        loginVo.setAccessToken("Bearer " + StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return StpUtil.getTokenValue();
    }

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
    public R<LoginVo> getToken(@RequestParam("appKey") String appKey,
                               @RequestParam("timestamp") String timestamp,
                               @RequestParam("signature") String signature,
                               @RequestParam("tenantId") String tenantId) {


        SysClientVo client = clientService.findByClientKey(appKey);
        SysUserVo user = userService.selectUserById(1l);
        LoginUser loginUser = loginService.buildLoginUser(user);
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
        loginVo.setAccessToken("Bearer " + StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());

        //将token放入redis
        Long expireIn = loginVo.getExpireIn();
        Duration duration = Duration.ofSeconds(expireIn);
        RedisUtils.setCacheObject(CacheConstants.YINGXIANG_ACCESSTOKEN, loginVo.getAccessToken(), duration);

        return R.ok(loginVo);
    }

    private String createTempTask(String fileIds) {
        final String replace = fileIds.replace("@", ",");
        String taskId = "temp" + IdUtil.simpleUUID();
        log.info("创建临时任务：{},文件id值：{}", taskId, replace);
        RedisUtils.setCacheObject(OssConstant.RUN_JIAN_TOKEN_KEY + taskId, replace, Duration.ofMillis(2 * 60 * 1000));
        return taskId;
    }

    @AccessTokenVerify
    @GetMapping("/getPreviewTaskUrl")
    public R<String> getPreviewTaskUrl(@RequestParam("businessSerialNo") String businessSerialNo,
                                       @RequestParam(required = false) String uid,
                                       @RequestParam(required = false) String fileIds) {
        if (StringUtils.isNotEmpty(fileIds)) {
            log.info("影像查看，接收到一组文件ID:{}", fileIds);
            String tempTask = this.createTempTask(fileIds);
            businessSerialNo = businessSerialNo + "@" + tempTask;
        }
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl + "/documentInfo?businessSerialNo=" + businessSerialNo + "&uid=" + uid + "&token=" + StpUtil.getTokenValue();
        return R.ok("", s);
    }

    /**
     * 多单据联查，润健客开，需要传UID
     *
     * @param businessSerialNo 单据流水号，多个使用@符号分割
     * @param uid              润健工号
     * @return 影像查看预览地址
     */
    @AccessTokenVerify
    @GetMapping("/getPreviewMultipleTaskUrl")
    public R<String> getPreviewMultipleTaskUrl(@RequestParam("businessSerialNo") String businessSerialNo,
                                               @RequestParam("uid") String uid,
                                               @RequestParam("fileIds") String fileIds) {
        log.info("单据联查，接收到参数:businessSerialNo:{},uid:{}", businessSerialNo, uid);
        if (StringUtils.isNotEmpty(fileIds)) {
            log.info("单据影像查看，接收到一组文件ID:{}", fileIds);
            String tempTask = this.createTempTask(fileIds);
            businessSerialNo = businessSerialNo + "@" + tempTask;
        }
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl + "/documentInfo?businessSerialNo=" + businessSerialNo + "&uid=" + uid + "&token=" + StpUtil.getTokenValue();
        return R.ok("", s);

    }

    @AccessTokenVerify
    @GetMapping("/getScanTaskUrl")
    public R<String> getScanTaskUrl(String businessSerialNo) {
//        InetAddress localHost = InetAddress.getLocalHost();

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
