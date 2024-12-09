package org.smartlink.server.nc.utils.biptoken.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @description: 获取BIP接口token
 * @author: L
 * @create:
 **/
@Data
public class BipTokenData {

    /**
     * 获取的访问令牌 access_token
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * 访问令牌的过期时间，单位秒
     */
    private long expire;
}
