package org.smartlink.common.core.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.business.domain.DataMedicalTreatment;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 非税收入类票据视图对象 data_medical_treatment
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataMedicalTreatment.class)
public class DataMedicalTreatmentVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 票据号码
     */
    @ExcelProperty(value = "票据号码")
    private String invoiceNumber;

    /**
     * 票据代码
     */
    @ExcelProperty(value = "票据代码")
    private String invoiceCode;

    /**
     * 校验码
     */
    @ExcelProperty(value = "校验码")
    private String checkCode;

    /**
     * 是否为电子医疗发票（1:是）
     */
    @ExcelProperty(value = "是否为电子医疗发票", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=:是")
    private String electronicMark;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 其他信息
     */
    @ExcelProperty(value = "其他信息")
    private String otherInfo;

    /**
     * 收款单位
     */
    @ExcelProperty(value = "收款单位")
    private String payee;

    /**
     * 交款人
     */
    @ExcelProperty(value = "交款人")
    private String payer;

    /**
     * 交款人统一社会信用代码
     */
    @ExcelProperty(value = "交款人统一社会信用代码")
    private String socialCreditCode;

    /**
     * 医院名称
     */
    @ExcelProperty(value = "医院名称")
    private String hospital;

    /**
     * 统筹金额
     */
    @ExcelProperty(value = "统筹金额")
    private String overallAmount;

    /**
     * 病历号
     */
    @ExcelProperty(value = "病历号")
    private String medicalRecordNumber;

    /**
     * 住院号
     */
    @ExcelProperty(value = "住院号")
    private String inpatientNumber;

    /**
     * 门诊号
     */
    @ExcelProperty(value = "门诊号")
    private String outpatientNumber;

    /**
     * 医保编号
     */
    @ExcelProperty(value = "医保编号")
    private String medicalInsuranceNumber;

    /**
     * 就诊日期
     */
    @ExcelProperty(value = "就诊日期")
    private String visitDate;

    /**
     * 医疗机构类别
     */
    @ExcelProperty(value = "医疗机构类别")
    private String medicalInstitutionType;

    /**
     * 医疗机构类型
     */
    @ExcelProperty(value = "医疗机构类型")
    private String medicalInsuranceType;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    private String gende;

    /**
     * 其他支付
     */
    @ExcelProperty(value = "其他支付")
    private String otherPayments;

    /**
     * 个人账户支付
     */
    @ExcelProperty(value = "个人账户支付")
    private String personalAccountPayment;

    /**
     * 现金支付
     */
    @ExcelProperty(value = "现金支付")
    private String cashPayment;

    /**
     * 个人自付
     */
    @ExcelProperty(value = "个人自付")
    private String personalExpense;

    /**
     * 个人支付
     */
    @ExcelProperty(value = "个人支付")
    private String personalPayment;

    /**
     * 住院科别
     */
    @ExcelProperty(value = "住院科别")
    private String inpatientDepartment;

    /**
     * 入院日期
     */
    @ExcelProperty(value = "入院日期")
    private String admissionDate;

    /**
     * 出院日期
     */
    @ExcelProperty(value = "出院日期")
    private String dischargeDate;

    /**
     * 年度医保范围内
     */
    @ExcelProperty(value = "年度医保范围内")
    private String annualHealthInsuranceCoverage;

    /**
     * 年度门诊大额支付
     */
    @ExcelProperty(value = "年度门诊大额支付")
    private String annualOutpatientCatastrophicPayment;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 合计金额(小写)
     */
    @ExcelProperty(value = "合计金额(小写)")
    private String invoiceTotal;

    /**
     * 合计金额(大写)
     */
    @ExcelProperty(value = "合计金额(大写)")
    private String totalWords;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String invoiceDate;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
