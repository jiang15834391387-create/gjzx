package org.smartlink.server.nc.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 任务、图片中间关联视图对象 data_cm_info
 *
 * @author L
 * @date
 */
@Data
@ApiModel("任务、图片中间关联视图对象")
@ExcelIgnoreUnannotated
public class DataCmInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private String id;

    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
    @ApiModelProperty("批次号")
    private String batchId;

    /**
     * 业务流水号
     */
    @ExcelProperty(value = "业务流水号")
    @ApiModelProperty("业务流水号")
    private String businessSerialNo;

    /**
     * 友报账编号
     */
    @ExcelProperty(value = "友报账编号")
    @ApiModelProperty("友报账编号")
    private String barCode;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    @ApiModelProperty("是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;


}
