package org.smartlink.web.ncc.deleteocr.request.allinvoice;

import lombok.Data;

/**
 * @author chenJiangHong
 * @Description 请求类data
 * @create: 2022-07-29 16:55
 */
@Data
public class DeleteAllInvoiceData {

    private String billType;

    private String saveToken;

    private DeleteAllInvoiceDataItem data;

}
