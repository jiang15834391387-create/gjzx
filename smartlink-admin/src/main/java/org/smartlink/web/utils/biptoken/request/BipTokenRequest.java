package org.smartlink.web.utils.biptoken.request;

import lombok.Data;

/**
 * @description: BIP获取token接口参数请求类
 * @author: L
 * @create:
 **/
@Data
public class BipTokenRequest {

    private String openApiUrl;

    private String appKey;

    private String appSecret;
}
