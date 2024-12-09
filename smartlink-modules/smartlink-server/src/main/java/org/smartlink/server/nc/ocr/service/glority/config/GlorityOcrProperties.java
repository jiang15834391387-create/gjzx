package org.smartlink.server.nc.ocr.service.glority.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 票小米配置
 *
 * @author L
 */
@Data
public class GlorityOcrProperties implements Serializable {

    private String url;

    private String appKey;

    private String appSecret;

    private String userName;

    private String pwdMd5;

    private String userSalt;

    private String taxNo;

}
