package org.smartlink.workflow.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.TestFormConfig;
import org.smartlink.workflow.domain.TestFormManage;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 费用报销申请视图对象 test_expense_reimbursement
 *
 * @author Lion Li
 * @date 2025-01-07
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestExpenseReimbursement.class)
public class TestExpenseReimbursementVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectName;

    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectNameValue;

    /**
     * 报销类型
     */
    @ExcelProperty(value = "报销类型")
    private String projectType;

    /**
     * 报销原因
     */
    @ExcelProperty(value = "报销原因")
    private String reimbursementReason;

    /**
     * 报销人
     */
    @ExcelProperty(value = "报销人")
    private String reimbursementUser;

    /**
     * 提交时间
     */
    @ExcelProperty(value = "提交时间")
    private Date submitTime;

    /**
     * 收款信息
     */
    @ExcelProperty(value = "收款信息")
    private String collectionInformation;

    /**
     * 消费明细
     */
    @ExcelProperty(value = "消费明细")
    private String consumptionDetails;
    /**
     * 配置
     */
    @ExcelProperty(value = "配置")
    private String detailsValue;
    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
}
