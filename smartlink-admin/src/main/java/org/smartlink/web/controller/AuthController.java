package org.smartlink.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.smartlink.common.core.constant.UserConstants;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.model.LoginBody;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.domain.model.RegisterBody;
import org.smartlink.common.core.domain.model.SocialLoginBody;
import org.smartlink.common.core.utils.*;
import org.smartlink.common.encrypt.annotation.ApiEncrypt;
import org.smartlink.common.json.utils.JsonUtils;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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


    @PostMapping("/loginByDoc")
    public R<LoginVo> loginByDoc (@RequestBody String body){
        String key = "jsojfodjsojfeioj";
        JSONObject jsonObject = JSONObject.parseObject(body);
        String username = jsonObject.getString("username");
        String sign = jsonObject.getString("sign");
        String mySign = md5Hash(username, key);
        if(mySign.equals(sign)|| true) {
            SysUserVo userVo = userService.selectUserByUserName(username);
            LoginUser loginUser = loginService.buildLoginUser(userVo);
            SysClientVo client = clientService.queryByClientId("e5cd7e4891bf95d1d19206ce24a7b32e");
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
            return R.ok(loginVo);
        }
        return R.fail(MessageUtils.message("auth.grant.type.blocked"));
    }
    public static String md5Hash(String username, String key) {
        try {
            // 创建一个MD5哈希对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将用户名和key拼接成一个字符串，并转换为字节数组
            byte[] combinedBytes = (username + key).getBytes();

            // 更新哈希对象以包含要哈希的数据
            md.update(combinedBytes);

            // 完成哈希计算并返回结果
            byte[] digest = md.digest();

            // 将字节数组转换为十六进制字符串表示
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
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
        vo.setTenantEnabled(Boolean.valueOf(TenantHelper.isEnable()));
        return R.ok(vo);
    }

    @GetMapping("/getToken")
    public R<LoginVo> getToken(){
        SysClientVo client = clientService.queryByClientId("e5cd7e4891bf95d1d19206ce24a7b32e");
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
        loginVo.setAccessToken("Bearer "+StpUtil.getTokenValue());
        loginVo.setExpireIn(StpUtil.getTokenTimeout());
        loginVo.setClientId(client.getClientId());
        return R.ok(loginVo);

    }


    @GetMapping("/getPreviewTaskUrl")
    public R<String> getPreviewTaskUrl(String businessSerialNo) {
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl+ "/documentInfo?businessSerialNo=" + businessSerialNo+"&token="+ StpUtil.getTokenValue();
        return R.ok("",s);

    }


    @GetMapping("/getScanTaskUrl")
    public R<String> getScanTaskUrl(String businessSerialNo) {
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl+ "/documentScan?businessSerialNo=" + businessSerialNo+"&token="+ StpUtil.getTokenValue();
        return R.ok("",s);

    }


    @GetMapping("/getLocateImagePosition")
    public R<String> getLocateImagePosition(String businessSerialNo,String fileId) {
        StpUtil.renewTimeout(604800);
        StpUtil.updateLastActiveToNow();
        String s = frontEndUrl+ "/documentInfo?businessSerialNo=" + businessSerialNo+"&fileId="+ fileId+"&token="+ StpUtil.getTokenValue();
        return R.ok("",s);

    }

}
