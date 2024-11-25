package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;

/**
 * 滴滴行程单明细对象 data_didi_itinerary_details
 *
 * @author L
 * @date
 */
@Data
@TableName("data_didi_itinerary_details")
public class DataDidiItineraryDetails extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 车型
     */
    @FieldName(value="车型")
    private String carType;
    /**
     * 城市
     */
    @FieldName(value="城市")
    private String city;
    /**
     * 图片表主键
     */
    @FieldName(value="图片表主键")
    private String fileId;
    /**
     * 里程（公里）
     */
    @FieldName(value="里程（公里）")
    private String mileage;
    /**
     * 终点
     */
    @FieldName(value="终点")
    private String stationGetOff;
    /**
     * 起点
     */
    @FieldName(value="起点")
    private String stationGetOn;
    /**
     * 下单时间
     */
    @FieldName(value="下单时间")
    private String timeOrder;
    /**
     * 上车时间
     */
    @FieldName(value="上车时间")
    private String timeGetOn;
    /**
     * 下车时间
     */
    @FieldName(value="下车时间")
    private String timeGetOff;
    /**
     * 服务商
     */
    @FieldName(value="服务商")
    private String producer;
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
     *  ocr表id
     */
    @FieldName(value = "ocr表id")
    private String ocrId;

}
