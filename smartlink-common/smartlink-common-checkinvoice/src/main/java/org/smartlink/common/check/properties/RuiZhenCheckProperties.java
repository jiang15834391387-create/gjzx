package org.smartlink.common.check.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 睿真查验配置
 *
 */
@Data
public class RuiZhenCheckProperties implements Serializable {

    private String url;

    private String appKey;

    private String appSecret;

}
