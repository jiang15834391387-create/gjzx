package org.smartlink.common.core.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.business.domain.DataFlightsItineraryDetail;

import java.io.Serial;
import java.io.Serializable;


/**
 * 航空电子行程单明细视图对象 data_flights_itinerary_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataFlightsItineraryDetail.class)
public class DataFlightsItineraryDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 承运人
     */
    @ExcelProperty(value = "承运人")
    private String carrier;

    /**
     * 乘机日期
     */
    @ExcelProperty(value = "乘机日期")
    private String invoiceDate;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    private String fileId;

    /**
     * 航班号
     */
    @ExcelProperty(value = "航班号")
    private String flightNumber;

    /**
     * 出发站
     */
    @ExcelProperty(value = "出发站")
    private String stationGetOn;

    /**
     * 座位等级
     */
    @ExcelProperty(value = "座位等级")
    private String seat;

    /**
     * 客票生效日期
     */
    @ExcelProperty(value = "客票生效日期")
    private String effectiveDate;

    /**
     * 有效截至日期
     */
    @ExcelProperty(value = "有效截至日期")
    private String expiryDate;

    /**
     * 主表id
     */
    @ExcelProperty(value = "主表id")
    private String ocrId;

    /**
     * 舱位等级
     */
    @ExcelProperty(value = "舱位等级")
    private String spaceLevel;

    /**
     * 免费行李
     */
    @ExcelProperty(value = "免费行李")
    private String allow;

    /**
     * 客票级别
     */
    @ExcelProperty(value = "客票级别")
    private String fareBasis;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String userId;

    /**
     * 乘机人姓名
     */
    @ExcelProperty(value = "乘机人姓名")
    private String userName;

    /**
     * 乘机时间
     */
    @ExcelProperty(value = "乘机时间")
    private String invoiceTime;

    /**
     * 到达站
     */
    @ExcelProperty(value = "到达站")
    private String stationGetOff;

    /**
     * flight_segment航段序号
     */
    @ExcelProperty(value = "flight_segment航段序号")
    private String flightSegment;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
