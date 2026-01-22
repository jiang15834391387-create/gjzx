package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 客运汽车票对象 data_passenger_car
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_passenger_car")
public class DataPassengerCar extends TenantEntity {

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
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 日期
     */
    private String invoiceDate;

    /**
     * 出发车站
     */
    private String stationGeton;

    /**
     * 达到车站
     */
    private String stationGetoff;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 时间
     */
    private String invoiceTime;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 姓名
     */
    private String name;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 身份证号
     */
    private String userId;

    /**
     * 是否盖章（0: 没有; 1: 有）
     */
    private String companySeal;

    /**
     * 车次
     */
    private String busNumber;

    /**
     * 图片旋转角度
     */
    private String orientation;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    private String checkInvoice;

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
