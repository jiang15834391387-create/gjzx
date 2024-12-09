package org.smartlink.server.nc.token.response;

import lombok.Data;

/**
 * @description: 获取NCC接口token
 * @author: L
 * @create:
 **/
@Data
public class NccTokenData {

    private String access_token;

    private String expires_in;

    private String refresh_token;

    private String security_key;

    private String ts;

    private String grant_type;

}
