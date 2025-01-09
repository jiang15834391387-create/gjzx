package org.smartlink.business.scan.dto;



import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.io.Serializable;

/**
 * 文件上传传递参数用的DTO
 * @author shidunkai
 */
@Data
public class FileUploadDTO implements Serializable {
    //@ApiModelProperty(value = "batchId")
    private String batchId;
    //@ApiModelProperty(value = "Task流水号" ,required = true)
    @NotBlank(message="businessSerialNo为空！")
    private String businessSerialNo;
    //@ApiModelProperty("文件名称")
    @NotBlank(message = "文件名称不能为空")
    private String fileName;
    //@ApiModelProperty("ip地址")
    private String cip;
    //@ApiModelProperty("单据类型")
    private String billType;
    //@ApiModelProperty("是否查验过滤")
    private String checkType;
    //@ApiModelProperty("图片扫描到页面的时间 格式yyyy-MM-dd HH:mm:ss")
    private String pageTime;
    //@ApiModelProperty("1.单扫2.批扫3.用户上传4.H5上传")
    private String scanType;
    //@ApiModelProperty("是否有OCR功能")
    private String isOcr;
    //@ApiModelProperty("是否有查验功能")
    private String isCheck;
    //@ApiModelProperty("系统来源")
    private String systemCode;
    //@ApiModelProperty("是否补扫标识")
    private String supplementaryScan;
    //@ApiModelProperty("批扫单据条码")
    private String barCode;
    //@ApiModelProperty("单据状态")
    private String taskStatus;
    //@ApiModelProperty("组织编号")
    private String orgCode;
    //@ApiModelProperty("小程序上传票夹标识")
    private String folderId;
//    @FieldName("微信小程序发票是否使用(0=未使用,1=已使用)")
    private String isUse;
//    @FieldName("用户ID")
    private String userId;
//    @FieldName("是否是批量上传")
    private Boolean isBatchUpload = false;
    /**
     * 非扫描台连接上传传null
     * */
//    @FieldName("mac地址")
    private String macIp;

//    @FieldName("batchIds")
    private String batchIds;

}
