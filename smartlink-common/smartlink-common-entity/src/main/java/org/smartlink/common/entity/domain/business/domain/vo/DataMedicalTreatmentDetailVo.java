package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatmentDetail;

import java.io.Serial;
import java.io.Serializable;


/**
 * 医疗票明细视图对象 data_medical_treatment_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataMedicalTreatmentDetail.class)
public class DataMedicalTreatmentDetailVo implements Serializable {

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
     * 开票日期
     */
    @ExcelProperty(value = "开票日期 ")
    private String date;

    /**
     * 交款人
     */
    @ExcelProperty(value = "交款人")
    private String payer;

    /**
     * 小计
     */
    @ExcelProperty(value = "小计")
    private String subtotal;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String total;

    /**
     * 收款单位
     */
    @ExcelProperty(value = "收款单位")
    private String payee;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 数量/单位
     */
    @ExcelProperty(value = "数量/单位")
    private String quantity;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private String amount;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String comment;

    /**
     * 坐标
     */
    @ExcelProperty(value = "坐标")
    private String region;

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
