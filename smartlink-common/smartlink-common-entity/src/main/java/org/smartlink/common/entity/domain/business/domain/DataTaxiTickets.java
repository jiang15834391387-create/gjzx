package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 出租车发票对象 data_taxi_tickets
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_taxi_tickets")
public class DataTaxiTickets extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 标题
     */
    private String title;

    /**
     * 城市
     */
    private String city;

    /**
     * 发票消费类型
     */
    private String kind;


    /**
     * 图片旋转角度
     */
    private String orientation;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 日期
     */
    private String invoiceDate;

    /**
     * 车牌号
     */
    private String licensePlate;

    /**
     * 里程
     */
    private String mileage;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 发票所属地区
     */
    private String place;

    /**
     * 省
     */
    private String province;

    /**
     * 下车时间
     */
    private String timeGetOff;

    /**
     * 上车时间
     */
    private String timeGetOn;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 燃油附加费
     */
    private String fuelSurcharge;

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
