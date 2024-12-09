package org.smartlink.server.nc.ncc.nccverify.response;

import lombok.Data;

import java.util.List;

/**
 * @description: NCC查验接口响应实体类
 * @author: L
 * @create:
 **/
@Data
public class VerifyTaxInvoiceResponse {

    private String success;

    private String code;

    private String message;

    private List<VerifyTaxInvoiceResponseData> data;

}
