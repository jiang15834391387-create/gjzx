package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataCustomsExportGoodsDetail;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 海关出口货物明细视图对象 data_customs_export_goods_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataCustomsExportGoodsDetail.class)
public class DataCustomsExportGoodsDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 商品编号
     */
    @ExcelProperty(value = "商品编号")
    private String commodityNumber;

    /**
     * 币制
     */
    @ExcelProperty(value = "币制")
    private String currency;

    /**
     * 商品名称
     */
    @ExcelProperty(value = "商品名称")
    private String descriptionOfCommodity;

    /**
     * 运抵国(地区)
     */
    @ExcelProperty(value = "运抵国(地区)")
    private String finalDestinationCountry;

    /**
     * 项号
     */
    @ExcelProperty(value = "项号")
    private String itemNumber;

    /**
     * 征免
     */
    @ExcelProperty(value = "征免")
    private String kindOfTax;

    /**
     * 原产国（地区）
     */
    @ExcelProperty(value = "原产国", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "地=区")
    private String originalCountry;

    /**
     * 境内货源地
     */
    @ExcelProperty(value = "境内货源地")
    private String originalPlaceOfDeliveredGoods;

    /**
     * 第二计量数量单位
     */
    @ExcelProperty(value = "第二计量数量单位")
    private String quantityOf2Uom;

    /**
     * 第一计量数量单位
     */
    @ExcelProperty(value = "第一计量数量单位")
    private String quantityOfUom;

    /**
     * 商品规格型号
     */
    @ExcelProperty(value = "商品规格型号")
    private String specification;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private String totalPrice;

    /**
     * 成交计量数量单位
     */
    @ExcelProperty(value = "成交计量数量单位")
    private String transactionUomAndQuantity;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private String unitPrice;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remark;


}
