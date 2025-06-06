package org.smartlink.workflow.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
    private Long fromManageId;
    private Long fromId;
    private String fromName;
    private String fromType;
    private Long categoryId;
    private String categoryName;
    private String categoryType;
    private String dataChannel;
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
    /**
     * 创建部门
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createDept;

    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新者
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateBy;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


    private String detailsData;
}
