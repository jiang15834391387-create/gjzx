package org.smartlink.common.check.doman.dto;

import lombok.Data;

import java.io.Serializable;

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
    private String id ;
    /**
     * 发票代码
     */
    private String code ;
    /**
     * 发票号码
     */
    private String number ;
    /**
     * 发票日期
     */
    private String date;
    /**
     * 发票类型 100010
     **/
    private String type ;
    /**
     * 电子票标识(增值税专用发票) (默认 0, 1 是)
     */
    private String electron_mark;
    /**
     * (增值税专用发票)  机动车(税前金额)   二手车(总价金额 )
     */
    private String  pretax_amount ;
    /**
     * 校验码后六位
     */
    private String check_code ;
    /**
     * 增值税电子普通发票(区块链) 区块链标识 1是
     */
    private Integer block_chain;
    /**
     * 增值税电子普通发票(区块链) 销售方纳税人识别号
     */
    private String seller_tax_id;

    /**
     * 增值税电子普通发票(区块链) 地区
     */
    private String area ;

    /**
     * 数电票 (金额 价税合计)
     */
    private String total ;


    /**
     * 数电票(火车票) 发票日期
     */
    private String date_of_issue;

    /**
     * 数电票(航空运输电子客票行程单) 发票号码
     */
    private String receipt_numbe;

}
