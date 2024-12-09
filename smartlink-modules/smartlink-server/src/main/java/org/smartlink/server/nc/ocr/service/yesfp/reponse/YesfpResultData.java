package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: YesfpData </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class YesfpResultData {

    private String imageId;

    private String imagePath;
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
    private String billType;

    private String exists;

   private Coordinate coordinate;
    /**
     * 发票识别的具体信息, 不同发票类型会不一样
     */
    private Invoice data;
}
