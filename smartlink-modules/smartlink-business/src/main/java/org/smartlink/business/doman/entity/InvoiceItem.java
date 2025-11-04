package org.smartlink.business.doman.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceItem {
    private int index;                // 序号
    private String name;              // 货物或应税劳务名称
    private String specification;     // 规格型号
    private String unit;              // 单位
    private int quantity;             // 数量
    private BigDecimal unitPrice;     // 单价
    private BigDecimal amount;        // 金额
    private String taxRate;           // 税率
    private BigDecimal taxAmount;     // 税额

    public InvoiceItem() {}

    public InvoiceItem(int index, String name, String specification, String unit,
                      int quantity, BigDecimal unitPrice, BigDecimal amount,
                      String taxRate, BigDecimal taxAmount) {
        this.index = index;
        this.name = name;
        this.specification = specification;
        this.unit = unit;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
    }


    public InvoiceItem(int index, String name, BigDecimal unitPrice, BigDecimal amount,
                       String taxRate, BigDecimal taxAmount) {
        this.index = index;
        this.name = name;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
    }
}
