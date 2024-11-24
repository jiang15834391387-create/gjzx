package org.smartlink.web.domain.imagefilesinfo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import org.smartlink.web.annotation.ExcelDictFormat;
import org.smartlink.web.annotation.FieldName;

import java.util.Date;
import java.util.List;


/**
 * 图片文件视图对象 data_image_files_info
 *
 * @author L
 * @date
 */
@Data
//@ApiModel("图片文件视图对象")
@ExcelIgnoreUnannotated
public class DataImageFilesInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
//    @ApiModelProperty("图片表主键")
    private String fileId;

    /**
     * 友报账编号
     */
    @ExcelProperty(value = "友报账编号")
//    @ApiModelProperty("友报账编号")
    private String barCode;
    /**
     * 业务联查使用
     */
    @ExcelProperty(value = "业务联查使用")
//    @ApiModelProperty("业务联查使用")
    private String nccInvestigationNo;
    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
//    @ApiModelProperty("批次号")
    private String batchId;

    /**
     * 税务云返回图片id
     */
    @ExcelProperty(value = "税务云返回图片id")
//    @ApiModelProperty("税务云返回图片id")
    private String ncImageId;

    /**
     * 客户端ip
     */
    @ExcelProperty(value = "客户端ip")
//    @ApiModelProperty("客户端ip")
    private String cip;

    /**
     * 文件夹分类名称
     */
    @ExcelProperty(value = "文件夹分类名称")
//    @ApiModelProperty("文件夹分类名称")
    private String docName;

    /**
     * 文件md5
     */
    @ExcelProperty(value = "文件md5")
//    @ApiModelProperty("文件md5")
    private String fileMd5;

    /**
     * 文件名称
     */
    @ExcelProperty(value = "文件名称")
//    @ApiModelProperty("文件名称")
    private String fileName;

    /**
     * 文件大小
     */
    @ExcelProperty(value = "文件大小")
//    @ApiModelProperty("文件大小")
    private String fileSize;

    /**
     * 文件流程状态（预留）
     */
    @ExcelProperty(value = "文件流程状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "预=留")
//    @ApiModelProperty("文件流程状态（预留）")
    private String fileFlowStatus;

    /**
     * 文件类型
     */
    @ExcelProperty(value = "文件类型")
//    @ApiModelProperty("文件类型")
    private String fileType;

    /**
     * 票夹文件信息
     */
    @ExcelProperty(value = "票夹文件信息")
//    @ApiModelProperty("票夹文件信息")
    private String folderId;

    /**
     * 圖片是否保密 0-加密 1-正常
     */
    @ExcelProperty(value = "圖片是否保密 0-加密 1-正常")
//    @ApiModelProperty("圖片是否保密 0-加密 1-正常")
    private String imageSecret;

    /**
     * 缩略图文件
     */
    @ExcelProperty(value = "缩略图文件")
//    @ApiModelProperty("缩略图文件")
    private String surl;

    /**
     * 图片源文件，附件源文件
     */
    @ExcelProperty(value = "图片源文件，附件源文件")
//    @ApiModelProperty("图片源文件，附件源文件")
    private String iurl;

    /**
     * pdf文件
     */
    @ExcelProperty(value = "pdf文件")
//    @ApiModelProperty("pdf文件")
    private String purl;

    /**
     * 批注文件
     */
    @ExcelProperty(value = "批注文件")
//    @ApiModelProperty("批注文件")
    private String lurl;

    /**
     * 文件信息
     */
    @ExcelProperty(value = "文件信息")
//    @ApiModelProperty("文件信息")
    private String message;

    /**
     * 新友报账编号
     */
    @ExcelProperty(value = "新友报账编号")
//    @ApiModelProperty("新友报账编号")
    private String newBarCode;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
//    @ApiModelProperty("排序")
    private Integer sort;
    /**
     * 0-删除 1-正常
     */
    @ExcelProperty(value = "0-删除 1-正常")
//    @ApiModelProperty("0-删除 1-正常")
    private String operateState;
    /**
     * 文档名
     */
    @ExcelProperty(value = "文档名")
//    @ApiModelProperty("文档名")
    private String documentName;

    /**
     * 节点id
     */
    @ExcelProperty(value = "节点id")
//    @ApiModelProperty("节点id")
    private String nodeId;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
//    @ApiModelProperty("备注")
    private String parentFileId;

    /**
     * 父节点id
     */
    @ExcelProperty(value = "父节点id")
//    @ApiModelProperty("父节点id")
    private String parentNodeId;

    /**
     * 文件排序编号
     */
    @ExcelProperty(value = "文件排序编号")
//    @ApiModelProperty("文件排序编号")
    private Long fileSort;

    /**
     * 文件状态
     */
    @ExcelProperty(value = "文件状态")
//    @ApiModelProperty("文件状态")
    private String fileStatus;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
//    @ApiModelProperty("是否删除标识 0-不删除  1-删除")
    private String deleteFlag;
    @FieldName(value = "多票据上传包含子发票类型数组")
    private String includeTypeArr;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
//    @ApiModelProperty("备注")
    private String remark;

    @ExcelProperty(value = "排序字段")
//    @ApiModelProperty("排序字段")
    private Integer sortValue;

//    @ApiModelProperty("图片扫描到页面的时间 格式yyyy-MM-dd HH:mm:ss")
    private String pageTime;

//    @ApiModelProperty("票夹发票是已否使用")
    private String isUse;

//    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

//    @ApiModelProperty("票种名称")
    private String productName;

//    @ApiModelProperty("创建时间")
    private String createTimeStr;

    /**
     * 识别信息文件ID
     */
    @TableField(exist = false)
    private List<String> cutFileId;
}
