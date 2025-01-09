package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 滴滴行程单对象 data_didi_itinerary
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_didi_itinerary")
public class DataDidiItinerary extends TenantEntity {

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
     * 日期
     */
    private String invoiceDate;

    /**
     * 行程结束时间
     */
    private String timeGetOff;

    /**
     * 行程开始时间
     */
    private String timeGetOn;

    /**
     * 行程人手机号
     */
    private String phone;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

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
