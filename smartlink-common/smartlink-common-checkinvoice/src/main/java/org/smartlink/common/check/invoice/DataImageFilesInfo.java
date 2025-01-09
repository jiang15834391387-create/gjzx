 package org.smartlink.common.check.invoice;

 import com.baomidou.mybatisplus.annotation.TableField;
 import com.baomidou.mybatisplus.annotation.TableId;
 import com.baomidou.mybatisplus.annotation.TableName;
 import lombok.Data;
 import org.smartlink.common.check.doman.InvoiceBaseEntity;
 import org.smartlink.common.mybatis.core.domain.BaseEntity;

 import java.util.List;


/**
 * 图片文件对象 invoice_files
 *
 */
@Data
@TableName("invoice_files")
public class DataImageFilesInfo extends InvoiceBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 图片表主键
     */
    @TableId(value = "file_id")
    private String fileId;
    /**
     * 友报账编号
     */
    private String barCode;
    /**
     * 业务联查使用
     */
    private String nccInvestigationNo;
    /**
     * 批次号
     */
    private String batchId;
    /**
     * 税务云返回图片id
     */
    private String ncImageId;
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
     * 补充状态 0待重扫 1补扫 2重扫完成 3事后补扫文件 4本次重扫未提交
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
     * 源文件
     */
    private String url;
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
     * 0-删除 1-正常
     */
    private String operateState;
    /**
     * 文档名
     */
    private String documentName;
    /**
     * 上级图片ID
     */
    private String parentFileId;
    /**
     * 文件状态
     */
    private String fileStatus;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 多票据上传包含子发票类型数组
     */
    private String includeTypeArr;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 备注
     */
    private String remark;

    /**
     * 排序字段
     */
    private Integer sortValue;

    /**
     * 图片扫描到页面的时间
     */
    private String pageTime;

    /**
     * 图片旋转角度
     */
    private int rotateAngle;

    /**
     * 微信小程序发票是否使用(0=未使用,1=已使用)
     */
    private String isUse;

    /**
     * 微信小程序发票是否使用(0=未使用,1=已使用)
     */
    private String userId;

    /**
     * 识别信息文件ID
     */
    @TableField(exist = false)
    private List<String> cutFileId;

    /**
     * OCR信息
     */
    @TableField(exist = false)
    private BaseEntity dataOcr;
}
