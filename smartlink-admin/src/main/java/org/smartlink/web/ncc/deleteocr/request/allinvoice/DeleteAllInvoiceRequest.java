package org.smartlink.web.ncc.deleteocr.request.allinvoice;

import lombok.Data;

/**
 * @author chenJiangHong
 * @Description 删除增值税发票信息请求类
 * @create: 2022-07-29 16:50
 */
@Data
public class DeleteAllInvoiceRequest {

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
     * 流水号
     */
    private String billid;
    /**
     * data
     */
    private DeleteAllInvoiceData data;

}
