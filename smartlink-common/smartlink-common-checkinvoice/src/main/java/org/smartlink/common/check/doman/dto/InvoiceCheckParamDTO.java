package org.smartlink.common.check.doman.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 查验参数DTO
 *
 */
@Data
public class InvoiceCheckParamDTO implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 发票代码
     */
    private String invoiceCode = "";
    /**
     * 发票号码
     */
    private String invoiceNumber = "";
    /**
     * 发票日期
     */
    private Date invoiceDate;
    /**
     * 校验码
     */
    private String checkCode = "";
    /**
     * 税前总金额
     */
    private String total = "";
    /**
     * 价税合计
     */
    private String totalLowercase;
    /**
     * 税后总金额
     */
    private String totalAmount = "";
    /**
     * 提交人
     */
    private String subMitUser = "";


}
