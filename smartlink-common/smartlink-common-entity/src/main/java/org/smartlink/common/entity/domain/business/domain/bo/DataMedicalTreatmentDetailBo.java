package org.smartlink.common.entity.domain.business.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatmentDetail;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 医疗票明细业务对象 data_medical_treatment_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataMedicalTreatmentDetail.class, reverseConvertGenerate = false)
public class DataMedicalTreatmentDetailBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 票据号码
     */
    @NotBlank(message = "票据号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 票据代码
     */
    @NotBlank(message = "票据代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 开票日期
     */
    @NotBlank(message = "开票日期 不能为空", groups = { AddGroup.class, EditGroup.class })
    private String date;

    /**
     * 交款人
     */
    @NotBlank(message = "交款人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payer;

    /**
     * 小计
     */
    @NotBlank(message = "小计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String subtotal;

    /**
     * 总计
     */
    @NotBlank(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String total;

    /**
     * 收款单位
     */
    @NotBlank(message = "收款单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payee;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectName;

    /**
     * 数量/单位
     */
    @NotBlank(message = "数量/单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String quantity;

    /**
     * 金额
     */
    @NotBlank(message = "金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String amount;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String comment;

    /**
     * 坐标
     */
    @NotBlank(message = "坐标不能为空", groups = { AddGroup.class, EditGroup.class })
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
