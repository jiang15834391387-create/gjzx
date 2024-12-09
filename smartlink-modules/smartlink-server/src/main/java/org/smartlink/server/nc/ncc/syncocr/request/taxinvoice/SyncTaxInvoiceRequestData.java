package org.smartlink.server.nc.ncc.syncocr.request.taxinvoice;

import lombok.Data;

/**
 * @description: 请求data
 * @author: L
 * @create:
 **/
@Data
public class SyncTaxInvoiceRequestData {

    /**
     * 发票代码
     */
    private String fpDm;

    /**
     * 发票号码
     */
    private String fpHm;

    /**
     * 开票日期
     */
    private String kprq;

    /**
     * 合计金额
     */
    private String hjje;

    /**
     * 校验码
     */
    private String jym;

    /**
     * 流水号
     */
    private String billid;
}
