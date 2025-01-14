package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 图片文件视图对象 data_image_files_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataImageFilesInfo.class)
public class DataImageFilesInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    private String fileId;

    /**
     * 父文件ID（如果是子文件，则存储原始文件ID）如果 parent_file_id 为 NULL，表示是原始文件。
     */
    @ExcelProperty(value = "父文件ID", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "如=果是子文件，则存储原始文件ID")
    private String parentFileId;

    /**
     * 业务联查使用
     */
    @ExcelProperty(value = "业务联查使用")
    private String glorityInvestigationNo;

    /**
     * 发票类型
     */
    @ExcelProperty(value = "发票类型")
    private String invoice;

    /**
     * 条形码
     */
    @ExcelProperty(value = "条形码")
    private String barCode;

    /**
     * 二维码
     */
    @ExcelProperty(value = "二维码")
    private String qrCode;

    /**
     * 批次号
     */
    @ExcelProperty(value = "批次号")
    private String batchId;

    /**
     * ocr返回图片id
     */
    @ExcelProperty(value = "ocr返回图片id")
    private String glorityImageId;

    /**
     * 客户端ip
     */
    @ExcelProperty(value = "客户端ip")
    private String cip;

    /**
     * 文件md5
     */
    @ExcelProperty(value = "文件md5")
    private String fileMd5;

    /**
     * 文件名称
     */
    @ExcelProperty(value = "文件名称")
    private String fileName;

    /**
     * 文件大小
     */
    @ExcelProperty(value = "文件大小")
    private String fileSize;

    /**
     * 文件流程状态 0待重扫 1补扫 2重扫完成 3事后补扫文件 4本次重扫未提交
     */
    @ExcelProperty(value = "文件流程状态 0待重扫 1补扫 2重扫完成 3事后补扫文件 4本次重扫未提交")
    private String fileFlowStatus;

    /**
     * 文件类型
     */
    @ExcelProperty(value = "文件类型")
    private String fileType;

    /**
     * 票夹文件信息
     */
    @ExcelProperty(value = "票夹文件信息")
    private String folderId;

    /**
     * 圖片是否保密 0-加密 1-正常
     */
    @ExcelProperty(value = "圖片是否保密 0-加密 1-正常")
    private String imageSecret;

    /**
     * 缩略图文件
     */
    @ExcelProperty(value = "缩略图文件")
    private String surl;

    /**
     * 图片源文件，附件源文件
     */
    @ExcelProperty(value = "图片源文件，附件源文件")
    private String iurl;

    /**
     * pdf文件
     */
    @ExcelProperty(value = "pdf文件")
    private String purl;

    /**
     * 批注文件
     */
    @ExcelProperty(value = "批注文件")
    private String lurl;

    /**
     * 文件信息
     */
    @ExcelProperty(value = "文件信息")
    private String message;

    /**
     * 文档名
     */
    @ExcelProperty(value = "文档名")
    private String documentName;

    /**
     * 查验状态
     */
    @ExcelProperty(value = "查验状态")
    private String checkStatus;

    /**
     * 文件状态
     */
    @ExcelProperty(value = "文件状态")
    private String fileStatus;

    /**
     * 排序字段
     */
    @ExcelProperty(value = "排序字段")
    private String sortValue;

    /**
     * 旋转角度
     */
    @ExcelProperty(value = "旋转角度")
    private String rotateAngle;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 图片扫描到页面的时间
     */
    @ExcelProperty(value = "图片扫描到页面的时间")
    private String pageTime;

    /**
     * 多票据上传包含子发票类型数组
     */
    @ExcelProperty(value = "多票据上传包含子发票类型数组")
    private String includeTypeArr;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
