package org.smartlink.web.ncc.nccstand.request.taxinvoice;

import lombok.Data;

import java.util.List;

/**
 * @description: 发票推送台账请求类
 * @author: L
 * @create:
 **/
@Data
public class StandTaxInvoiceRequest {

    private String userid;

    //private String pk_org;

    private String orgCode;

    private String datasource;

    private String factorycode;

    private String billtype;

    private String transitype;

    private List<StandTaxInvoiceRequestData> data;

}
