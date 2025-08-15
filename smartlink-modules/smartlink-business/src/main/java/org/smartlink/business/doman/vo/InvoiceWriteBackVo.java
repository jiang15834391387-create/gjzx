package org.smartlink.business.doman.vo;

import lombok.Data;

@Data
public class InvoiceWriteBackVo {
    private String amount;//金额
    private String details;//详情
    private String invoiceId;//发票id
    private String url;

    public InvoiceWriteBackVo setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public InvoiceWriteBackVo setAmount(String amount) {
        this.amount = amount;
        return this;
    }

    public InvoiceWriteBackVo setDetails(String details) {
        this.details = details;
        return this;
    }
    public InvoiceWriteBackVo setUrL(String url) {
        this.url = url;
        return this;
    }
}
