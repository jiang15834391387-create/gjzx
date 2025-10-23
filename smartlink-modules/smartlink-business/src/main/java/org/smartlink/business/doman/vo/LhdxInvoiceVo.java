package org.smartlink.business.doman.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 联合大学发票导入表
 */
@Data
@ExcelIgnoreUnannotated
public class LhdxInvoiceVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = "序号", index = 0)
    private String serialNumber;

    @ExcelProperty(value = "开票日期", index = 1)
    private String invoiceDate;

    @ExcelProperty(value = "发票类型", index = 2)
    private String invoiceType;

    @ExcelProperty(value = "发票号码", index = 3)
    private String invoiceNumber;

    @ExcelProperty(value = "发票代码", index = 4)
    private String invoiceCode;

    @ExcelProperty(value = "购买方名称", index = 5)
    private String buyerName;

    @ExcelProperty(value = "购买方纳税识别号", index = 6)
    private String buyerTaxId;

    @ExcelProperty(value = "销售方名称", index = 7)
    private String sellerName;

    @ExcelProperty(value = "销售方纳税识别号", index = 8)
    private String sellerTaxId;

    @ExcelProperty(value = "发票明细行", index = 9)
    private String invoiceDetailLine;

    @ExcelProperty(value = "发票明细项目名称", index = 10)
    private String invoiceItemName;

    @ExcelProperty(value = "发票明细行单价金额", index = 11)
    private String unitPrice;

    @ExcelProperty(value = "税率", index = 12)
    private String taxRate;

    @ExcelProperty(value = "税额", index = 13)
    private String taxAmount;

    @ExcelProperty(value = "价税小计", index = 14)
    private String totalAmount;

    @ExcelProperty(value = "收款人", index = 15)
    private String payee;

    @ExcelProperty(value = "开票人", index = 16)
    private String drawer;

    @ExcelProperty(value = "备注", index = 17)
    private String remarks;

    private String invoiceNumberCode;

}
