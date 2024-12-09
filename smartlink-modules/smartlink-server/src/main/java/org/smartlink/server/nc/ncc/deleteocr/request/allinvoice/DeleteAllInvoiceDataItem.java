package org.smartlink.server.nc.ncc.deleteocr.request.allinvoice;

import lombok.Builder;
import lombok.Data;

/**
 * @description: 删除请求类明细
 * @author: L
 * @create:
 **/
@Data
@Builder
public class DeleteAllInvoiceDataItem {

    private String fpDm;

    private String fpHm;

}
