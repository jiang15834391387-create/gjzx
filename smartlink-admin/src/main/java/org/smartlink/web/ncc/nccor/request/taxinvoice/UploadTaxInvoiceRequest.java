package org.smartlink.web.ncc.nccor.request.taxinvoice;

import lombok.Builder;
import lombok.Data;

/**
 * @description: NCC获取OCR信息接口请求类
 * @author: chenJiangHong
 * @create: 2022-06-13 00:30
 **/
@Data
@Builder
public class UploadTaxInvoiceRequest {

    /**
     * 制单人ID
     */
    private String userid;

    /**
     * 组织机构编码 非必传
     */
     private String orgCode;

    /**
     * 组织机构id 必传
     */
    //private String pk_org;

    /**
     * 数据源
     */
    private String datasource;

    /**
     * 厂商编码
     */
    private String factorycode;

    /**
     * 单据类型
     */
    private String billtype;

    /**
     * 交易类型
     */
    private String transitype;

    /**
     * data
     */
    private UploadTaxInvoiceRequestData data;

}
