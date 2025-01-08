package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 医疗票明细对象 data_medical_treatment_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_medical_treatment_detail")
public class DataMedicalTreatmentDetail extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 票据号码
     */
    private String invoiceNumber;

    /**
     * 票据代码
     */
    private String invoiceCode;

    /**
     * 开票日期 
     */
    private String date;

    /**
     * 交款人
     */
    private String payer;

    /**
     * 小计
     */
    private String subtotal;

    /**
     * 总计
     */
    private String total;

    /**
     * 收款单位
     */
    private String payee;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 数量/单位
     */
    private String quantity;

    /**
     * 金额
     */
    private String amount;

    /**
     * 备注
     */
    private String comment;

    /**
     * 坐标
     */
    private String region;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
