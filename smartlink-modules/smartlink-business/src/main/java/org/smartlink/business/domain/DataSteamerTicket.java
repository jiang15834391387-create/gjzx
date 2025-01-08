package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 船票对象 data_steamer_ticket
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_steamer_ticket")
public class DataSteamerTicket extends TenantEntity {

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
     * 城市
     */
    private String city;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 币种
     */
    private String currencyCode;

    /**
     * 日期
     */
    private Date invoiceDate;

    /**
     * 姓名
     */
    private String name;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 省
     */
    private String province;

    /**
     * 到达站
     */
    private String stationGetOff;

    /**
     * 出发站
     */
    private String stationGetOn;

    /**
     * 时间
     */
    private String invoiceTime;

    /**
     * 总计
     */
    private Long invoiceTotal;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 身份证号
     */
    private String userId;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * token
     */
    private String saveToken;

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

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 入台账标识
     */
    private String pushBusinessInfoFlag;


}
