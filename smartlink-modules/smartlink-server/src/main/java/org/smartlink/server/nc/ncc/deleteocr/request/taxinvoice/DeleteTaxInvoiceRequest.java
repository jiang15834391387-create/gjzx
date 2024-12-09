package org.smartlink.server.nc.ncc.deleteocr.request.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @author L
 * @Description 删除增值税发票信息请求类
 * @create:
 */
@Data
public class DeleteTaxInvoiceRequest {

    /**
     * 制单人id
     */
    private String userid;

    /**
     * 所属组织机构主键
     */
    private String pk_org;

    /**
     * 数据源
     */
    private String datasource;

    /**
     * 厂商编码
     */
    private String factorycode;

    /**
     * 单据类型编码
     */
    private String billtype;

    /**
     * 交易类型编码
     */
    private String transitype;

    /**
     * data
     */
    private List<DeleteTaxInvoiceData> data;

}
