package org.smartlink.web.domain.imagefilesinfo;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;


/**
 * 图片文件业务对象 data_image_files_info
 *
 * @author L
 * @date
 */

@Data
@EqualsAndHashCode(callSuper = true)
//@ApiModel("图片文件业务对象")
public class DataImageFilesInfoBo extends BaseEntity {

    /**
     * 图片表主键
     */
//    @ApiModelProperty(value = "图片表主键", required = true)
    @NotBlank(message = "图片表主键不能为空", groups = {EditGroup.class})
    private String fileId;

    /**
     * 友报账编号
     */
//    @ApiModelProperty(value = "友报账编号", required = true)
    @NotBlank(message = "友报账编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String barCode;
    /**
     * 业务联查使用
     */
//    @ApiModelProperty(value = "业务联查使用", required = true)
    @NotBlank(message = "业务联查使用不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nccInvestigationNo;
    /**
     * 批次号
     */
//    @ApiModelProperty(value = "批次号", required = true)
    @NotBlank(message = "批次号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String batchId;

    /**
     * 税务云返回图片id
     */
//    @ApiModelProperty(value = "税务云返回图片id", required = true)
    @NotBlank(message = "税务云返回图片id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String ncImageId;

    /**
     * 客户端ip
     */
//    @ApiModelProperty(value = "客户端ip", required = true)
    @NotBlank(message = "客户端ip不能为空", groups = {AddGroup.class, EditGroup.class})
    private String cip;

    /**
     * 文件夹分类名称
     */
//    @ApiModelProperty(value = "文件夹分类名称", required = true)
    @NotBlank(message = "文件夹分类名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String docName;

    /**
     * 文件md5
     */
//    @ApiModelProperty(value = "文件md5", required = true)
    @NotBlank(message = "文件md5不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fileMd5;

    /**
     * 文件名称
     */
//    @ApiModelProperty(value = "文件名称", required = true)
    @NotBlank(message = "文件名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fileName;

    /**
     * 文件大小
     */
//    @ApiModelProperty(value = "文件大小", required = true)
    @NotBlank(message = "文件大小不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fileSize;

    /**
     * 文件流程状态（预留）
     */
//    @ApiModelProperty(value = "文件流程状态（预留）", required = true)
    @NotBlank(message = "文件流程状态（预留）不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fileFlowStatus;

    /**
     * 文件类型
     */
//    @ApiModelProperty(value = "文件类型", required = true)
    @NotBlank(message = "文件类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fileType;

    /**
     * 票夹文件信息
     */
//    @ApiModelProperty(value = "票夹文件信息", required = true)
    @NotBlank(message = "票夹文件信息不能为空", groups = {AddGroup.class, EditGroup.class})
    private String folderId;

    /**
     * 圖片是否保密 0-加密 1-正常
     */
//    @ApiModelProperty(value = "圖片是否保密 0-加密 1-正常", required = true)
    @NotBlank(message = "圖片是否保密 0-加密 1-正常不能为空", groups = {AddGroup.class, EditGroup.class})
    private String imageSecret;

    /**
     * 缩略图文件
     */
//    @ApiModelProperty(value = "缩略图文件", required = true)
    @NotBlank(message = "缩略图文件不能为空", groups = {AddGroup.class, EditGroup.class})
    private String surl;

    /**
     * 图片源文件，附件源文件
     */
//    @ApiModelProperty(value = "图片源文件，附件源文件", required = true)
    @NotBlank(message = "图片源文件，附件源文件不能为空", groups = {AddGroup.class, EditGroup.class})
    private String iurl;

    /**
     * pdf文件
     */
//    @ApiModelProperty(value = "pdf文件", required = true)
    @NotBlank(message = "pdf文件不能为空", groups = {AddGroup.class, EditGroup.class})
    private String purl;

    /**
     * 批注文件
     */
//    @ApiModelProperty(value = "批注文件", required = true)
    @NotBlank(message = "批注文件不能为空", groups = {AddGroup.class, EditGroup.class})
    private String lurl;

    /**
     * 文件信息
     */
//    @ApiModelProperty(value = "文件信息", required = true)
    @NotBlank(message = "文件信息不能为空", groups = {AddGroup.class, EditGroup.class})
    private String message;

    /**
     * 新友报账编号
     */
//    @ApiModelProperty(value = "新友报账编号", required = true)
    @NotBlank(message = "新友报账编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newBarCode;

    /**
     * 排序
     */
//    @ApiModelProperty(value = "排序", required = true)
    @NotBlank(message = "排序不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer sort;
    /**
     * 0-删除 1-正常
     */
//    @ApiModelProperty(value = "操作状态", required = true)
    @NotBlank(message = "操作状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String operateState;
    /**
     * 文档名
     */
//    @ApiModelProperty(value = "文档名", required = true)
    @NotBlank(message = "文档名不能为空", groups = {AddGroup.class, EditGroup.class})
    private String documentName;

    /**
     * 节点id
     */
//    @ApiModelProperty(value = "节点id", required = true)
    @NotBlank(message = "节点id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String nodeId;

    /**
     * 备注
     */
//    @ApiModelProperty(value = "备注", required = true)
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String parentFileId;

    /**
     * 父节点id
     */
//    @ApiModelProperty(value = "父节点id", required = true)
    @NotBlank(message = "父节点id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String parentNodeId;

    /**
     * 文件排序编号
     */
//    @ApiModelProperty(value = "文件排序编号", required = true)
    @NotNull(message = "文件排序编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long fileSort;

    /**
     * 文件状态
     */
//    @ApiModelProperty(value = "文件状态", required = true)
    @NotBlank(message = "文件状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private String fileStatus;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
//    @ApiModelProperty(value = "是否删除标识 0-不删除  1-删除", required = true)
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = {AddGroup.class, EditGroup.class})
    private String deleteFlag;
    @FieldName(value = "多票据上传包含子发票类型数组")
    private String includeTypeArr;
    /**
     * 备注
     */
//    @ApiModelProperty(value = "备注", required = true)
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;
//    @ApiModelProperty(value = "排序字段", required = true)
    @NotBlank(message = "排序字段不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer sortValue;

//    @ApiModelProperty("图片扫描到页面的时间 格式yyyy-MM-dd HH:mm:ss")
    private String pageTime;
}
