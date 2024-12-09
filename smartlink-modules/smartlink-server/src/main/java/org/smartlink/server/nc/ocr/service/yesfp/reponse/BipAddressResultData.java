package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import lombok.Data;

/**
 * @description: 获取bip税务云地址返回类
 * @author: L
 * @create:
 **/
@Data
public class BipAddressResultData {

    /**
     * 网关地址
     */
    private String gatewayUrl;

    /**
     * 获取token地址
     */
    private String tokenUrl;

}
