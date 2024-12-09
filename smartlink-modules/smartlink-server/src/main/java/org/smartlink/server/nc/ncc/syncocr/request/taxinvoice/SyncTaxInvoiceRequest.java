package org.smartlink.server.nc.ncc.syncocr.request.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: NCC同步增值税发票信息接口
 * @author: L
 * @create:
 **/
@Data
public class SyncTaxInvoiceRequest {

    /**
     * 制单人ID
     */
    private String userid;

    /**
     * 组织主键
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
     * 发票信息
     */
    private List<SyncTaxInvoiceRequestData> data;

}
