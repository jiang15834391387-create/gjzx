package org.smartlink.web.ncc.deleteocr.request.taxinvoice;

import lombok.Data;

/**
 * @author chenJiangHong
 * @Description 请求类data
 * @create: 2022-07-29 16:55
 */
@Data
public class DeleteTaxInvoiceData {

    /**
     * 发票代码
     */
    private String fpDm;

    /**
     * 发票号码
     */
    private String fpHm;

    /**
     * 删除接口所需token
     */
    private String saveToken;

}
