package org.smartlink.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("wf_inventory_ledger")
public class WfInventoryLedger extends BaseEntity {
    @TableId
    private Long id;
    private Long inventoryId;
    private Integer operationType; // 1-入库, 2-出库
    private Integer operationCount;
    private Integer beforeQuantity;
    private Integer afterQuantity;
    private String operationContent;
    private String createByName;
    private String inventoryName;
    private String remark;
}
