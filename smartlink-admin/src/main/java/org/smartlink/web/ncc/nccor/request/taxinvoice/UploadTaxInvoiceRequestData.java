package org.smartlink.web.ncc.nccor.request.taxinvoice;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @description: ocr接口请求data对象
 * @author: chenJiangHong
 * @create: 2022-06-13 00:55
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
