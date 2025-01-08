package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 海关出口货物明细对象 data_customs_export_goods_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_customs_export_goods_detail")
public class DataCustomsExportGoodsDetail extends TenantEntity {

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
     * 商品编号
     */
    private String commodityNumber;

    /**
     * 币制
     */
    private String currency;

    /**
     * 商品名称
     */
    private String descriptionOfCommodity;

    /**
     * 运抵国(地区)
     */
    private String finalDestinationCountry;

    /**
     * 项号
     */
    private String itemNumber;

    /**
     * 征免
     */
    private String kindOfTax;

    /**
     * 原产国（地区）
     */
    private String originalCountry;

    /**
     * 境内货源地
     */
    private String originalPlaceOfDeliveredGoods;

    /**
     * 第二计量数量单位
     */
    private String quantityOf2Uom;

    /**
     * 第一计量数量单位
     */
    private String quantityOfUom;

    /**
     * 商品规格型号
     */
    private String specification;

    /**
     * 总价
     */
    private Long totalPrice;

    /**
     * 成交计量数量单位
     */
    private String transactionUomAndQuantity;

    /**
     * 单价
     */
    private Long unitPrice;

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
