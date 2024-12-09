package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Title: Items </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@NoArgsConstructor
@Data
public class Items {

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
    private DetailMotor detailMotor;

}
