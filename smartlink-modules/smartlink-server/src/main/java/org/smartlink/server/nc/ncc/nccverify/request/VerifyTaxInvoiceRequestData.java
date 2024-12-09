package org.smartlink.server.nc.ncc.nccverify.request;

import lombok.Data;

/**
 * @description: 校验发票信息请求data
 * @author: L
 * @create:
 **/
@Data
public class VerifyTaxInvoiceRequestData {

    private String fpDm;

    private String fpHm;

    private String kprq;

    private String hjje;

    private String jshj;

    private String jym;

    private String verifyToken;

    private String billid;

}
