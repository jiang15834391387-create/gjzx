package org.smartlink.server.nc.ncc.nccverify.response;

import lombok.Data;

/**
 * @description: 查验返回发票明细
 * @author: L
 * @create:
 **/
@Data
public class VerifyDataItem {

    //单位
    private String dw;
    //规格型号
    private String ggxh;
    //税额
    private String se;
    //税率
    private String sl;
    //单价
    private String xmdj;
    //金额
    private String xmje;
    //名称
    private String xmmc;
    //数量
    private String xmsl;
    //项目加税合计
    private String xmjshj;

}
