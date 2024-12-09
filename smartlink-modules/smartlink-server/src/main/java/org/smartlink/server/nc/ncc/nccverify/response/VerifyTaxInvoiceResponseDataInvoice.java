package org.smartlink.server.nc.ncc.nccverify.response;

import lombok.Data;

import java.util.List;

/**
 * @description: 查验返回发票信息
 * @author: L
 * @create:
 **/
@Data
public class VerifyTaxInvoiceResponseDataInvoice {

    private String bz;

    private String fhr;

    private String fpDm;

    private String fpHm;

    private String fpMw;

    private String fplx;

    private String fpjz;

    private String zfbz;

    private String gmfDzdh;

    private String gmfMc;

    private String gmfNsrsbh;

    private String gmfYhzh;

    private String hjje;

    private String hjse;

    private String jqbh;

    private String jshj;

    private String jym;

    private String kpr;

    private String kprq;

    private String skr;

    private String xsfDzdh;

    private String xsfMc;

    private String xsfNsrsbh;

    private String xsfYhzh;

    private List<VerifyDataItem> items;

}
