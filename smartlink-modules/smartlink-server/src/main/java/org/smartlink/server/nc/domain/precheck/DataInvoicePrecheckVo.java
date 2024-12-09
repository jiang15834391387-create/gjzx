package org.smartlink.server.nc.domain.precheck;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * 发票预校验视图对象 data_invoice_precheck
 *
 * @author L
 * @date
 */
@Data
@ApiModel("发票预校验视图对象")
@ExcelIgnoreUnannotated
public class DataInvoicePrecheckVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private Long id;

    /**
     * 过滤类型 0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验
     */
    @ExcelProperty(value = "过滤类型 0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验")
    @ApiModelProperty("过滤类型 0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验")
    private String checkType;

    /**
     * 校验类型名称
     */
    @ExcelProperty(value = "校验类型名称")
    @ApiModelProperty("校验类型名称")
    private String checkName;

    /**
     * 0关1开
     */
    @ExcelProperty(value = "0关1开")
    @ApiModelProperty("0关1开")
    private String checkSwitch;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    @ApiModelProperty("值")
    private String checkContent1;

    /**
     * 值
     */
    @ExcelProperty(value = "值")
    @ApiModelProperty("值")
    private String checkContent2;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 多租户标识
     */
    @ExcelProperty(value = "多租户标识")
    @ApiModelProperty("多租户标识")
    private String tenantId;


}
