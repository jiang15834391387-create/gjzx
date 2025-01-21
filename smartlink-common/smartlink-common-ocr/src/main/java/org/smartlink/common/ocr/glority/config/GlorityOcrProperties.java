package org.smartlink.common.ocr.glority.config;

import lombok.Data;

import java.io.Serializable;

/**
 * 票小秘配置
 *
 * @author lqm
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
