package org.smartlink.server.nc.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 系统OCR配置视图对象 sys_ocr_config
 *
 * @author L
 * @date
 */
@Data
@ApiModel("系统OCR配置视图对象")
@ExcelIgnoreUnannotated
public class SysOcrConfigVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主建
     */
    @ExcelProperty(value = "主建")
    @ApiModelProperty("主建")
    private Long ocrConfigId;

    /**
     * 配置KEY
     */
    @ExcelProperty(value = "配置KEY")
    @ApiModelProperty("配置KEY")
    private String configKey;

    /**
     * 配置详情
     */
    @ExcelProperty(value = "配置详情")
    @ApiModelProperty("配置详情")
    private String detailInfo;

    /**
     * 状态(0正常1停用)
     */
    @ExcelProperty(value = "状态(0正常1停用)")
    @ApiModelProperty("状态(0正常1停用)")
    private String status;

    /**
     * 扩展字段
     */
    @ExcelProperty(value = "扩展字段")
    @ApiModelProperty("扩展字段")
    private String ext1;

    /**
     * 创建者
     */
    @ExcelProperty(value = "创建者")
    @ApiModelProperty("创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ExcelProperty(value = "更新者")
    @ApiModelProperty("更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;


}
