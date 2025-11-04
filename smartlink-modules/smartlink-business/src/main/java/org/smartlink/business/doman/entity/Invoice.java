package org.smartlink.business.doman.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class Invoice {
    private String invoiceCode;        // 发票代码
    private String invoiceNumber;      // 发票号码
    private Date issueDate;           // 开票日期
    private String checkCode;         // 校验码

    private BuyerInfo buyer;          // 购买方信息
    private SellerInfo seller;        // 销售方信息

    private List<InvoiceItem> items;  // 商品明细

    private BigDecimal totalAmount;   // 合计金额
    private BigDecimal totalTax;      // 合计税额
    private BigDecimal totalAmountWithTax; // 价税合计
    private String totalAmountWithoutTaxBig; // 价税合计大写

    private String remark;            // 备注
    private String drawer;            // 开票人
    private String payee;             // 收款人
    private String reviewer;          // 复核人

    // 构造函数
    public Invoice() {
    }

    public Invoice(String invoiceCode, String invoiceNumber, Date issueDate, String checkCode,
                   BuyerInfo buyer, SellerInfo seller, List<InvoiceItem> items,
                   BigDecimal totalAmount, BigDecimal totalTax, BigDecimal totalAmountWithTax,String totalAmountWithoutTaxBig,
                   String remark, String drawer, String payee, String reviewer) {
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.checkCode = checkCode;
        this.buyer = buyer;
        this.seller = seller;
        this.items = items;
        this.totalAmount = totalAmount;
        this.totalTax = totalTax;
        this.totalAmountWithTax = totalAmountWithTax;
        this.totalAmountWithoutTaxBig = totalAmountWithoutTaxBig;
        this.remark = remark;
        this.drawer = drawer;
        this.payee = payee;
        this.reviewer = reviewer;
    }

    public Invoice(String invoiceCode, String invoiceNumber, Date issueDate,
                   BuyerInfo buyer, SellerInfo seller, List<InvoiceItem> items,
                   BigDecimal totalAmountWithTax,String totalAmountWithoutTaxBig,
                   String remark, String drawer, String payee) {
        this.invoiceCode = invoiceCode;
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.buyer = buyer;
        this.seller = seller;
        this.items = items;
        this.totalAmountWithTax = totalAmountWithTax;
        this.totalAmountWithoutTaxBig = totalAmountWithoutTaxBig;
        this.remark = remark;
        this.drawer = drawer;
        this.payee = payee;
    }
}

