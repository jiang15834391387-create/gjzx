package org.smartlink.server.nc.domain.invoice.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 发票手动查验参数DTO
 *
 * @author 马旭辉
 * @date 11/30
 */
@Data
public class ManuallyCheckInvoiceDTO implements Serializable {

    /**
     * 发票类型
     */
    @NotBlank
    private String invoiceType;
    /**
     * 文件id
     */
    @NotBlank
    private String fileId;
    /**
     * OCR主键
     */
    @NotBlank
    private String id;
    /**
     * 合计金额（税前）
     */
    @NotBlank
    private String sumAmount;
    /**
     * 价税合计
     */
    private String invoiceTotal;
    /**
     * 发票号码
     */
    @NotBlank
    private String invoiceNumber;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 开票日期
     */
    @NotNull
    private String invoiceDate;
    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 发票来源 若等于wechat 则代表是小程序上传的发票
     */
    private String invoiceSource;


    private String businessSerialNo;

    private String pushBusinessInfoFlag;

}
