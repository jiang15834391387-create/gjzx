package org.smartlink.server.nc.ncc.nccor.response.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: 电子发票上传返回类
 * @author: L
 * @create:
 **/
@Data
public class UploadElectronicInvoiceNonStandardResponse {

    private String success;

    private String code;

    private String message;

    private List<UploadElectronicInvoiceNonStandardResponseData> data;

}
