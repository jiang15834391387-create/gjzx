package org.smartlink.workflow.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 费用报销申请对象 test_expense_reimbursement
 *
 * @author Lion Li
 * @date 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("test_expense_reimbursement")
public class TestExpenseReimbursement extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 申请类型(1.费用报销，2.通用报销)
     */
    private String projectType;

    /**
     * 报销原因
     */
    private String reimbursementReason;

    /**
     * 报销人
     */
    private String reimbursementUser;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 收款信息
     */
    private String collectionInformation;

    /**
     * 消费明细
     */
    private String consumptionDetails;

    /**
     * 状态
     */
    private String status;
    /**
     * 是否删除（0否 1是）
     */
    private Integer isDeleted;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
