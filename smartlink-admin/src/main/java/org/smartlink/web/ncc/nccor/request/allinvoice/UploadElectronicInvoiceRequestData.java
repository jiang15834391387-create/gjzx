package org.smartlink.web.ncc.nccor.request.allinvoice;

import lombok.Data;

/**
 * @description: NCC2207上传电子发票请求类data
 * @author: ChenJiangHong
 * @create: 2022-09-08 09:56
 **/
@Data
public class UploadElectronicInvoiceRequestData {

    /**
     * 发票名称
     */
    private String fileName;

    /**
     * 发票base64
     */
    private String fileContent;

}
