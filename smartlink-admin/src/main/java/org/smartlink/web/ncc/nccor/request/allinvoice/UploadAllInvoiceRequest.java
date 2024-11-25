package org.smartlink.web.ncc.nccor.request.allinvoice;

import lombok.Builder;
import lombok.Data;

/**
 * @description: NCC获取OCR信息接口请求类
 * @author: L
 * @create:
 **/
@Data
@Builder
public class UploadAllInvoiceRequest {

    /**
     * 制单人ID
     */
    private String userid;

    /**
     * 组织机构编码
     */
    private String orgCode;

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
     * 流水号字段
     */
    private String billid;

    /**
     * data
     */
    private UploadAllInvoiceRequestData data;

}
