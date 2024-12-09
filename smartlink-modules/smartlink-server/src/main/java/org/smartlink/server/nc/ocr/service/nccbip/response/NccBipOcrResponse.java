package org.smartlink.server.nc.ocr.service.nccbip.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author L
 * @title 租户识别请求返回
 * @description 租户识别请求返回
 * @date
 */
@NoArgsConstructor
@Data
public class NccBipOcrResponse {
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private OneDataDTO data;

    @NoArgsConstructor
    @Data
    public static class OneDataDTO {
        @JsonProperty("code")
        private String code;
        @JsonProperty("datas")
        private List<DatasDTO> datas;
        @JsonProperty("msg")
        private String msg;

        @NoArgsConstructor
        @Data
        public static class DatasDTO {
            /**
             * 图片ID
             */
            @JsonProperty("imageId")
            private String imageId;
            @JsonProperty("region")
            private Integer[] region;
            @JsonProperty("data")
            private DataDTO data;
            @JsonProperty("imagePath")
            private Object imagePath;
            /**
             * 票种类型
             * invoice	1	增值税发票
             * tolls	6	过路费
             * train	3	火车票
             * quota	5	定额发票
             * machine	4	机打发票
             * taxi	2	出租车发票
             * passenger	7	客运发票
             * air	8	航空电子行程单
             * other	9	其他发票
             * nontax	12	财政非税票据	票据，非发票，发票场景不需考虑
             */
            @JsonProperty("billType")
            private String billType;

            /**
             * BIP token校验码
             */
            @JsonProperty("imgOcrToken")
            private String imgOcrToken;

            @NoArgsConstructor
            @Data
            public static class DataDTO {

                /**
                 * BIP token校验码
                 */
                @JsonProperty("imgOcrToken")
                private String imgOcrToken;
                //----增票
                /**
                 * 0-未查验；1-已查验
                 */
                @JsonProperty("thirdVerifyStatus")
                private Integer thirdVerifyStatus;
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
                @JsonProperty("fpdm")
                @JSONField(name = "fpdm")
                private String fpDm;
                /**
                 * 发票号码
                 */
                @JsonProperty("fphm")
                @JSONField(name = "fphm")
                private String fpHm;
                /**
                 * 数电票号码
                 */
                @JsonProperty("electronicNumber")
                @JSONField(name = "electronicNumber")
                private String electronicNumber;
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
                private List<ItemsDTO> items;
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
                /**
                 * 省份
                 */
                @JsonProperty("province")
                private String province;
                /**
                 * 城市
                 */
                @JsonProperty("city")
                private String city;


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
                private String insurance;
                /**
                 * 类型
                 */
                @JsonProperty("airportType")
                private String airportType;
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
                private List<ItemListDTO> itemList;
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
                /**
                 * 身份证号
                 */
                @JsonProperty("idNumber")
                private String idNumber;
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
                //海关进口增值税专用缴款书
                @JsonProperty("corporate_account_no")
                private String corporateAccountNo;
                @JsonProperty("contract_no")
                private String contractNo;
                @JsonProperty("corporate_bank")
                private String corporateBank;
                @JsonProperty("customs_name")
                private String customsName;
                @JsonProperty("subject")
                private String subject;
                @JsonProperty("revenue_sys")
                private String revenueSys;
                @JsonProperty("revenue_org")
                private String revenueOrg;
                @JsonProperty("currency_code")
                private String currencyCode;
                @JsonProperty("load_bill_no")
                private String loadBillNo;
                @JsonProperty("customs_no")
                private String customsNo;
                @JsonProperty("corporate_name")
                private String corporateName;
                @JsonProperty("invoiceType")
                private String invoiceType;
                @JsonProperty("customs_bill_no")
                private String customsBillNo;
                /**
                 * 合计金额
                 */
                @JsonProperty("total")
                private String total;
                /**
                 * 	金额（大写）
                 */
                @JsonProperty("total_cn")
                private String totalCn;

                /**
                 * 增票明细
                 */
                @NoArgsConstructor
                @Data
                public static class ItemsDTO {
                    /**
                     * 单位
                     */
                    @JsonProperty("dw")
                    private String dw;
                    /**
                     * 规格型号
                     */
                    @JsonProperty("ggxh")
                    private String ggxh;
                    /**
                     * 税额
                     */
                    @JsonProperty("se")
                    private Double se;
                    /**
                     * 税率
                     */
                    @JsonProperty("sl")
                    private Double sl;
                    /**
                     * 车牌号(通行费发票)
                     */
                    @JsonProperty("txfCph")
                    private String txfCph;
                    /**
                     * 类型(通行费发票)
                     */
                    @JsonProperty("txfLx")
                    private String txfLx;
                    /**
                     * 通行日期起(通行费发票)
                     */
                    @JsonProperty("txfTxrqq")
                    private String txfTxrqq;
                    /**
                     * 通行日期止(通行费发票)
                     */
                    @JsonProperty("txfTxrqz")
                    private String txfTxrqz;
                    /**
                     * 单价
                     */
                    @JsonProperty("xmdj")
                    private Double xmdj;
                    /**
                     * 金额
                     */
                    @JsonProperty("xmje")
                    private Double xmje;
                    /**
                     * 价税合计
                     */
                    @JsonProperty("xmjshj")
                    private Double xmjshj;
                    /**
                     * 项目名称
                     */
                    @JsonProperty("xmmc")
                    private String xmmc;
                    /**
                     * 项目数量
                     */
                    @JsonProperty("xmsl")
                    private Double xmsl;



                    @JsonProperty("detailMotor")
                    private DetailMotorDTO detailMotor;

                    @NoArgsConstructor
                    @Data
                    public static class DetailMotorDTO {
                        /**
                         * 产地
                         */
                        @JsonProperty("cd")
                        private String cd;
                        /**
                         * 转入地车辆车管所名称
                         */
                        @JsonProperty("cgsmc")
                        private String cgsmc;
                        /**
                         * 车架号码
                         */
                        @JsonProperty("cjhm")
                        private String cjhm;
                        /**
                         * 车辆类型
                         */
                        @JsonProperty("cllx")
                        private String cllx;
                        /**
                         * 厂牌型号
                         */
                        @JsonProperty("cpxh")
                        private String cpxh;
                        /**
                         * 车牌
                         */
                        @JsonProperty("cpzh")
                        private String cpzh;
                        /**
                         * 登记证号
                         */
                        @JsonProperty("djzh")
                        private String djzh;
                        /**
                         * 吨位
                         */
                        @JsonProperty("dunwei")
                        private String dunwei;
                        /**
                         * 发动机号码
                         */
                        @JsonProperty("fdjhm")
                        private String fdjhm;
                        /**
                         * 买方电话
                         */
                        @JsonProperty("gfdh")
                        private String gfdh;
                        /**
                         * 买方单位/个人住址
                         */
                        @JsonProperty("gfdz")
                        private String gfdz;
                        /**
                         * 合格证号
                         */
                        @JsonProperty("hgzh")
                        private String hgzh;
                        /**
                         * 进口证明书号
                         */
                        @JsonProperty("jkzmsh")
                        private String jkzmsh;

                        @JsonProperty("jydh")
                        private String jydh;
                        /**
                         * 经营、拍卖单位
                         */
                        @JsonProperty("jydw")
                        private String jydw;
                        /**
                         * 经营、拍卖单位地址
                         */
                        @JsonProperty("jydz")
                        private String jydz;
                        /**
                         * 经营、拍卖单位纳税人识别号
                         */
                        @JsonProperty("jysbh")
                        private String jysbh;
                        /**
                         * 开户银行及账号
                         */
                        @JsonProperty("jyyhzh")
                        private String jyyhzh;
                        /**
                         * 二手车市场电话
                         */
                        @JsonProperty("scdh")
                        private String scdh;
                        /**
                         * 二手车市场地址
                         */
                        @JsonProperty("scdz")
                        private String scdz;
                        /**
                         * 二手车市场
                         */
                        @JsonProperty("scmc")
                        private String scmc;
                        /**
                         * 生产企业名称
                         */
                        @JsonProperty("scqymc")
                        private String scqymc;
                        /**
                         * 二手车市场纳税人识别号
                         */
                        @JsonProperty("scsbh")
                        private String scsbh;
                        /**
                         * 二手车市场开户银行及账号
                         */
                        @JsonProperty("scyhzh")
                        private String scyhzh;
                        /**
                         * 身份证号码/组织机构代码
                         */
                        @JsonProperty("sfzhm")
                        private String sfzhm;
                        /**
                         * 商检单号
                         */
                        @JsonProperty("sjdh")
                        private String sjdh;
                        /**
                         * 主管税务机关代码
                         */
                        @JsonProperty("swjgdm")
                        private String swjgdm;
                        /**
                         * 主管税务机关名称
                         */
                        @JsonProperty("swjgmc")
                        private String swjgmc;
                        /**
                         * 完税凭证号
                         */
                        @JsonProperty("wspzh")
                        private String wspzh;
                        /**
                         * 限乘人数
                         */
                        @JsonProperty("xcrs")
                        private String xcrs;
                        /**
                         * 卖方电话
                         */
                        @JsonProperty("xfdh")
                        private String xfdh;
                        /**
                         * 卖方单位/个人
                         */
                        @JsonProperty("xfdw")
                        private String xfdw;
                        /**
                         * 卖方单位/个人住址
                         */
                        @JsonProperty("xfdz")
                        private String xfdz;
                        /**
                         * 卖方单位代码/身份证号
                         */
                        @JsonProperty("xfhm")
                        private String xfhm;
                    }
                }

                /**
                 * 行程单明细
                 */
                @NoArgsConstructor
                @Data
                public static class ItemListDTO {
                    /**
                     * 行程单明细ID
                     */
                    @JsonProperty("id")
                    private String id;
                    /**
                     * 行程单主表ID
                     */
                    @JsonProperty("airId")
                    private String airId;
                    /**
                     * 乘机日期
                     */
                    @JsonProperty("date")
                    private String date;
                    /**
                     * 仓位
                     */
                    @JsonProperty("seat")
                    private String seat;
                    /**
                     * 承运人
                     */
                    @JsonProperty("carrier")
                    private String carrier;
                    /**
                     * 出发
                     */
                    @JsonProperty("from")
                    private String from;
                    /**
                     * 乘机时间
                     */
                    @JsonProperty("time")
                    private String time;
                    /**
                     * 到达
                     */
                    @JsonProperty("to")
                    private String to;
                    /**
                     * 航班号
                     */
                    @JsonProperty("flightNumber")
                    private String flightNumber;
                }
            }
        }
    }
}
