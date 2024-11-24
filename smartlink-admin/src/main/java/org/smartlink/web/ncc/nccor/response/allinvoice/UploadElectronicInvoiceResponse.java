package org.smartlink.web.ncc.nccor.response.allinvoice;

import lombok.Data;

/**
 * @description: 电子发票上传返回类
 * @author: ChenJiangHong
 * @create: 2022-09-08 10:33
 **/
@Data
public class UploadElectronicInvoiceResponse {

    private String success;

    private String code;

    private String message;

    private String data;

}
