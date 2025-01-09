package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 图片文件对象 data_image_files_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_image_files_info")
public class DataImageFilesInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片表主键
     */
    @TableId(value = "file_id")
    private String fileId;

    /**
     * 父文件ID（如果是子文件，则存储原始文件ID）如果 parent_file_id 为 NULL，表示是原始文件。
     */
    private String parentFileId;

    /**
     * 业务联查使用
     */
    private String glorityInvestigationNo;

    /**
     * 发票类型
     */
    private String invoice;

    /**
     * 条形码
     */
    private String barCode;

    /**
     * 批次号
     */
    private String batchId;

    /**
     * ocr返回图片id
     */
    private String glorityImageId;

    /**
     * 客户端ip
     */
    private String cip;

    /**
     * 文件md5
     */
    private String fileMd5;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 文件流程状态 0待重扫 1补扫 2重扫完成 3事后补扫文件 4本次重扫未提交
     */
    private String fileFlowStatus;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 票夹文件信息
     */
    private String folderId;

    /**
     * 圖片是否保密 0-加密 1-正常
     */
    private String imageSecret;

    /**
     * 缩略图文件
     */
    private String surl;

    /**
     * 图片源文件，附件源文件
     */
    private String iurl;

    /**
     * pdf文件
     */
    private String purl;

    /**
     * 批注文件
     */
    private String lurl;

    /**
     * 文件信息
     */
    private String message;

    /**
     * 文档名
     */
    private String documentName;

    /**
     * 查验状态
     */
    private String checkStatus;

    /**
     * 文件状态
     */
    private String fileStatus;

    /**
     * 排序字段
     */
    private String sortValue;

    /**
     * 旋转角度
     */
    private String rotateAngle;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 图片扫描到页面的时间
     */
    private String pageTime;

    /**
     * 多票据上传包含子发票类型数组
     */
    private String includeTypeArr;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
