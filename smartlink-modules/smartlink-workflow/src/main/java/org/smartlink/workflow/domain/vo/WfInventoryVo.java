package org.smartlink.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.WfInventory;

import java.util.Date;

@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = WfInventory.class)
public class WfInventoryVo {
    @ExcelProperty(value = "库存ID")
    private Long id;
    @ExcelProperty(value = "物品编码")
    private String itemCode;
    @ExcelProperty(value = "物品名称")
    private String itemName;
    @ExcelProperty(value = "分类")
    private String category;
    @ExcelProperty(value = "规格型号")
    private String specification;
    @ExcelProperty(value = "单位")
    private String unit;
    @ExcelProperty(value = "当前库存")
    private Integer quantity;
    @ExcelProperty(value = "单价")
    private String price;
    @ExcelProperty(value = "供应商")
    private String supplier;
    @ExcelProperty(value = "存放位置")
    private String warehouseLocation;
    @ExcelProperty(value = "预警库存")
    private Integer minStock;
    @ExcelProperty(value = "备注")
    private String remark;
    @ExcelProperty(value = "更新时间")
    private Date updateTime;
    private String delFlag;
}
