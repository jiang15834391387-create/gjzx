package org.smartlink.web.ncc.nccor.request.allinvoice;

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
public class UploadAllInvoiceRequestData {

    /**
     * 图片对应Base64
     */
    private String file;



}
