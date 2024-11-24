package org.smartlink.web.token.request;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: NCC获取token接口参数请求类
 * @author: L
 * @create:
 **/
@Data
@Component
public class NccTokenRequest {

    /**
     * NCC系统地址
     */
    @Value("${wsdl.baseUrl}")
    private String baseUrl;

    @Value("${wsdl.wsUrl}")
    private String wsUrl;
    /**
     * NCC系统client id
     */
    @Value("${wsdl.clientId}")
    private String clientId;

    /**
     * NCC系统加密字段参数
     */
    @Value("${wsdl.clientSecret}")
    private String clientSecret;

    /**
     * NCC系统账号
     */
    @Value("${wsdl.nccUserName}")
    private String nccUserName;

    /**
     * NCC系统密码
     */
    @Value("${wsdl.nccPassword}")
    private String nccPassword;

    /**
     * NCC系统帐套编码
     */
    @Value("${wsdl.bizCenter}")
    private String bizCenter;




}
