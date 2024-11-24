package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 航空电子行程单对象 data_flight_itinerary
 *
 * @author L
 * @date
 */
@Data
@TableName("data_flight_itinerary")
public class DataFlightItinerary extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 销售单位代号
     */
    @FieldName(value="销售单位代号")
    private String agentCode;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 民航发展基金
     */
    @FieldName(value="民航发展基金")
    private String caacDevelopmentFund;
    /**
     * 校验码
     */
    @FieldName(value="校验码")
    private String checkCode;
    /**
     * 填开日期
     */
    @FieldName(value="填开日期")
    private Date invoiceDate;
    /**
     * 票价
     */
    @FieldName(value="票价")
    private BigDecimal fare;
    /**
     * 图片表主键
     */
    @FieldName(value="图片表主键")
    private String fileId;
    /**
     * 燃油附加费
     */
    @FieldName(value="燃油附加费")
    private BigDecimal fuelSurcharge;
    /**
     * 保险费
     */
    @FieldName(value="保险费")
    private BigDecimal insurance;
    /**
     * 国内国际标签
     */
    @FieldName(value="国内国际标签")
    private String internationalFlag;
    /**
     * 填开单位
     */
    @FieldName(value="填开单位")
    private String issueBy;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 电子客票号码
     */
    @FieldName(value="电子客票号码")
    private String ticketNum;
    /**
     * 税额
     */
    @FieldName(value="税额")
    private BigDecimal tax;
    /**
     * 总计
     */
    @FieldName(value="总计")
    private BigDecimal invoiceTotal;
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
     * 行程单详情
     */
    @TableField(exist = false)
    private List<DataFlights> dataFlights;


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
