package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataImageFilesInfo;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 图片文件业务对象 data_image_files_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataImageFilesInfo.class, reverseConvertGenerate = false)
public class DataImageFilesInfoBo extends BaseEntity {

    /**
     * 图片表主键
     */
    @NotBlank(message = "图片表主键不能为空", groups = { EditGroup.class })
    private String fileId;

    /**
     * 父文件ID（如果是子文件，则存储原始文件ID）如果 parent_file_id 为 NULL，表示是原始文件。
     */
    @NotBlank(message = "父文件ID（如果是子文件，则存储原始文件ID）如果 parent_file_id 为 NULL，表示是原始文件。不能为空", groups = { AddGroup.class, EditGroup.class })
    private String parentFileId;

    /**
     * 业务联查使用
     */
    @NotBlank(message = "业务联查使用不能为空", groups = { AddGroup.class, EditGroup.class })
    private String glorityInvestigationNo;

    /**
     * 发票类型
     */
    @NotBlank(message = "发票类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoice;

    /**
     * 友报账编号
     */
    @NotBlank(message = "友报账编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String barCode;

    /**
     * 批次号
     */
    @NotBlank(message = "批次号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String batchId;

    /**
     * ocr返回图片id
     */
    @NotBlank(message = "ocr返回图片id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String glorityImageId;

    /**
     * 客户端ip
     */
    @NotBlank(message = "客户端ip不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cip;

    /**
     * 文件md5
     */
    @NotBlank(message = "文件md5不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileMd5;

    /**
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileName;

    /**
     * 文件大小
     */
    @NotBlank(message = "文件大小不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileSize;

    /**
     * 文件流程状态 0待重扫 1补扫 2重扫完成 3事后补扫文件 4本次重扫未提交
     */
    @NotBlank(message = "文件流程状态 0待重扫 1补扫 2重扫完成 3事后补扫文件 4本次重扫未提交不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileFlowStatus;

    /**
     * 文件类型
     */
    @NotBlank(message = "文件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileType;

    /**
     * 票夹文件信息
     */
    @NotBlank(message = "票夹文件信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String folderId;

    /**
     * 圖片是否保密 0-加密 1-正常
     */
    @NotBlank(message = "圖片是否保密 0-加密 1-正常不能为空", groups = { AddGroup.class, EditGroup.class })
    private String imageSecret;

    /**
     * 缩略图文件
     */
    @NotBlank(message = "缩略图文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String surl;

    /**
     * 图片源文件，附件源文件
     */
    @NotBlank(message = "图片源文件，附件源文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String iurl;

    /**
     * pdf文件
     */
    @NotBlank(message = "pdf文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String purl;

    /**
     * 批注文件
     */
    @NotBlank(message = "批注文件不能为空", groups = { AddGroup.class, EditGroup.class })
    private String lurl;

    /**
     * 文件信息
     */
    @NotBlank(message = "文件信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String message;

    /**
     * 操作状态
     */
    @NotBlank(message = "操作状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String operateState;

    /**
     * 文档名
     */
    @NotBlank(message = "文档名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String documentName;

    /**
     * 查验状态
     */
    @NotBlank(message = "查验状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkStatus;

    /**
     * 文件状态
     */
    @NotBlank(message = "文件状态不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileStatus;

    /**
     * 旋转角度
     */
    @NotNull(message = "旋转角度不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long rotateAngle;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;

    /**
     * 图片扫描到页面的时间
     */
    @NotBlank(message = "图片扫描到页面的时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pageTime;

    /**
     * 多票据上传包含子发票类型数组
     */
    @NotBlank(message = "多票据上传包含子发票类型数组不能为空", groups = { AddGroup.class, EditGroup.class })
    private String includeTypeArr;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;

    /**
     * 排序字段
     */
    @NotNull(message = "排序字段不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long sortValue;

    /**
     * 小程序上传发票用户ID
     */
    @NotBlank(message = "小程序上传发票用户ID不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

    /**
     * 微信小程序发票是否使用(0=未使用,1=已使用)
     */
    @NotBlank(message = "微信小程序发票是否使用(0=未使用,1=已使用)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String isUse;


}
