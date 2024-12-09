package org.smartlink.server.nc.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 出租车发票对象 data_taxi_tickets
 *
 * @author L
 * @date
 */
@Data
@TableName("data_taxi_tickets")
public class DataTaxiTickets extends BaseEntity {

    private static final long serialVersionUID=1L;
    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 城市
     */
    @FieldName(value="城市")
    private String city;

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
     * 日期
     */
    @FieldName(value="日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 图片表id
     */
    @FieldName(value="图片表id")
    private String fileId;
    /**
     * 车牌号
     */
    @FieldName(value="车牌号")
    private String licensePlate;
    /**
     * 里程
     */
    @FieldName(value="里程")
    private String mileage;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 发票所属地区
     */
    @FieldName(value="发票所属地区")
    private String place;
    /**
     * 省
     */
    @FieldName(value="省")
    private String province;
    /**
     * 下车时间
     */
    @FieldName(value="下车时间")
    private String timeGetOff;
    /**
     * 上车时间
     */
    @FieldName(value="上车时间")
    private String timeGetOn;
    /**
     * 总计
     */
    @FieldName(value="总计")
    private BigDecimal invoiceTotal;
    /**
     * 燃油附加费
     */
    @FieldName(value="燃油附加费")
    private BigDecimal fuelSurcharge;
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
