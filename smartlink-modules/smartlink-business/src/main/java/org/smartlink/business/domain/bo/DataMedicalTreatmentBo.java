package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataMedicalTreatment;
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
 * 非税收入类票据业务对象 data_medical_treatment
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataMedicalTreatment.class, reverseConvertGenerate = false)
public class DataMedicalTreatmentBo extends BaseEntity {

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
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

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
     * 校验码
     */
    @NotBlank(message = "校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkCode;

    /**
     * 是否为电子医疗发票（1:是）
     */
    @NotBlank(message = "是否为电子医疗发票（1:是）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 其他信息
     */
    @NotBlank(message = "其他信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String otherInfo;

    /**
     * 收款单位
     */
    @NotBlank(message = "收款单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payee;

    /**
     * 交款人
     */
    @NotBlank(message = "交款人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payer;

    /**
     * 交款人统一社会信用代码
     */
    @NotBlank(message = "交款人统一社会信用代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String socialCreditCode;

    /**
     * 医院名称
     */
    @NotBlank(message = "医院名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String hospital;

    /**
     * 统筹金额
     */
    @NotNull(message = "统筹金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long overallAmount;

    /**
     * 病历号
     */
    @NotBlank(message = "病历号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String medicalRecordNumber;

    /**
     * 住院号
     */
    @NotBlank(message = "住院号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inpatientNumber;

    /**
     * 门诊号
     */
    @NotBlank(message = "门诊号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String outpatientNumber;

    /**
     * 医保编号
     */
    @NotBlank(message = "医保编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String medicalInsuranceNumber;

    /**
     * 就诊日期
     */
    @NotBlank(message = "就诊日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String visitDate;

    /**
     * 医疗机构类别
     */
    @NotBlank(message = "医疗机构类别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String medicalInstitutionType;

    /**
     * 医疗机构类型
     */
    @NotBlank(message = "医疗机构类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String medicalInsuranceType;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gende;

    /**
     * 其他支付
     */
    @NotBlank(message = "其他支付不能为空", groups = { AddGroup.class, EditGroup.class })
    private String otherPayments;

    /**
     * 个人账户支付
     */
    @NotBlank(message = "个人账户支付不能为空", groups = { AddGroup.class, EditGroup.class })
    private String personalAccountPayment;

    /**
     * 现金支付
     */
    @NotBlank(message = "现金支付不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cashPayment;

    /**
     * 个人自付
     */
    @NotBlank(message = "个人自付不能为空", groups = { AddGroup.class, EditGroup.class })
    private String personalExpense;

    /**
     * 个人支付
     */
    @NotBlank(message = "个人支付不能为空", groups = { AddGroup.class, EditGroup.class })
    private String personalPayment;

    /**
     * 住院科别
     */
    @NotBlank(message = "住院科别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String inpatientDepartment;

    /**
     * 入院日期
     */
    @NotBlank(message = "入院日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String admissionDate;

    /**
     * 出院日期
     */
    @NotBlank(message = "出院日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dischargeDate;

    /**
     * 年度医保范围内
     */
    @NotBlank(message = "年度医保范围内不能为空", groups = { AddGroup.class, EditGroup.class })
    private String annualHealthInsuranceCoverage;

    /**
     * 年度门诊大额支付
     */
    @NotBlank(message = "年度门诊大额支付不能为空", groups = { AddGroup.class, EditGroup.class })
    private String annualOutpatientCatastrophicPayment;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 合计金额(小写)
     */
    @NotNull(message = "合计金额(小写)不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long invoiceTotal;

    /**
     * 合计金额(大写)
     */
    @NotBlank(message = "合计金额(大写)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalWords;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date invoiceDate;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

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
