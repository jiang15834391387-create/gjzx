package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    private Date invoiceDate;

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
    private Long invoiceTotal;

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
     * 睿真token
     */
    private String saveToken;

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
     * 置信度
     */
    private String confidence;

    /**
     * 版本号
     */
    @Version
    private Long version;

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 入台账标识
     */
    private String pushBusinessInfoFlag;


}
