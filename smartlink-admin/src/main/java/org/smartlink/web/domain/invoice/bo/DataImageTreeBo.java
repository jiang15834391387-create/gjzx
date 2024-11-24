package org.smartlink.web.domain.invoice.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.web.domain.modle.BaseEntity;


/**
 * 图片树节点业务对象 data_image_tree
 *
 * @author L
 * @date
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("图片树节点业务对象")
@NoArgsConstructor
public class DataImageTreeBo extends BaseEntity {
    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id", required = true)
    private Long id;

    /**
     * 子节点ID
     */
    @ApiModelProperty(value = "子节点ID", required = true)
    @NotBlank(message = "子节点ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productId;

    /**
     * 父菜单ID
     */
    @ApiModelProperty(value = "父菜单ID", required = true)
    @NotBlank(message = "父菜单ID不能为空", groups = {AddGroup.class, EditGroup.class})
    private String parentId;

    /**
     * 子节点名称
     */
    @ApiModelProperty(value = "子节点名称", required = true)
    @NotBlank(message = "子节点名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productName;

    /**
     * 节点层级
     */
    @ApiModelProperty(value = "节点层级", required = true)
    @NotNull(message = "节点层级不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long productLevel;

    /**
     * 节点type
     */
    @ApiModelProperty(value = "节点type 0默认 1自定义 其他节点code ", required = true)
    @NotBlank(message = "节点type不能为空", groups = {AddGroup.class, EditGroup.class})
    private String productType;

    /**
     * 显示顺序
     */
    @ApiModelProperty(value = "显示顺序", required = true)
    @NotNull(message = "显示顺序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long orderNum;

    /**
     * imageid
     */
    @ApiModelProperty(value = "imageid", required = true)
    private String imageId;

    /**
     * batchid
     */
    @ApiModelProperty(value = "batchid", required = true)
    private String batchId;

    /**
     * 0=正常,1=停用
     */
    @ApiModelProperty(value = "0=正常,1=停用", required = true)
    @NotBlank(message = "0=正常,1=停用不能为空", groups = {AddGroup.class, EditGroup.class})
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
    @ApiModelProperty("批扫业务标识")
    private String batchBusinessQuote;

    public DataImageTreeBo(String fileId, String fileType, String fileName, String batchId) {
        this.productId = fileId;
        this.parentId = fileType;
        this.productName = fileName;
        this.productLevel = 2L;
        this.productType = "0";
        this.status = "0";
        this.orderNum = 0L;
        this.imageId = fileId;
        this.batchId = batchId;
    }
}
