package org.smartlink.web.ncc.deleteocr.request.allinvoice;

import lombok.Builder;
import lombok.Data;

/**
 * @description: 电子发票请求类data
 * @author: ChenJiangHong
 * @create: 2022-09-20 23:47
 **/
@Data
@Builder
public class DeleteElectronicInvoiceData {
    private String fphm;
    private String fpdm;
}
