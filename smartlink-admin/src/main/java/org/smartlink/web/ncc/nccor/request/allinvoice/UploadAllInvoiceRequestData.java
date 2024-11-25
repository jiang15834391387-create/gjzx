package org.smartlink.web.ncc.nccor.request.allinvoice;

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
public class UploadAllInvoiceRequestData {

    /**
     * 图片对应Base64
     */
    private String file;



}
