package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.*;
import org.smartlink.common.tenant.core.TenantEntity;

import java.util.List;

/**
 * BPM 工作流的表单定义
 * 用于工作流的申请表单，需要动态配置的场景
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bpm_form")
public class BpmFormDO  extends TenantEntity {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 表单名
     */
    private String name;
    private Integer isDeleted;
    /**
     * 表单的配置
     */
    private String conf;
    /**
     * 表单项的数组
     *
     * 目前直接将 https://github.com/JakHuang/form-generator 生成的 JSON 串，直接保存
     * 定义：https://github.com/JakHuang/form-generator/issues/46
     */
    //@TableField(typeHandler = JacksonTypeHandler.class)
    private String fields;
    /**
     * 备注
     */
    private String remark;

}
