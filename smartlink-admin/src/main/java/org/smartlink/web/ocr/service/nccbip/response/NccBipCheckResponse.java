package org.smartlink.web.ocr.service.nccbip.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author L
 * @title 查验返回结果
 * @description 查验返回结果
 * @date
 */
@NoArgsConstructor
@Data
public class NccBipCheckResponse {
    /**
     * 接口状态码
     */
    @JSONField(name="code")
    private String code;
    /**
     * 接口状态说明
     */
    @JSONField(name="message")
    private String msg;

    /**
     * BIP使用
     */
    @JSONField(name="data")
    private List<DatasDTO> dataBip;

    @NoArgsConstructor
    @Data
    public static class DatasDTO {
        /**
         * 业务状态吗
         */
        @JSONField(name="code")
        private String code;
        /**
         * 业务状态说明
         */
        @JSONField(name="msg")
        private String msg;
        /**
         * 发票代码
         */
        @JSONField(name="fpdm")
        private String fpDm;
        /**
         * 发票号码
         */
        @JSONField(name="fphm")
        private String fpHm;
        /**
         * 发票保存token；1张发票对应1个saveToken。此token有效期为10分钟，超过时长此token失效，需要重新查验。token只能使用一次。
         */
        @JSONField(name="saveToken")
        private String saveToken;
        @JSONField(name="invoice")
        private InvoiceDTO invoice;

        @NoArgsConstructor
        @Data
        public static class InvoiceDTO {
            @JSONField(name="bz")
            private String bz;
            /**
             * 复核人
             */
            @JSONField(name="fhr")
            private String fhr;
            @JSONField(name="fpdm")
            private String fpDm;
            @JSONField(name="fphm")
            private String fpHm;
            /**
             * 发票密文
             */
            @JSONField(name="fpMw")
            private Object fpMw;
            /**
             * 1:增值税电子普通发票；3:增值税普通发票；4:增值税专用发票
             */
            @JSONField(name="fplx")
            private String fplx;
            /**
             * 0:电子发票1:纸质发票
             */
            @JSONField(name="fpjz")
            private String fpjz;
            //是否红冲
            @JSONField(name="bred")
            private String bred;
            @JSONField(name="zfbz")
            private String zfbz;
            @JSONField(name="gmfDzdh")
            private String gmfDzdh;
            @JSONField(name="gmfMc")
            private String gmfMc;
            @JSONField(name="gmfNsrsbh")
            private String gmfNsrsbh;
            @JSONField(name="gmfYhzh")
            private String gmfYhzh;
            @JSONField(name="hjje")
            private Double hjje;
            @JSONField(name="hjse")
            private Double hjse;
            @JSONField(name="jqbh")
            private String jqbh;
            @JSONField(name="jshj")
            private Double jshj;
            @JSONField(name="jym")
            private String jym;
            @JSONField(name="kpr")
            private String kpr;
            @JSONField(name="kprq")
            private String kprq;
            @JSONField(name="skr")
            private String skr;
            @JSONField(name="xsfDzdh")
            private String xsfDzdh;
            @JSONField(name="xsfMc")
            private String xsfMc;
            @JSONField(name="xsfNsrsbh")
            private String xsfNsrsbh;
            @JSONField(name="xsfYhzh")
            private String xsfYhzh;
            @JSONField(name="submitter")
            private String submitter;
            @JSONField(name="hasExist")
            private Boolean hasExist;
            @JSONField(name="swjgmc")
            private String swjgmc;
            @JSONField(name="swjgdm")
            private String swjgdm;
            @JSONField(name="items")
            private List<ItemsDTO> items;

            @NoArgsConstructor
            @Data
            public static class ItemsDTO {
                @JSONField(name="dw")
                private String dw;
                @JSONField(name="ggxh")
                private String ggxh;
                @JSONField(name="se")
                private Double se;
                @JSONField(name="sl")
                private Integer sl;
                @JSONField(name="xmdj")
                private Double xmdj;
                @JSONField(name="xmje")
                private Double xmje;
                @JSONField(name="xmmc")
                private String xmmc;
                @JSONField(name="xmsl")
                private Integer xmsl;
                @JSONField(name="xmjshj")
                private Object xmjshj;
                @JsonProperty("detailMotor")
                private NccBipCheckResponse.DatasDTO.InvoiceDTO.ItemsDTO.DetailMotorDTO detailMotor;

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
        }
    }
}
