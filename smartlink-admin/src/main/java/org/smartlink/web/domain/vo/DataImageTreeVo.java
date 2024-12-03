package org.smartlink.web.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 图片树节点视图对象 data_image_tree
 *
 * @author L
 * @date
 */
@Data
@ApiModel("图片树节点视图对象")
@ExcelIgnoreUnannotated
public class DataImageTreeVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ExcelProperty(value = "主键id")
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 子节点ID
     */
    @ExcelProperty(value = "子节点ID")
    @ApiModelProperty("子节点ID")
    private String productId;

    /**
     * 父菜单ID
     */
    @ExcelProperty(value = "父菜单ID")
    @ApiModelProperty("父菜单ID")
    private String parentId;

    /**
     * 子节点名称
     */
    @ExcelProperty(value = "子节点名称")
    @ApiModelProperty("子节点名称")
    private String productName;

    /**
     * 节点层级
     */
    @ExcelProperty(value = "节点层级")
    @ApiModelProperty("节点层级")
    private Long productLevel;

    /**
     * 节点type
     */
    @ExcelProperty(value = "节点type")
    @ApiModelProperty("节点type")
    private String productType;

    /**
     * 显示顺序
     */
    @ExcelProperty(value = "显示顺序")
    @ApiModelProperty("显示顺序")
    private Long orderNum;

    /**
     * imageid
     */
    @ExcelProperty(value = "imageid")
    @ApiModelProperty("imageid")
    private String imageId;

    /**
     * batchid
     */
    @ExcelProperty(value = "batchid")
    @ApiModelProperty("batchid")
    private String batchId;

    /**
     * 0=正常,1=停用
     */
    @ExcelProperty(value = "0=正常,1=停用")
    @ApiModelProperty("0=正常,1=停用")
    private String status;

    /**
     * 匹配文件规则
     */
    private String matchingFileRules;
    /**
     * 匹配单据规则
     */
    private String matchingBillRules;
    /**
     * 0=不必须,1=必须上传
     */
    private String mustUpload;
    /**
     * 0=不能删除,1=可以删除
     */
    private String deleteOrNot;

    /**
     * 空节点是否显示 0=不,1=显示
     */
    private String blankShow;

    /**
     * 批扫业务标识
     */
    @ExcelProperty(value = "批扫业务标识")
    @ApiModelProperty("批扫业务标识")
    private String batchBusinessQuote;
}
