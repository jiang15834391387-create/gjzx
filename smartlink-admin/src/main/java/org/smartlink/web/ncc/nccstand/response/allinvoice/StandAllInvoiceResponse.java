package org.smartlink.web.ncc.nccstand.response.allinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: 响应接收类
 * @author: L
 * @create:
 **/
@Data
public class StandAllInvoiceResponse {

    private String success;

    private String code;

    private String message;

    List<StandAllInvoiceResponseData> data;

}
