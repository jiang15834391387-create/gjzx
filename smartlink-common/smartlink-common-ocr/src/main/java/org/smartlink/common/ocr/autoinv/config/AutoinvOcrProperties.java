package org.smartlink.common.ocr.autoinv.config;

import lombok.Data;

/**
 * autoinv OCR 配置类
 */
@Data
public class AutoinvOcrProperties {
    /**
     * API URL
     */
    private String url;

    /**
     * 访问令牌
     */
    private String accessToken;

    /**
     * 子账号名称(字母或数字)
     */
    private String subName;
}
