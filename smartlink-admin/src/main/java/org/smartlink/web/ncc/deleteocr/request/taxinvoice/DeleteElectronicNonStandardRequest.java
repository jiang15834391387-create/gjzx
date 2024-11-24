package org.smartlink.web.ncc.deleteocr.request.taxinvoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 非标准删除电子发票台账请求类
 * @author: ChenJiangHong
 * @create: 2023-01-10 18:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteElectronicNonStandardRequest {

    private String userid;

    private String pk_org;//组织PK,组织PK、组织编码至少传入一项

    private String fpDm;

    private String fpHm;

    private String orgCode;

    private String nsrsbh;

    private String  datasource;

    private String factorycode;

    private String billtype;

    private String transitype;

    private DeleteElectronicNonStandardRequestData data;

}
