package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: ItemList </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class ItemList {
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
