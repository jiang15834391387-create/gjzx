package org.smartlink.web.ncc.nccor.request.allinvoice;

import lombok.Data;

/**
 * @description: NCC2207上传电子发票请求类
 * @author: ChenJiangHong
 * @create: 2022-09-08 09:56
 **/
@Data
public class UploadElectronicInvoiceRequest {

    /**
     * 数据源
     */
    private String datasource;

    /**
     * 厂商编码
     */
    private String factorycode;

    /**
     * 请求唯一标识
     */
    private String uuid;

    /**
     * 流水号
     */
    private String billid;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 机构主键
     */
    private String pk_org;

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
    private UploadElectronicInvoiceRequestData data;

}
