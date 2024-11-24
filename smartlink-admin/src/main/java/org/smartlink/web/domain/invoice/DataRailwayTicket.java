package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 火车票对象 data_railway_ticket
 *
 * @author L
 * @date
 */
@Data
@TableName("data_railway_ticket")
public class DataRailwayTicket extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 日期
     */
    @FieldName(value="日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 图片表id
     */
    @FieldName(value="图片表id")
    private String fileId;
    /**
     * 姓名
     */
    @FieldName(value="姓名")
    private String name;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 座位等级
     */
    @FieldName(value="座位等级")
    private String seat;
    /**
     * 座位号
     */
    @FieldName(value="座位号")
    private String seatNum;
    /**
     * 检票口
     */
    @FieldName(value="检票口")
    private String wicket;
    /**
     * 取票地址
     */
    @FieldName(value="取票地址")
    private String ticketAddress;
    /**
     * 身份证号
     */
    @FieldName(value="身份证号")
    private String idNumber;
    /**
     * 序列号
     */
    @FieldName(value="序列号")
    private String serialNumber;
    /**
     * 到达车站
     */
    @FieldName(value="到达车站")
    private String stationGetOff;
    /**
     * 启始车站
     */
    @FieldName(value="启始车站")
    private String stationGetOn;
    /**
     * 时间
     */
    @FieldName(value="时间")
    private String invoiceTime;
    /**
     * 合计
     */
    @FieldName(value="合计")
    private BigDecimal invoiceTotal;
    /**
     * 发票专用章存在性判断
     */
    @FieldName(value="发票专用章存在性判断")
    private String invoiceStamp;
    /**
     * 车次号
     */
    @FieldName(value="车次号")
    private String trainNumber;
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
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

    /**
     * 推送ncc台账图片id
     */
    @FieldName(value="推送ncc台账图片id")
    private String ncImageId;
    /**
     * 坐标
     */
    @FieldName(value = "坐标")
    private String coordinateStr;
    /**
     * 暂存状态
     */
    @FieldName(value = "暂存状态")
    private String isStaging;
}
