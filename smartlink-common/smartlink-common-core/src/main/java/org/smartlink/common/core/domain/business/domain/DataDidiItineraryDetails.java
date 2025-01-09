package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 滴滴行程单明细对象 data_didi_itinerary_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_didi_itinerary_details")
public class DataDidiItineraryDetails extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表主键
     */
    private String fileId;

    /**
     * 标题
     */
    private String title;

    /**
     * 车型
     */
    private String carType;

    /**
     * 城市
     */
    private String city;

    /**
     * 里程（公里）
     */
    private String mileage;

    /**
     * 终点
     */
    private String stationGetOff;

    /**
     * 起点
     */
    private String stationGetOn;

    /**
     * 下单时间
     */
    private String timeOrder;

    /**
     * 上车时间
     */
    private String timeGetOn;

    /**
     * 下车时间
     */
    private String timeGetOff;

    /**
     * 服务商
     */
    private String producer;

    /**
     * 总计
     */
    private String invoiceTotal;

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
