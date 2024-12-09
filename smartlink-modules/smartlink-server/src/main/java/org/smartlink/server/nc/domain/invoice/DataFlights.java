package org.smartlink.server.nc.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.util.Date;

/**
 * 航空电子行程单明细对象 data_flights
 *
 * @author L
 * @date
 */
@Data
@TableName("data_flights")
public class DataFlights extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 承运人
     */
    @FieldName(value="承运人")
    private String carrier;
    /**
     * 乘机日期
     */
    @FieldName(value="乘机日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 图片表主键
     */
    @FieldName(value="图片表主键")
    private String fileId;
    /**
     * 航班号
     */
    @FieldName(value="航班号")
    private String flightNumber;
    /**
     * 出发站
     */
    @FieldName(value="出发站")
    private String stationGetOn;
    /**
     * 座位等级
     */
    @FieldName(value="座位等级")
    private String seat;
    /**
     * 客票生效日期
     */
    @FieldName(value="客票生效日期")
    private Date effectiveDate;
    /**
     * 有效截至日期
     */
    @FieldName(value="有效截至日期")
    private Date expiryDate;
    /**
     * 舱位等级
     */
    @FieldName(value="舱位等级")
    private String spaceLevel;
    /**
     * 免费行李
     */
    @FieldName(value="免费行李")
    private String allow;
    /**
     * 客票级别
     */
    @FieldName(value="客票级别")
    private String fareBasis;
    /**
     * 身份证号
     */
    @FieldName(value="身份证号")
    private String userId;
    /**
     * 乘机人姓名
     */
    @FieldName(value="乘机人姓名")
    private String userName;
    /**
     * 乘机时间
     */
    @FieldName(value="乘机时间")
    private String invoiceTime;
    /**
     * 到达站
     */
    @FieldName(value="到达站")
    private String stationGetOff;
    /**
     * 税务云token
     */
    @FieldName(value="税务云token")
    private String saveToken;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @FieldName(value="是否删除标识")
    private String deleteFlag;
    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;
    /**
     * 置信度
     */
    @FieldName(value="置信度")
    private String confidence;

    /**
     *  ocr表id
     */
    @FieldName(value = "ocr表id")
    private String ocrId;

}
