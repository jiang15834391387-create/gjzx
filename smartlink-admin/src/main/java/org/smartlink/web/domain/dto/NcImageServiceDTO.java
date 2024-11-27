package org.smartlink.web.domain.dto;


import lombok.Data;
import org.smartlink.web.domain.DataImageFilesInfo;

/**
 * @description: NC业务参数DTO
 * @author: L
 * @create:
 **/
@Data
public class NcImageServiceDTO {

    /*ncc*/
    /**
     * image对象
     */
    private DataImageFilesInfo dataImageFilesInfo;
    /**
     * 流水号字段
     */
    private String businessSerialNo;
    /**
     * image对应Base64编码
     */
    private String file;
    /**
     * 上传发票业务逻辑类型  1：正常上传发票逻辑  0：手动修改发票逻辑
     */
    private String uploadBusinessType;
    /**
     * 单据编号
     */
    private String barcode;
    /**
     * 请求时间
     */
    private String opTime;
    /**
     * 影像厂商编码
     */
    private String factoryCode;
    /**
     * 单据主键
     */
    private String billId;
    /**
     * 文件信息
     */
    private DataDTO data;
    /**
     * 附件PDF文件流
     */
    private byte[] pByte;

    /**
     * 附件文件流
     */
    private byte[] bytes;

    /**
     * Token
     */
    private String saveToken;
    /**
     * 发票类型
     */
    private String fpLx;
    /**
     * 发票类型
     */
    private String invoiceType;
    @Data
    public static class DataDTO {
        private String file;
    }

}
