package org.smartlink.business.scan.dto;

import lombok.Data;

@Data
public class TreeNodeSaveDTO {
    /**
     * 父类ID(赋值给数据库的parentId)
     */
    private String productId;
    /**
     * 节点类型
     */
    private String productType;
    /**
     * 节点层级
     */
    private Long productLevel;
    /**
     * 节点名称
     */
    private String productName;
}
