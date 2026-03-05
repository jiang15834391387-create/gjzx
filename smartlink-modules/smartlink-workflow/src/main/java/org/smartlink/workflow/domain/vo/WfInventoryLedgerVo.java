package org.smartlink.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.WfInventoryLedger;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WfInventoryLedger.class)
public class WfInventoryLedgerVo {
    @ExcelProperty(value = "台账ID")
    private Long id;
    @ExcelProperty(value = "库存物品ID")
    private Long inventoryId;
    @ExcelProperty(value = "库存物品名称")
    private Integer operationType;
    @ExcelProperty(value = "操作类型") // 1-入库, 2-出库
    private String operationTypeName;
    @ExcelProperty(value = "操作数量")
    private Integer operationCount;
    @ExcelProperty(value = "操作前数量")
    private Integer beforeQuantity;
    @ExcelProperty(value = "操作后数量")
    private Integer afterQuantity;
    private String operationContent;
    @ExcelProperty(value = "操作备注")
    private String remark;
    @ExcelProperty(value = "操作人名称")
    private String createByName;
    @ExcelProperty(value = "操作时间")
    private Date createTime;
}
