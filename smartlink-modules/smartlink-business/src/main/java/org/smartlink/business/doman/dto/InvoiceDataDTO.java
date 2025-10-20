package org.smartlink.business.doman.dto;

import lombok.Data;

@Data
public class InvoiceDataDTO {
    /**
     * 发票附件
     */
    private String invoiceImg;

    /**
     * 发票基本信息
     */
    private Object info;

    public InvoiceDataDTO(String invoiceImg, Object info) {
        this.invoiceImg = invoiceImg;
        this.info = info;
    }
}
