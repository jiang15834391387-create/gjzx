package org.smartlink.server.nc.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @description: NCC参数配置
 * @author: L
 * @create:
 **/
@Data
@Component
public class NccParamProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否调用NCC OCR接口
     */
    private String nccOcr;

    /**
     * 是否调用NCC 查验接口
     */
    private String nccCheck;

    /**
     * NCC业务系统版本
     */
    private String nccVersion;

    /**
     * 影像厂商标识
     */
    private String factoryCode;

    /**
     * NCC系统加密字段参数
     */
    private String clientSecret;

    /**
     * NCC系统client id
     */
    private String clientId;

    /**
     * NCC系统管理员账号
     */
    private String nccUserName;

    /**
     * NCC系统管理员密码
     */
    private String nccPassword;

    /**
     * NCC系统帐套编码
     */
    private String bizCenter;

    /**
     * NCC系统源路径
     */
    @Value("${ncc.baseUrl}")
    private String baseUrl;

    /**
     * 影像系统访问地址
     */
    private String systemIp;

    /**
     * 影像系统访问端口
     */
    private String systemPort;

    /**
     * 业务系统ws接口地址
     */
    @Value("${ncc.wsUrl}")
    private String wsUrl;

    /**
     * NCC系统数据源
     */
    private String dataSource;

    /**
     * 加密类型
     */
    private String encryptType;

    /**
     * 接口请求类型  eq：【0】增值税接口调用，【1】全票种接口调用
     */
    private String interFaceType;

}
