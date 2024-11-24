package org.smartlink.web.ncc.deleteocr.request.allinvoice;

import lombok.Data;

/**
 * @description: 全票种删除电子发票请求类
 * @author: ChenJiangHong
 * @create: 2022-09-20 23:40
 **/
@Data
public class DeleteElectronicInvoiceRequest {

    private String datasource;

    private String factorycode;

    private String uuid;

    private String billid;

    private String userid;

    private String pk_org;

    private String billtype;

    private String transitype;

    private DeleteElectronicInvoiceData data;

}
