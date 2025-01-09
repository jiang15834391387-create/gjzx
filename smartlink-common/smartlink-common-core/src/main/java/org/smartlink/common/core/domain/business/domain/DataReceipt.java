package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

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
    @TableId(value = "id")
    private String id;

    /**
     * 币种
     */
    private String currencyCode;

    /**
     * 日期
     */
    private String invoiceDate;

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
    private String subTotal;

    /**
     * 税费
     */
    private String tax;

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
    private String invoiceTotal;

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
