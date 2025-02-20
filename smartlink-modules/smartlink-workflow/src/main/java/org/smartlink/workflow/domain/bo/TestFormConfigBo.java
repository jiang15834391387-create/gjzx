package org.smartlink.workflow.domain.bo;

import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.workflow.domain.TestFormConfig;

/**
 * 单配置业务对象 test_form_config
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Data
@AutoMapper(target = TestFormConfig.class, reverseConvertGenerate = false)
public class TestFormConfigBo  {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 字段名称
     */
    @NotBlank(message = "字段名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fieldName;

    private String fieldTypeData;

    /**
     * 字段英文名称
     */
    private String fieldValue;
    /**
     * 字段名称
     */
    @NotBlank(message = "字段名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fieldType;

    /**
     * 是否必填（0否 1是）
     */
    @NotNull(message = "是否必填（0否 1是）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Integer fieldRequired;

    private Long formId;


}
