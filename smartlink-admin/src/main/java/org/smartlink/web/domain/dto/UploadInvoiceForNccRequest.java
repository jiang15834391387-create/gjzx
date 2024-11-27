package org.smartlink.web.domain.dto;


import lombok.Builder;
import lombok.Data;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.properties.NccParamProperties;

/**
 * @description: NCC识别逻辑dto
 * @author: L
 * @create:
 **/
@Data
@Builder
public class UploadInvoiceForNccRequest {

    /**
     * 图片文件对象
     */
    private DataImageFilesInfo dataImageFilesInfo;

    /**
     * NCC业务参数对象
     */
    private NccParamProperties paramProperties;

    /**
     * 单据类型
     */
    private String billType;

    /**
     * 交易类型
     */
    private String pkBillType;

    /**
     * 组织机构编码
     */
    private String orgCode;

    /**
     *
     */
    private Boolean nccSCTask;

    /**
     * 是否可读
     */
    private String onlyInvoice;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 流水号
     */
    private String billId;

    /**
     *
     */
    private String pk_org;

    /**
     * image对应Base64
     */
    private String file;

    /**
     * 上传业务类型
     */
    private String uploadBusinessType;

}
