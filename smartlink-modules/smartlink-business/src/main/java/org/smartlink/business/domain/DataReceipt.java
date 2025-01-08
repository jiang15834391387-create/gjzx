package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 小票对象 data_receipt
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_receipt")
public class DataReceipt extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 币种
     */
    private String currencyCode;

    /**
     * 日期
     */
    private Date invoiceDate;

    /**
     * 时间
     */
    private String time;

    /**
     * 折扣
     */
    private String discount;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 店名
     */
    private String storeName;

    /**
     * 小计
     */
    private Long subTotal;

    /**
     * 税费
     */
    private Long tax;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 小费
     */
    private String tips;

    /**
     * 总计
     */
    private Long invoiceTotal;

    /**
     * 消费类型
     */
    private String type;

    /**
     * 1:国际票 0:国内票
     */
    private String internationalMark;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * token
     */
    private String saveToken;

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

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 入台账标识
     */
    private String pushBusinessInfoFlag;


}
