package org.smartlink.server.nc.ocr.service.yesfp.request;

import lombok.*;

/**
 * <p>Title: pdfFiles </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfFiles {

    /**
     * 来源业务系统
     */
    private String srcBillType;
    /**
     * 业务系统单据号
     *
     */
    private String srcBillCode;
    /**
     * 发票文件名
     *
     */
    private String fileName;
    /**
     * 对应发票文件 Base64编码字节流
     *
     */
    private String content;
    /**
     * 设置发票分类（只支持增值税发票）,为空自动解析；目前支持：”交通”,”餐饮”,”通讯”,”其他”；
     */
    private String classification;
}
