package org.smartlink.business.doman.dto;

import lombok.Data;

import java.util.List;

@Data
public class StructureDataDTO {
    /**
     * 银行回单
     */
    private String receiptUrl;
    /**
     * 发票附件/结构化信息
     */
    private List<InvoiceDataDTO> invoiceInfo;
}

