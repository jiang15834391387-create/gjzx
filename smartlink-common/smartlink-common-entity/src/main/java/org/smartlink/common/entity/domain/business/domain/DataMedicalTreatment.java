package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;
import java.util.List;

/**
 * 非税收入类票据对象 data_medical_treatment
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_medical_treatment")
public class DataMedicalTreatment extends TenantEntity {

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
     * 标题
     */
    private String title;

    /**
     * 票据号码
     */
    private String invoiceNumber;

    /**
     * 票据代码
     */
    private String invoiceCode;

    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 是否为电子医疗发票（1:是）
     */
    private String electronicMark;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 其他信息
     */
    private String otherInfo;

    /**
     * 收款单位
     */
    private String payee;

    /**
     * 交款人
     */
    private String payer;

    /**
     * 交款人统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 医院名称
     */
    private String hospital;

    /**
     * 统筹金额
     */
    private String overallAmount;

    /**
     * 病历号
     */
    private String medicalRecordNumber;

    /**
     * 住院号
     */
    private String inpatientNumber;

    /**
     * 门诊号
     */
    private String outpatientNumber;

    /**
     * 医保编号
     */
    private String medicalInsuranceNumber;

    /**
     * 就诊日期
     */
    private String visitDate;

    /**
     * 医疗机构类别
     */
    private String medicalInstitutionType;

    /**
     * 医疗机构类型
     */
    private String medicalInsuranceType;

    /**
     * 性别
     */
    private String gende;

    /**
     * 其他支付
     */
    private String otherPayments;

    /**
     * 个人账户支付
     */
    private String personalAccountPayment;

    /**
     * 现金支付
     */
    private String cashPayment;

    /**
     * 个人自付
     */
    private String personalExpense;

    /**
     * 个人支付
     */
    private String personalPayment;

    /**
     * 住院科别
     */
    private String inpatientDepartment;

    /**
     * 入院日期
     */
    private String admissionDate;

    /**
     * 出院日期
     */
    private String dischargeDate;

    /**
     * 年度医保范围内
     */
    private String annualHealthInsuranceCoverage;

    /**
     * 年度门诊大额支付
     */
    private String annualOutpatientCatastrophicPayment;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 合计金额(小写)
     */
    private String invoiceTotal;

    /**
     * 合计金额(大写)
     */
    private String totalWords;

    /**
     * 日期
     */
    private String invoiceDate;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;


    //非税票据详情对象
    @TableField(exist = false)
    private List<DataMedicalTreatmentDetail> medicalTreatmentDetails;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
