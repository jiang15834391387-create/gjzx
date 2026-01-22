package org.smartlink.workflow.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 单管理对象 test_form_manage
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("test_form_manage")
public class TestFormManage extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private Long id;
    private String formName;
    private String formLogo;
    private String formType;


    private Long formBindId;
    private String formBindName;

    private Long categoryId;
    private String categoryName;
    private String categoryType;

    private String remark;
    /**
     * 是否删除（0否，1是）
     */
    private Integer isDeleted;
    /**
     * 是否绑定工作流程（0否，1是）
     */
    private Integer isBindModel;

    private String modelId;
    /**
     * 排序
     */
    private Long sort;

}
