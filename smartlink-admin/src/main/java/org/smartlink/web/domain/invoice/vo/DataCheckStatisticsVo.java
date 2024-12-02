package org.smartlink.web.domain.invoice.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 查验记录统计视图对象 data_check_statistics
 *
 * @author L
 * @date
 */
@Data
@ApiModel("查验记录统计视图对象")
@ExcelIgnoreUnannotated
public class DataCheckStatisticsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String ID;

    /**
     * 查验厂商
     */
    @ExcelProperty(value = "查验厂商")
    @ApiModelProperty("查验厂商")
    private String checkSupplier;

    /**
     * 类型，0失败，1成功
     */
    @ExcelProperty(value = "类型，0失败，1成功")
    @ApiModelProperty("类型，0失败，1成功")
    private String TYPE;

    /**
     * 查验备注
     */
    @ExcelProperty(value = "查验备注")
    @ApiModelProperty("查验备注")
    private String REMARK;

    /**
     * 查验日期
     */
    @ExcelProperty(value = "查验日期")
    @ApiModelProperty("查验日期")
    private Date checkDate;

    /**
     * 总使用次数
     */
    @ExcelProperty(value = "总使用次数")
    @ApiModelProperty(value = "总使用次数", required = true)
    private Integer total;

    /**
     * 已使用次数
     */
    @ExcelProperty(value = "已使用次数")
    @ApiModelProperty(value = "已使用次数", required = true)
    private Integer used;

    /**
     * 剩余使用次数
     */
    @ExcelProperty(value = "剩余使用次数")
    @ApiModelProperty(value = "剩余使用次数", required = true)
    private Integer surplusUsed;

}
