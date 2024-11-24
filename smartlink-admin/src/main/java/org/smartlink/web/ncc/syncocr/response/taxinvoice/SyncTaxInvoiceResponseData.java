package org.smartlink.web.ncc.syncocr.response.taxinvoice;

import lombok.Data;

/**
 * @description: 返回data
 * @author: L
 * @create:
 **/
@Data
public class SyncTaxInvoiceResponseData {

    private String code;

    private String msg;

    private String fpDm;

    private String fpHm;

    private String kprq;

    private String hjje;

    private String jym;

    private String verifyToken;

}
