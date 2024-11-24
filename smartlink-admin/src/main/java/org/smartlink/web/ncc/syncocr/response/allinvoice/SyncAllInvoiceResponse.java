package org.smartlink.web.ncc.syncocr.response.allinvoice;

import lombok.Data;

/**
 * @description: 同步OCR接口返回类
 * @author: L
 * @create:
 **/
@Data
public class SyncAllInvoiceResponse {

    private String success;

    private String code;

    private String message;

    private SyncAllInvoiceResponseData data;

}
