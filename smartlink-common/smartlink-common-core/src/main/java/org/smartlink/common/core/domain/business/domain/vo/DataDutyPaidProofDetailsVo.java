package org.smartlink.common.core.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.business.domain.DataDutyPaidProofDetails;

import java.io.Serial;
import java.io.Serializable;


/**
 * 完税证明明细视图对象 data_duty_paid_proof_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataDutyPaidProofDetails.class)
public class DataDutyPaidProofDetailsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    private String fileId;

    /**
     * 入库日期
     */
    @ExcelProperty(value = "入库日期")
    private String entryDate;

    /**
     * 征收机关
     */
    @ExcelProperty(value = "征收机关")
    private String taxAgency;

    /**
     * 税款所属期
     */
    @ExcelProperty(value = "税款所属期")
    private String taxPeriod;

    /**
     * 税种
     */
    @ExcelProperty(value = "税种")
    private String taxType;

    /**
     * 预算科目编码
     */
    @ExcelProperty(value = "预算科目编码")
    private String budgetAccountCode;

    /**
     * 预算科目名称
     */
    @ExcelProperty(value = "预算科目名称")
    private String budgetAccountName;

    /**
     * 预算科目级次
     */
    @ExcelProperty(value = "预算科目级次")
    private String budgetAccountLevel;

    /**
     * 品目名称
     */
    @ExcelProperty(value = "品目名称")
    private String name;

    /**
     * 原凭证号
     */
    @ExcelProperty(value = "原凭证号")
    private String originalNumber;

    /**
     * 课税数量
     */
    @ExcelProperty(value = "课税数量")
    private String quantity;

    /**
     * 税率或单位税额
     */
    @ExcelProperty(value = "税率或单位税额")
    private String taxRate;

    /**
     * 计税金额或销售收入
     */
    @ExcelProperty(value = "计税金额或销售收入")
    private String total;

    /**
     * 已缴金额
     */
    @ExcelProperty(value = "已缴金额")
    private String amountPaid;

    /**
     * 实缴金额
     */
    @ExcelProperty(value = "实缴金额")
    private String actualPaidAmount;

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
