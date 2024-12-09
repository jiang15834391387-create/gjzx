package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <p>Title: Invoice </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class Invoice {


    // 0-未查验；1-已查验
    @JsonProperty("thirdVerifyStatus")
    private String thirdVerifyStatus;
    /**
     * 备注
     */
    @JsonProperty("bz")
    private String bz;
    /**
     * 发票分类
     */
    @JsonProperty("classification")
    private String classification;
    /**
     * 复核人
     */
    @JsonProperty("fhr")
    private String fhr;
    /**
     * 发票代码
     */
    @JsonProperty("fpDm")
    private String fpDm;
    /**
     * 发票号码
     */
    @JsonProperty("fpHm")
    private String fpHm;
    /**
     * 发票密文
     */
    @JsonProperty("fpMw")
    private String fpMw;
    /**
     * 发票介质 0-电子发票；1-纸质发票
     */
    @JsonProperty("fpjz")
    private String fpjz;
    /**
     * 发票类型 发票类型1增值税电子普通发票;3增值税普通发票;4增值税专用发票;5机动车销售统一发票;6货物运输业增值税专用发票;14通行费发票;8成品油电子发票
     */
    @JsonProperty("fplx")
    private String fplx;
    /**
     * 购买方地址电话
     */
    @JsonProperty("gmfDzdh")
    private String gmfDzdh;
    /**
     * 购买方名称
     */
    @JsonProperty("gmfMc")
    private String gmfMc;
    /**
     * 购买方纳税人识别号
     */
    @JsonProperty("gmfNsrsbh")
    private String gmfNsrsbh;
    /**
     * 购买方银行账号
     */
    @JsonProperty("gmfYhzh")
    private String gmfYhzh;
    /**
     * 合计金额
     */
    @JsonProperty("hjje")
    private Double hjje;
    /**
     * 合计税额
     */
    @JsonProperty("hjse")
    private Double hjse;
    /**
     * 税控设备编号
     */
    @JsonProperty("jqbh")
    private String jqbh;
    /**
     * 价税合计
     */
    @JsonProperty("jshj")
    @JsonSerialize(using = ToStringSerializer.class)
    private Double jshj;
    /**
     * 校验码
     */
    @JsonProperty("jym")
    private String jym;
    /**
     * 开票类型 0-蓝字发票;1-红字发票
     */
    @JsonProperty("kplx")
    private String kplx;
    /**
     * 开票人
     */
    @JsonProperty("kpr")
    private String kpr;
    /**
     * 开票日期
     */
    @JsonProperty("kprq")
    private String kprq;
    /**
     * 零税率标志(通行费发票用) 空-非零税率 1-免税 2-不征收 3-零税率
     */
    @JsonProperty("lslbz")
    private Object lslbz;
    /**
     * 农产品收购标志	2=农产品收购
     */
    @JsonProperty("sgbz")
    private Object sgbz;
    /**
     * 收款人
     */
    @JsonProperty("skr")
    private String skr;
    /**
     * 来源单据号
     */
    @JsonProperty("srcBillCode")
    private String srcBillCode;
    /**
     * 来源系统
     */
    @JsonProperty("srcBillType")
    private String srcBillType;
    @JsonProperty("filepath")
    private String filepath;
    /**
     * 主管税务机关代码(机动车用)
     */
    @JsonProperty("swjgdm")
    private String swjgdm;
    /**
     * 主管税务机关名称(机动车用)
     */
    @JsonProperty("swjgmc")
    private String swjgmc;
    /**
     * 通行费标志(通行费发票用)	06-可抵扣通行费 07-不可抵扣同行费
     */
    @JsonProperty("txfbz")
    private Object txfbz;
    @JsonProperty("wspzh")
    private String wspzh;
    /**
     * 销售方地址电话
     */
    @JsonProperty("xsfDzdh")
    private String xsfDzdh;
    /**
     * 销售方名称
     */
    @JsonProperty("xsfMc")
    private String xsfMc;
    /**
     * 销售方纳税人识别号
     */
    @JsonProperty("xsfNsrsbh")
    private String xsfNsrsbh;
    /**
     * 销售方银行账号
     */
    @JsonProperty("xsfYhzh")
    private String xsfYhzh;
    /**
     * 原发票代码	红字发票填写原蓝票代码
     */
    @JsonProperty("yfpDm")
    private String yfpDm;
    /**
     * 原发票号码	红字发票填写原蓝票号码
     */
    @JsonProperty("yfpHm")
    private String yfpHm;

    @JsonProperty("items")
    private List<Items> items;
    /**
     * 开票日期
     */
    @JsonProperty("date")
    private String date;
    /**
     * 消费类型
     */
    @JsonProperty("kind")
    private String kind;
    @JsonProperty("filePath")
    private Object filePath;
    /**
     * 销方名称
     */
    @JsonProperty("sellerName")
    private String sellerName;
    /**
     * 购方名称
     */
    @JsonProperty("buyerName")
    private String buyerName;
    /**
     * 发票代码
     */
    @JsonProperty("invoiceCode")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @JsonProperty("invoiceNum")
    private String invoiceNum;
    /**
     * 购方税号
     */
    @JsonProperty("buyerTaxId")
    private String buyerTaxId;
    /**
     * 校验码
     */
    @JsonProperty("checkCode")
    private String checkCode;
    /**
     * 合计金额
     */
    @JsonProperty("totalAmount")
    private Double totalAmount;
    /**
     * 报销状态
     */
    @JsonProperty("purchaserStatus")
    private Object purchaserStatus;
    /**
     * 销方税号
     */
    @JsonProperty("sellerTaxId")
    private String sellerTaxId;

    //----飞机行程单
    /**
     * 发票主键
     */
    @JsonProperty("id")
    private Object id;
    /**
     * 开票时间
     */
    @JsonProperty("time")
    private String time;
    /**
     * 种类
     */
    @JsonProperty("category")
    private String category;
    /**
     * 金额
     */
    @JsonProperty("amount")
    private Double amount;
    /**
     * 票价
     */
    @JsonProperty("fare")
    private Double fare;
    /**
     * 税费
     */
    @JsonProperty("tax")
    private Double tax;
    /**
     * 销售单位代码
     */
    @JsonProperty("agentCode")
    private String agentCode;
    /**
     * 填开单位
     */
    @JsonProperty("issueBy")
    private String issueBy;
    /**
     * 乘机人姓名
     */
    @JsonProperty("userName")
    private String userName;
    /**
     * 身份证号
     */
    @JsonProperty("userId")
    private String userId;
    /**
     * 民航发展基金
     */
    @JsonProperty("caacDevelopFund")
    private Double caacDevelopFund;
    /**
     * 保险费
     */
    @JsonProperty("insurance")
    private Double insurance;
    /**
     * 金额（不含税）
     */
    @JsonProperty("feeWithoutTax")
    private Double feeWithoutTax;
    /**
     * 税额
     */
    @JsonProperty("taxAmount")
    private Double taxAmount;

    /**
     * 电子客票号码
     */
    @JsonProperty("ticketNum")
    private String ticketNum;

    @JsonProperty("itemList")
    private List<ItemList> itemList;
    /**
     * 燃油附加费
     */
    @JsonProperty("fuelSurcharge")
    private Double fuelSurcharge;
    //----火车票
    /**
     * 始发站
     */
    @JsonProperty("origin")
    private String origin;
    /**
     * 终点站
     */
    @JsonProperty("destination")
    private String destination;
    /**
     * 坐席
     */
    @JsonProperty("level")
    private String level;
    /**
     * 号码
     */
    @JsonProperty("number")
    private String number;
    /**
     * 车次
     */
    @JsonProperty("trainNum")
    private String trainNum;
    /**
     * 姓名
     */
    @JsonProperty("name")
    private String name;
    /**
     * 座位号
     */
    @JsonProperty("seatNo")
    private String seatNo;
    //----出租车
    /**
     * 上车时间
     */
    @JsonProperty("startTime")
    private String startTime;
    /**
     * 下车时间
     */
    @JsonProperty("endTime")
    private String endTime;
    /**
     * 发票所在地
     */
    @JsonProperty("place")
    private String place;
    /**
     * 车牌号
     */
    @JsonProperty("carNum")
    private String carNum;
    /**
     * 里程
     */
    @JsonProperty("mileage")
    private Double mileage;


    //----过路费
    /**
     * 出口
     */
    @JsonProperty("exit")
    private String exit;
    /**
     * 入口
     */
    @JsonProperty("entrance")
    private String entrance;


}
