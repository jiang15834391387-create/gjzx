package org.smartlink.web.ncc.nccor.response.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: 电子发票上传返回类
 * @author: ChenJiangHong
 * @create: 2022-09-08 10:33
 **/
@Data
public class UploadElectronicInvoiceNonStandardResponse {

    private String success;

    private String code;

    private String message;

    private List<UploadElectronicInvoiceNonStandardResponseData> data;

}
