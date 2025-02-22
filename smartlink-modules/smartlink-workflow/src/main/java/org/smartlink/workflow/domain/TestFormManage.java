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

    /**
     * 表单名称
     */
    private String formName;

/*    *//**
     * 表单用途名称
     *//*
    private String formPurposeName;

    *//**
     * 表单用途类型
     *//*
    private String formPurposeType;*/

    /**
     * 单据绑定名称
     */
    private String formBindName;

    /**
     * 单据绑定类型
     */
    private String formBindType;


    private String remark;

    /**
     * 是否删除（0否，1是）
     */
    private Long isDeleted;


}
