package org.smartlink.web.ncc.nccstand.response.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: 响应接收类
 * @author: L
 * @create:
 **/
@Data
public class StandTaxInvoiceResponse {

    private String success;

    private String code;

    private String message;

    List<StandTaxInvoiceResponseData> data;

}
