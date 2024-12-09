package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import lombok.Data;

/**
 * @description: 获取bip税务云地址返回类
 * @author: L
 * @create:
 **/
@Data
public class BipAddressResult {

    private String code;

    private String message;

    private BipAddressResultData data;

}
