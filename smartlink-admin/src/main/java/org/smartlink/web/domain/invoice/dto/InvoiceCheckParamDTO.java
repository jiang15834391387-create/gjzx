package org.smartlink.web.domain.invoice.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查验参数DTO
 *
 * @author L
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
     * 税后总金额（开灵厂商需要）
     */
    private String totalAmount = "";
    /**
     * 提交人
     */
    private String subMitUser = "";
    /**
     * 业务系统单据号
     */
    private String billNum;
    /**
     * 发票类型
     */
    private String billType;
    /**
     * 文件路径
     */
    private String filePath;
    /**
     * 查验Token
     */
    private String saveToken;
    /**
     * 火车票乘车人
     */
    private String name;
    /**
     * 客车下车站
     */
    private String exit;
    /**
     * 数电票号码
     */
    private String electronicNumber;
    /**
     * 电子客票号码
     */
    private String ticketNum;

}
