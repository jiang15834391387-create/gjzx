package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 完税证明明细对象 data_duty_paid_proof_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_duty_paid_proof_details")
public class DataDutyPaidProofDetails extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表主键
     */
    private String fileId;

    /**
     * 入库日期
     */
    private String entryDate;

    /**
     * 征收机关
     */
    private String taxAgency;

    /**
     * 税款所属期
     */
    private String taxPeriod;

    /**
     * 税种
     */
    private String taxType;

    /**
     * 预算科目编码
     */
    private String budgetAccountCode;

    /**
     * 预算科目名称
     */
    private String budgetAccountName;

    /**
     * 预算科目级次
     */
    private String budgetAccountLevel;

    /**
     * 品目名称
     */
    private String name;

    /**
     * 原凭证号
     */
    private String originalNumber;

    /**
     * 课税数量
     */
    private String quantity;

    /**
     * 税率或单位税额
     */
    private String taxRate;

    /**
     * 计税金额或销售收入
     */
    private String total;

    /**
     * 已缴金额
     */
    private String amountPaid;

    /**
     * 实缴金额
     */
    private String actualPaidAmount;

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
