package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataDutyPaidProofDetails;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 完税证明明细业务对象 data_duty_paid_proof_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataDutyPaidProofDetails.class, reverseConvertGenerate = false)
public class DataDutyPaidProofDetailsBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 图片表主键
     */
    @NotBlank(message = "图片表主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 入库日期
     */
    @NotBlank(message = "入库日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String entryDate;

    /**
     * 征收机关
     */
    @NotBlank(message = "征收机关不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxAgency;

    /**
     * 税款所属期
     */
    @NotBlank(message = "税款所属期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxPeriod;

    /**
     * 税种
     */
    @NotBlank(message = "税种不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxType;

    /**
     * 预算科目编码
     */
    @NotBlank(message = "预算科目编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String budgetAccountCode;

    /**
     * 预算科目名称
     */
    @NotBlank(message = "预算科目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String budgetAccountName;

    /**
     * 预算科目级次
     */
    @NotBlank(message = "预算科目级次不能为空", groups = { AddGroup.class, EditGroup.class })
    private String budgetAccountLevel;

    /**
     * 品目名称
     */
    @NotBlank(message = "品目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 原凭证号
     */
    @NotBlank(message = "原凭证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String originalNumber;

    /**
     * 课税数量
     */
    @NotBlank(message = "课税数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private String quantity;

    /**
     * 税率或单位税额
     */
    @NotBlank(message = "税率或单位税额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxRate;

    /**
     * 计税金额或销售收入
     */
    @NotBlank(message = "计税金额或销售收入不能为空", groups = { AddGroup.class, EditGroup.class })
    private String total;

    /**
     * 已缴金额
     */
    @NotBlank(message = "已缴金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String amountPaid;

    /**
     * 实缴金额
     */
    @NotBlank(message = "实缴金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String actualPaidAmount;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
