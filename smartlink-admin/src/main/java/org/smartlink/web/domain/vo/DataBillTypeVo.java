package org.smartlink.web.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 单据类型视图对象 data_bill_type
 *
 * @author L
 * @date
 */
@Data
@ApiModel("单据类型视图对象")
@ExcelIgnoreUnannotated
public class DataBillTypeVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private String id;

    /**
     * 单据是否普启用ocr识别，0-禁止  1-启用
     */
    @ExcelProperty(value = "单据是否普启用ocr识别，0-禁止  1-启用")
    @ApiModelProperty("单据是否普启用ocr识别，0-禁止  1-启用")
    private String ocrEnable;

    /**
     * 父级单据类型id
     */
    @ExcelProperty(value = "父级单据类型id")
    @ApiModelProperty("父级单据类型id")
    private String parentTpyeId;

    /**
     * 父级系统
     */
    @ExcelProperty(value = "父级系统")
    @ApiModelProperty("父级系统")
    private String parentSystem;

    /**
     * 所属组织机构
     */
    @ExcelProperty(value = "所属组织机构")
    @ApiModelProperty("所属组织机构")
    private String groupId;

    /**
     * 系统编码
     */
    @ExcelProperty(value = "系统编码")
    @ApiModelProperty("系统编码")
    private String systemCode;

    /**
     * 系统名称
     */
    @ExcelProperty(value = "系统名称")
    @ApiModelProperty("系统名称")
    private String systemName;

    /**
     * 单据类型编码
     */
    @ExcelProperty(value = "单据类型编码")
    @ApiModelProperty("单据类型编码")
    private String typeCode;

    /**
     * 单据类型名称
     */
    @ExcelProperty(value = "单据类型名称")
    @ApiModelProperty("单据类型名称")
    private String typeName;

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
