package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Title: DetailMotor </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@NoArgsConstructor
@Data
public class DetailMotor {

    //车辆类型
    private String cllx;

    //车架号/车辆识别代码
    private String cjhm;

    //发动机号码
    private String fdjhm;

    //厂牌型号
    private String cpxh;

    //产地
    private String cd;

    //合格证号
    private String hgzh;

    //进口证明书号
    private String jkzmsh;

    //商检单号
    private String sjdh;

    //完税凭证号码
    private String wspzh;

    //限乘人数
    private String xcrs;

    //吨位
    private String dunwei;
    /**
     * 主管税务机关名称
     */
    @JsonProperty("swjgmc")
    private String swjgmc;

    /**
     * 转入地车辆车管所名称
     */
    @JsonProperty("cgsmc")
    private String cgsmc;
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
     * 买方电话
     */
    @JsonProperty("gfdh")
    private String gfdh;
    /**
     * 买方单位/个人住址
     */
    @JsonProperty("gfdz")
    private String gfdz;

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
     * 主管税务机关代码
     */
    @JsonProperty("swjgdm")
    private String swjgdm;

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
