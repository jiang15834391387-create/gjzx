package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 航空电子行程单明细对象 data_flights_itinerary_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_flights_itinerary_detail")
public class DataFlightsItineraryDetail extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 承运人
     */
    private String carrier;

    /**
     * 乘机日期
     */
    private String invoiceDate;

    /**
     * 图片表主键
     */
    private String fileId;

    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 出发站
     */
    private String stationGetOn;

    /**
     * 座位等级
     */
    private String seat;

    /**
     * 客票生效日期
     */
    private String effectiveDate;

    /**
     * 有效截至日期
     */
    private String expiryDate;

    /**
     * 主表id
     */
    private String ocrId;

    /**
     * 舱位等级
     */
    private String spaceLevel;

    /**
     * 免费行李
     */
    private String allow;

    /**
     * 客票级别
     */
    private String fareBasis;

    /**
     * 身份证号
     */
    private String userId;

    /**
     * 乘机人姓名
     */
    private String userName;

    /**
     * 乘机时间
     */
    private String invoiceTime;

    /**
     * 到达站
     */
    private String stationGetOff;

    /**
     * flight_segment航段序号
     */
    private String flightSegment;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
