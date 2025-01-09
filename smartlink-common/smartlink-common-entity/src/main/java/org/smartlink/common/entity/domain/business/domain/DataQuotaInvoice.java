package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 定额发票对象 data_quota_invoice
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_quota_invoice")
public class DataQuotaInvoice extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

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
     * 图片表id
     */
    private String fileId;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 省
     */
    private String province;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    private String companySeal;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 金额(大写)
     */
    private String moneyUppercase;

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
