package org.smartlink.web.ncc.nccor.response.taxinvoice;

import lombok.Data;

/**
 * @description: 电子发票返回data
 * @author: L
 * @create:
 **/
@Data
public class UploadElectronicInvoiceNonStandardResponseData {

    private String code;

    private String msg;

    private String fpdm;

    private String fphm;
    //组织，集团
    private String pk_group;
    //购货单位开户行及账号
    private String gmfyhzh;
    //发票主键
    private String pk_invoice;
    //购买方名称
    private String gmfmc;
    //发票分类 1=采购发票; 2=费用发票; 3=其他;
    private String classtype;
    //不含税金额
    private String hjje;
    //交易类型编码
    private String billtypecode;
    //
    private String dr;
    //收票组织
    private String pk_org;
    //销售方地址电话
    private String xsfdzdh;
    //开票日期
    private String kprq;
    //发票类型
    // 1=增值税电子普通发票;
    //2=增值税电子专用发票;
    //3=增值税普通发票;
    //4=增值税专用发票;
    //8=增值税电子普通发票（成品油）;
    //9=成品油普通发票(卷式);
    //10=成品油普通发票;
    //11=成品油专用发票;
    //12=增值税普通发票(卷式);
    //14=通行费增值税电子普通发票;
    //15=机打发票;
    //16=火车票;
    //17=航空客票;
    //18=客运汽车票;
    //19=出租车票;
    //20=过路费票;
    //21=定额发票;
    private String invoice_type;
    //销售方纳税人识别号
    private String xsfnsrsbh;
    //税额
    private String hjse;
    //机器编号
    private String jjm;
    //纳税人识别号
    private String gmfnsrsbh;
    //红字发票
    private String red_flag;
    //销售方名称
    private String xsfmc;
    //购买方地址电话
    private String gmfdzdh;
    //销售方开户行及账号
    private String xsfyhzh;
    //价税合计
    private String jshj;
    //校验码
    private String jym;

}
