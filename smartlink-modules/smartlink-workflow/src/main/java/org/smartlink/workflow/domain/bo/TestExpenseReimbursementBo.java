package org.smartlink.workflow.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import org.smartlink.workflow.domain.TestExpenseReimbursement;

/**
 * 费用报销申请业务对象 test_expense_reimbursement
 *
 * @author Lion Li
 * @date 2025-01-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = TestExpenseReimbursement.class, reverseConvertGenerate = false)
public class TestExpenseReimbursementBo extends BaseEntity {

    /**
     * 主键
     */
    //@NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectName;
    /**
     * 项目名称
     */
    @ExcelProperty(value = "项目名称")
    private String projectNameValue;
    /**
     * 报销类型
     */
    @NotBlank(message = "报销类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String projectType;

    /**
     * 报销原因
     */
    @NotBlank(message = "报销原因不能为空", groups = { AddGroup.class, EditGroup.class })
    private String reimbursementReason;

    /**
     * 报销人
     */
    @NotBlank(message = "报销人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String reimbursementUser;

    /**
     * 提交时间
     */
    @NotNull(message = "提交时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date submitTime;

    /**
     * 收款信息
     */
    @NotBlank(message = "收款信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String collectionInformation;

    /**
     * 状态
     */
    //@NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String status;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;
    /**
     * 是否删除（0否 1是）
     */
    private Integer isDeleted;
    /**
     * 设备类型（APP，PC ）
     */
    private String deviceType;

    private String consumptionDetails;
    private String detailsValue;
}
