package org.smartlink.server.nc.ocr.service.ccint.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 合合配置
 *
 * @author L
 */
@Data
public class CcintOcrProperties implements Serializable {

    private String url;

    private String appKey;

    private String appSecret;

    private String userName;

    private String pwdMd5;

    private String userSalt;

    private String taxNo;

    private String ysOff;

    /**
     * 识别合同接口地址
     */
    private String docUrl;

    /**
     * 识别通用文档接口地址
     */
    private String documentOcrUrl;

    /**
     * 识别通用文档接口appId
     */
    private String documentAppId;

    /**
     * 识别通用文档接口secretCode
     */
    private String documentSecretCode;

}
