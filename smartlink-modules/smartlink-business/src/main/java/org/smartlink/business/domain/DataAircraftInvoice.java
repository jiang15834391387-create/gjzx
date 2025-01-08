package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 机打发票对象 data_aircraft_invoice
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_aircraft_invoice")
public class DataAircraftInvoice extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 关联图片表id
     */
    private String fileId;

    /**
     * 标题
     */
    private String title;

    /**
     * 购方单位
     */
    private String buyerName;

    /**
     * 纳税人识别号
     */
    private String buyerTaxid;

    /**
     * 种类
     */
    private String category;

    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 所属城市
     */
    private String city;

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 日期
     */
    private Date invoiceDate;

    /**
     * 地区(省)
     */
    private String province;

    /**
     * 销方单位名称
     */
    private String sellerName;

    /**
     * 销方税号
     */
    private String sellerTaxid;

    /**
     * 总价
     */
    private Long invoiceTotal;

    /**
     * 是否为浙江/广东通用机打电子发票
     */
    private String electronicMark;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    private String companySeal;

    /**
     * 大写合计金额
     */
    private String moneyUppercase;

    /**
     * 税前金额
     */
    private Long pretaxAmount;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 税务云token
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
