package org.smartlink.common.check.doman.dto;

import lombok.Data;

/**
 * 睿真请求参数DTO
 *
 * @author maxuhui
 */
@Data
public class RuiZhenCheckParamDTO {

    /**
     * 购方税号
     */
    private String taxNo;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNumber;
    /**
     * 开票日期
     */
    private String billingDate;
    /**
     * 校验码  校验码后6位或全部（普票、电子普票、卷式普票、通行费必填）
     */
    private String checkCode_6;
    /**
     * 合计金额（不含税）
     */
    private String totalAmount;

}
