package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_inventory")
public class WfInventory extends BaseEntity {
    @TableId
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
    private String remark;
    private String delFlag;
}
