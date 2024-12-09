package org.smartlink.server.nc.ncc.syncocr.response.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: 同步OCR接口返回类
 * @author: L
 * @create:
 **/
@Data
public class SyncTaxInvoiceResponse {

    private String success;

    private String code;

    private String message;

    private List<SyncTaxInvoiceResponseData> data;

}
