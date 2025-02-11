package org.smartlink.workflow.domain.bo;


import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.workflow.domain.TestFormConfig;
import org.smartlink.workflow.domain.TestFormManage;

import java.util.List;

/**
 * 单管理业务对象 test_form_manage
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = TestFormManage.class, reverseConvertGenerate = false)
public class TestFormManageBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 单据名称
     */
    @NotBlank(message = "单据名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formName;

    /**
     * 单据绑定名称
     */
    @NotBlank(message = "单据绑定名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formBindName;

    /**
     * 单据绑定类型
     */
    @NotBlank(message = "单据绑定类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formBindType;

    /**
     * 状态
     */
    //@NotBlank(message = "状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formStatus;

    /**
     *
     */
    //@NotBlank(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 表单用途名称
     */
    //@NotBlank(message = "表单用途不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formPurposeName;

    /**
     * 表单用途名称
     */
    //@NotBlank(message = "表单用途不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formPurposeType;

    /**
     * 是否删除（0否，1是）
     */
    //@NotNull(message = "是否删除（0否，1是）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long isDeleted;

    private List<TestFormConfig> formConfigList;
}
