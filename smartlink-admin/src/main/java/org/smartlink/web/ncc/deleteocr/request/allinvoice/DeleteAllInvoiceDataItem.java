package org.smartlink.web.ncc.deleteocr.request.allinvoice;

import lombok.Builder;
import lombok.Data;

/**
 * @description: 删除请求类明细
 * @author: ChenJiangHong
 * @create: 2022-09-01 00:44
 **/
@Data
@Builder
public class DeleteAllInvoiceDataItem {

    private String fpDm;

    private String fpHm;

}
