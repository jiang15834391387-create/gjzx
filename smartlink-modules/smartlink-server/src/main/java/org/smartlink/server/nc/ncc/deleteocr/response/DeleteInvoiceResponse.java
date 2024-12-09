package org.smartlink.server.nc.ncc.deleteocr.response;

import lombok.Data;

/**
 * @author L
 * @Description 删除发票信息响应类
 * @create:
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
