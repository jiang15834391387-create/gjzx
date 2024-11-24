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
 * 客运汽车票对象 data_passenger_ticket
 *
 * @author L
 * @date
 */
@Data
@TableName("data_passenger_ticket")
public class DataPassengerTicket extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 发票代码
     */
    @FieldName(value="发票代码")
    private String invoiceCode;
    /**
     * 发票日期
     */
    @FieldName(value="发票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
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
     * 乘客身份证号码
     */
    @FieldName(value="乘客身份证号码")
    private String passengerId;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 启始车站
     */
    @FieldName(value="启始车站")
    private String stationGetOff;
    /**
     * 出发车站
     */
    @FieldName(value="出发车站")
    private String stationGetOn;
    /**
     * 时间
     */
    @FieldName(value="时间")
    private String invoiceTime;
    /**
     * 总价
     */
    @FieldName(value="总价")
    private BigDecimal invoiceTotal;
    /**
     * 发票专用章存在性判断
     */
    @FieldName(value="发票专用章存在性判断")
    private String invoiceStamp;
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
