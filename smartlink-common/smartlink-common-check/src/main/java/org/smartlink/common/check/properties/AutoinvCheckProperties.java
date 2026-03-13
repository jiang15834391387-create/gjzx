package org.smartlink.common.check.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * autoinv查验配置
 *
 */
@Data
public class AutoinvCheckProperties implements Serializable {
    //厂商
    private String autoinv;
    //请求地址
    private String url;
    //许可码
    private String permitCode;
}
