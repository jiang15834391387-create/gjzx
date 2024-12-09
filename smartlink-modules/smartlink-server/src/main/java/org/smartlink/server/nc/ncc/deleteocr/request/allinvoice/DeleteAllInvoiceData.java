package org.smartlink.server.nc.ncc.deleteocr.request.allinvoice;

import lombok.Data;

/**
 * @author L
 * @Description 请求类data
 * @create:
 */
@Data
public class DeleteAllInvoiceData {

    private String billType;

    private String saveToken;

    private DeleteAllInvoiceDataItem data;

}
