package org.smartlink.web.ncc.nccstand.response.taxinvoice;

import lombok.Data;

/**
 * @description: 响应data
 * @author: L
 * @create:
 **/
@Data
public class StandTaxInvoiceResponseData {

    private String code;

    private String msg;

    private String fpDm;

    private String fpHm;

    private String billId;

    private String saveToken;

}
