package org.smartlink.workflow.domain.bo;


import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
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

    //单据名称
    private String formName;

    //单据logo
    private String formLogo;

    //单据类型手动填写
    private String formType;

    //备注
    private String remark;

    //排序
    private Long sort;

    /**
     * 是否绑定工作流程（0否，1是）
     */
    private Integer isBindModel;
    /**
     * 是否删除（0否，1是）
     */
    private Integer isDeleted;
/*-------------------------------------------------------------*/
    /*分组信息*/
    private Long categoryId;
    private String categoryName;
    private String categoryType;


    /**
     * 单据绑定id
     */
    private Long formBindId;
    /**
     * 单据绑定名称
     */
    @NotBlank(message = "单据绑定名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String formBindName;




    private String modelId;





}
