package org.smartlink.web.ncc.nccor.request.taxinvoice;

import lombok.Data;
import lombok.ToString;

/**
 * @description: NCC2111上传电子发票请求类
 * @author: L
 * @create:
 **/
@Data
@ToString(exclude = {"filecontent"})
public class UploadElectronicInvoiceNonStandardRequest {

    //组织PK
    private String pk_org;

    //单据id
    private String billid;

    //单据类型
    private String billtype;

    //交易类型
    private String transitype;

    //文件名称
    private String filename;

    //电子发票源文件（Base64）
    private String filecontent;

}
