package org.smartlink.web.ncc.deleteocr.response;

import lombok.Data;

/**
 * @author chenJiangHong
 * @Description 删除发票信息响应类
 * @create: 2022-07-29 16:51
 */
@Data
public class DeleteInvoiceResponse {

    /**
     * 是否成功标识
     */
    private String success;

    /**
     * 状态码
     */
    private String code;

    /**
     * 返回信息
     */
    private String message;
}
