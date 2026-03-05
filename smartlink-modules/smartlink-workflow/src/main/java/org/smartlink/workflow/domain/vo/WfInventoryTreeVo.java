package org.smartlink.workflow.domain.vo;

import lombok.Data;
import java.util.List;

@Data
public class WfInventoryTreeVo {
    /**
     * 节点唯一标识（父节点为分类名称，子节点直接就是 itemCode）
     */
    private String id;

    /**
     * 显示的名称（分类名称 或 物品名称）
     */
    private String label;

    /**
     * 对应的物品编码（仅叶子节点有值，前端可直接获取）
     */
    private String itemCode;

    /**
     * 子节点列表
     */
    private List<WfInventoryTreeVo> children;
}
