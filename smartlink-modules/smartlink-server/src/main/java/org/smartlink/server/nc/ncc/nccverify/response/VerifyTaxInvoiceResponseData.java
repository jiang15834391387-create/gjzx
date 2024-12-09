package org.smartlink.server.nc.ncc.nccverify.response;

import lombok.Data;

/**
 * @description: 响应data
 * @author: L
 * @create:
 **/
@Data
public class VerifyTaxInvoiceResponseData {

    private String code;

    private String msg;

    private String fpDm;

    private String fpHm;

    private String saveToken;

    private VerifyTaxInvoiceResponseDataInvoice invoice;

}
