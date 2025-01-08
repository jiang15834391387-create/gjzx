package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataMedicalTreatmentDetail;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

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
    @NotNull(message = "开票日期 不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date date;

    /**
     * 交款人
     */
    @NotBlank(message = "交款人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payer;

    /**
     * 小计
     */
    @NotNull(message = "小计不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long subtotal;

    /**
     * 总计
     */
    @NotNull(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long total;

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
    @NotNull(message = "金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long amount;

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
     * token
     */
    @NotBlank(message = "token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String saveToken;

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

    /**
     * 查验结果
     */
    @NotBlank(message = "查验结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkResult;

    /**
     * 入台账标识
     */
    @NotBlank(message = "入台账标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pushBusinessInfoFlag;


}
