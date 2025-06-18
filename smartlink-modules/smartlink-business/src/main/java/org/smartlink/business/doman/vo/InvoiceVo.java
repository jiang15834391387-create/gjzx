package org.smartlink.business.doman.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.math.BigDecimal;

/**
 * 发票夹列表对象
 */
@Data
public class InvoiceVo {
    private String id;//发票唯一标识
    private String fileId;//文件唯一标识
    private String sellerName; // 销售方名称
    private String buyerName; //购买方名称 //买方单位/个人
    private String invoiceDate;//开票日期//发票日期 日期//填开日期
    private String invoiceType; // 发票种类
    private String message; // 文件信息，上传成功、修改成功
    private String checkStatus; // 查验结果，如成功2、失败3
    private String status;//文件状态 ,待报销、已报销、已删除
    private String grossAmount;//所有发票总计金额
    private String consumptionCompanyName;//消费使用单位-名称
    private String contractNumber;//合同协议号
    private String customsNumber;//海关编号//报关单编号
    private String dateOfApplication;//申报日期
    private String phone;//行程人手机号
    private String invoiceTotal;//总计//价税合计 //金额合计//合计金额//合计//费用合计
    private String timeGetOn;//行程开始时间
    private String invoiceNumber;//发票号码 //票据号码
    private String shipper;//托运人名称
    private String totalPrice;//费用合计
    private String electronicReceiptNumber;//电子收款凭证号
    private String date;//申请日期
    private String userName;//旅客姓名
    private String payer;//交款人
    private String paymentCode;//缴款码
    private String visitDate;//就诊日期
    private String name;//姓名
    private String invoiceCode;//发票代码
    private String city;//城市
    private String trainNumber;//车次号
    private String storeName;//店名
    private String invoiceTime;//时间
    private String companyName;//缴款单位(人)公司名称
    private String provinces;//省/市
    private DataImageFilesInfo filesInfo;//图片对象
    @JsonIgnore
    public BigDecimal getMoneyAsBigDecimal() {
        if (invoiceTotal == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(invoiceTotal);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
