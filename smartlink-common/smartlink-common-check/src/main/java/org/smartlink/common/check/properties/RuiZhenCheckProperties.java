package org.smartlink.common.check.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 睿真查验配置
 *
 */
@Data
public class RuiZhenCheckProperties implements Serializable {
    //厂商
    private String regenai;
    //请求地址
    private String url;
    //appKey
    private String appKey;
    //appSecret
    private String appSecret;

}
