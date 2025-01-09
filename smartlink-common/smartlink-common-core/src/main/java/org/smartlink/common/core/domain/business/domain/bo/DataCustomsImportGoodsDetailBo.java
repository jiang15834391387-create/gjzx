package org.smartlink.common.core.domain.business.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.business.domain.DataCustomsImportGoodsDetail;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 海关进口货物明细业务对象 data_customs_import_goods_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataCustomsImportGoodsDetail.class, reverseConvertGenerate = false)
public class DataCustomsImportGoodsDetailBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 商品编号
     */
    @NotBlank(message = "商品编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String commodityNumber;

    /**
     * 币制
     */
    @NotBlank(message = "币制不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currency;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String descriptionOfCommodity;

    /**
     * 境内目的地
     */
    @NotBlank(message = "境内目的地不能为空", groups = { AddGroup.class, EditGroup.class })
    private String domesticDestinationPlace;

    /**
     * 最终目的国（地区）
     */
    @NotBlank(message = "最终目的国（地区）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String finalDestinationCountry;

    /**
     * 项号
     */
    @NotBlank(message = "项号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String itemNumber;

    /**
     * 征免
     */
    @NotBlank(message = "征免不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kindOfTax;

    /**
     * 原产国（地区）
     */
    @NotBlank(message = "原产国（地区）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String originalCountry;

    /**
     * 境内货源地
     */
    @NotBlank(message = "境内货源地不能为空", groups = { AddGroup.class, EditGroup.class })
    private String originalPlaceOfDeliveredGoods;

    /**
     * 第二计量数量单位
     */
    @NotBlank(message = "第二计量数量单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String quantityOf2Uom;

    /**
     * 第一计量数量单位
     */
    @NotBlank(message = "第一计量数量单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String quantityOfUom;

    /**
     * 商品规格型号
     */
    @NotBlank(message = "商品规格型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String specification;

    /**
     * 总价
     */
    @NotBlank(message = "总价不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalPrice;

    /**
     * 成交计量数量单位
     */
    @NotBlank(message = "成交计量数量单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transactionUomAndQuantity;

    /**
     * 单价
     */
    @NotBlank(message = "单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unitPrice;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
