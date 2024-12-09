package org.smartlink.server.nc.ncc.nccor.response.allinvoice;

import lombok.Data;

/**
 * @description: 电子发票上传返回类
 * @author: L
 * @create:
 **/
@Data
public class UploadElectronicInvoiceResponse {

    private String success;

    private String code;

    private String message;

    private String data;

}
