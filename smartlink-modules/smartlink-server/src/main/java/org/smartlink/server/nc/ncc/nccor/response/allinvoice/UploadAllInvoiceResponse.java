package org.smartlink.server.nc.ncc.nccor.response.allinvoice;

import lombok.Data;

/**
 * @description: NCC获取增值税发票结果返回类
 * @author: L
 * @create:
 **/
@Data
public class UploadAllInvoiceResponse {

    /**
     * 是否成功
     */
    private String success;

    /**
     * 状态码
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * data
     */
    private UploadAllInvoiceResponseData data;

}
