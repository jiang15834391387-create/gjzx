package org.smartlink.workflow.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 单配置对象 test_form_config
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Data
@TableName("test_form_config")
public class TestFormConfig  {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段名称
     */
    private String fieldType;

    /**
     * 是否必填（0否 1是）
     */
    private Integer fieldRequired;

    private Long formId;

    private String tenantId;

}
