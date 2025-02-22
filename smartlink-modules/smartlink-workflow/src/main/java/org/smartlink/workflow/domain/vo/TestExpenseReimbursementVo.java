package org.smartlink.workflow.domain.vo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.TestExpenseReimbursement;

import java.io.Serial;
import java.io.Serializable;


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

    private Long fromId;
    private String fromName;
    private String fromType;
    private Long categoryId;
    private String categoryName;
    private String categoryType;

    /**
     * 消费明细
     */
    @ExcelProperty(value = "消费明细")
    private String consumptionDetails;
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
