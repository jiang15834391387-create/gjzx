package org.smartlink.server.nc.ncc.nccverify.request;

import lombok.Data;

import java.util.List;

/**
 * @description: 校验增值税发票请求类
 * @author: L
 * @create:
 **/
@Data
public class VerifyTaxInvoiceRequest {

    private String userid;

    private String orgCode;

    //private String pk_org;

    private String datasource;

    private String factorycode;

    private String billtype;

    private String transitype;

    private List<VerifyTaxInvoiceRequestData> data;

}
