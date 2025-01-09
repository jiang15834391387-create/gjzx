package org.smartlink.common.entity.domain.business.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataDutyPaidProof;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 完税证明业务对象 data_duty_paid_proof
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataDutyPaidProof.class, reverseConvertGenerate = false)
public class DataDutyPaidProofBo extends BaseEntity {

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
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 票证字轨
     */
    @NotBlank(message = "票证字轨不能为空", groups = { AddGroup.class, EditGroup.class })
    private String serialNumber;

    /**
     * 购买方名称
     */
    @NotBlank(message = "购买方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerName;

    /**
     * 缴款人识别号
     */
    @NotBlank(message = "缴款人识别号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerTaxId;

    /**
     * 日期
     */
    @NotBlank(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 总计
     */
    @NotBlank(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

    /**
     * 大写金额
     */
    @NotBlank(message = "大写金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalUppercase;

    /**
     * 主管税务机关
     */
    @NotBlank(message = "主管税务机关不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxAuthority;

    /**
     * 征收机关代码
     */
    @NotBlank(message = "征收机关代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxAgencyCode;

    /**
     * 纳税人开户行
     */
    @NotBlank(message = "纳税人开户行不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerDepositBank;

    /**
     * 纳税人账号
     */
    @NotBlank(message = "纳税人账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerAccount;

    /**
     * 税款限缴期限
     */
    @NotBlank(message = "税款限缴期限不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxPaymentLimitedTime;

    /**
     * 收款国库
     */
    @NotBlank(message = "收款国库不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receivingTreasury;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

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
