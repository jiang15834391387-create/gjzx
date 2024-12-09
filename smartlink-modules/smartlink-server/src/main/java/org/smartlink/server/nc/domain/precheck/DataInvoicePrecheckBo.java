package org.smartlink.server.nc.domain.precheck;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 发票预校验业务对象 data_invoice_precheck
 *
 * @author ruoyi
 * @date 2022-11-08
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("发票预校验业务对象")
public class DataInvoicePrecheckBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotNull(message = "主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long id;

    /**
     * 过滤类型 0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验
     */
    @ApiModelProperty(value = "过滤类型 0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验", required = true)
    private String checkType;

    /**
     * 校验类型名称
     */
    @ApiModelProperty(value = "校验类型名称", required = true)
    private String checkName;

    /**
     * 0关1开
     */
    @ApiModelProperty(value = "0关1开", required = true)
    private String checkSwitch;

    /**
     * 值
     */
    @ApiModelProperty(value = "值", required = true)
    private String checkContent1;

    /**
     * 值
     */
    @ApiModelProperty(value = "值", required = true)
    private String checkContent2;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    private String remark;

    /**
     * 多租户标识
     */
    @ApiModelProperty(value = "多租户标识", required = true)
    private String tenantId;


}
