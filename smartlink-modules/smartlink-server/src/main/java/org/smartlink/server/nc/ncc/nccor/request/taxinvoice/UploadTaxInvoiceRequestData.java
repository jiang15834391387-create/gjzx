package org.smartlink.server.nc.ncc.nccor.request.taxinvoice;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @description: ocr接口请求data对象
 * @author: L
 * @create:
 **/
@Data
@Builder
@ToString(exclude = {"file"})
public class UploadTaxInvoiceRequestData {

    /**
     * 图片对应Base64
     */
    private String file;

    /**
     * 流水号字段
     */
    private String billid;

}
