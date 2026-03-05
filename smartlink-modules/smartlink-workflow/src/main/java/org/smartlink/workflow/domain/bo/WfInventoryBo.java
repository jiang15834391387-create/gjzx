package org.smartlink.workflow.domain.bo;

import lombok.Data;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

@Data
public class WfInventoryBo extends BaseEntity {
    private Long id;
    private String itemCode;
    private String itemName;
    private String category;
    private String specification;
    private String unit;
    private Integer quantity;
    private String price;
    private String supplier;
    private String warehouseLocation;
    private Integer minStock;
    private String operationContent;
    private String remark;
    private String delFlag;
}
