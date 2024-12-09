package org.smartlink.server.nc.domain.precheck;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 发票预校验对象 data_invoice_precheck
 *
 * @author ruoyi
 * @date 2022-11-08
 */
@Data
@TableName("data_invoice_precheck")
public class DataInvoicePrecheck extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    @FieldName(value = "主键")
    private Long id;
    /**
     * 过滤类型 0节假日1发票连号2敏感词发票3特殊商品发票4抬头校验5发票重复6打印一致性校验
     */
    private String checkType;
    /**
     * 校验类型名称
     */
    private String checkName;
    /**
     * 0关1开
     */
    private String checkSwitch;
    /**
     * 值
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String checkContent1;
    /**
     * 值
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String checkContent2;
    /**
     * 备注
     */
    private String remark;
    /**
     * 多租户标识
     */
    private String tenantId;


}
