package org.smartlink.server.nodeType.domain.bo;


import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.server.nodeType.domain.DataNodeType;

/**
 * 树节点业务对象 data_node_type
 *
 * @author Lion Li
 * @date 2024-08-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataNodeType.class, reverseConvertGenerate = false)
public class DataNodeTypeBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 节点类型
     */
    @NotBlank(message = "节点类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nodeType;

    /**
     * 节点名称
     */
    @NotBlank(message = "节点名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nodeName;

    /**
     * 节点编码
     */
    @NotBlank(message = "节点编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String nodeCode;

    /**
     * 父节点ID
     */
    @NotBlank(message = "父节点ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String parentId;


}
