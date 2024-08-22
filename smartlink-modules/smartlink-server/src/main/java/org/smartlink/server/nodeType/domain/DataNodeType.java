package org.smartlink.server.nodeType.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 树节点对象 data_node_type
 *
 * @author Lion Li
 * @date 2024-08-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_node_type")
public class DataNodeType extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 节点类型
     */
    private String nodeType;

    /**
     * 节点名称
     */
    private String nodeName;

    /**
     * 节点编码
     */
    private String nodeCode;

    /**
     * 父节点ID
     */
    private String parentId;


    private Long version;


    private Long delFlag;


}
