package org.smartlink.common.check.doman;

import lombok.Data;

import java.util.Map;

/**
 * 发票修改请求对象
 */
@Data
public class InvoiceRequest {
    /**
     * 发票类型（如 10100: 增值税发票，10503: 火车票）
     */
    private String invoiceType;
    /**
     * 发票通用信息
     */
    private Map<String, Object> generalInfo;

    // 发票明细对象
    private BillRequest billRequest;
}
