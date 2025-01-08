package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataCustomsImxportGoods;
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
 * 海关进口货物报关单视图对象 data_customs_imxport_goods
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataCustomsImxportGoods.class)
public class DataCustomsImxportGoodsVo implements Serializable {

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
     * 杂费
     */
    @ExcelProperty(value = "杂费")
    private String additionalExpress;

    /**
     * 支付特许权使用费确认
     */
    @ExcelProperty(value = "支付特许权使用费确认")
    private String confirmOfPayRoyalties;

    /**
     * 特殊关系确认
     */
    @ExcelProperty(value = "特殊关系确认")
    private String confirmOfSpecialRelationship;

    /**
     * 消费使用单位-编号
     */
    @ExcelProperty(value = "消费使用单位-编号")
    private String consumptionCompanyCode;

    /**
     * 消费使用单位-名称
     */
    @ExcelProperty(value = "消费使用单位-名称")
    private String consumptionCompanyName;

    /**
     * 合同协议号
     */
    @ExcelProperty(value = "合同协议号")
    private String contractNumber;

    /**
     * 海关编号
     */
    @ExcelProperty(value = "海关编号")
    private String customsNumber;

    /**
     * 申报日期
     */
    @ExcelProperty(value = "申报日期")
    private String dateOfApplication;

    /**
     * 进口日期
     */
    @ExcelProperty(value = "进口日期")
    private String dateOfImport;

    /**
     * 提运单号
     */
    @ExcelProperty(value = "提运单号")
    private String deliveryNumber;

    /**
     * 启运国（地区）-编号
     */
    @ExcelProperty(value = "启运国", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "地=区")
    private String departureCountryCode;

    /**
     * 启运国（地区）-名称
     */
    @ExcelProperty(value = "启运国", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "地=区")
    private String departureCountryName;

    /**
     * 境内收发货人-编号
     */
    @ExcelProperty(value = "境内收发货人-编号")
    private String executiveCompanyCode;

    /**
     * 境内收发货人-名称
     */
    @ExcelProperty(value = "境内收发货人-名称")
    private String executiveCompanyName;

    /**
     * 运费
     */
    @ExcelProperty(value = "运费")
    private String freight;

    /**
     * 毛重
     */
    @ExcelProperty(value = "毛重")
    private String grossWeight;

    /**
     * 保费
     */
    @ExcelProperty(value = "保费")
    private String insurancePremium;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 征免性质-编号
     */
    @ExcelProperty(value = "征免性质-编号")
    private String kindOfTaxCode;

    /**
     * 征免性质-名称
     */
    @ExcelProperty(value = "征免性质-名称")
    private String kindOfTaxName;

    /**
     * 标记唛码及备注
     */
    @ExcelProperty(value = "标记唛码及备注")
    private String marksAndRemarks;

    /**
     * 运输方式-编号
     */
    @ExcelProperty(value = "运输方式-编号")
    private String modeOfTransportationCode;

    /**
     * 运输方式-名称
     */
    @ExcelProperty(value = "运输方式-名称")
    private String modeOfTransportationName;

    /**
     * 净重
     */
    @ExcelProperty(value = "净重")
    private String netWeight;

    /**
     * 件数
     */
    @ExcelProperty(value = "件数")
    private String numberOfPackages;

    /**
     * 境外发货人-编号
     */
    @ExcelProperty(value = "境外发货人-编号")
    private String overseasConsigneeCode;

    /**
     * 境外发货人-名称
     */
    @ExcelProperty(value = "境外发货人-名称")
    private String overseasConsigneeName;

    /**
     * 入境关别-编号
     */
    @ExcelProperty(value = "入境关别-编号")
    private String portOfImportCode;

    /**
     * 如境关别-名称
     */
    @ExcelProperty(value = "如境关别-名称")
    private String portOfImportName;

    /**
     * 预录入编号
     */
    @ExcelProperty(value = "预录入编号")
    private String preRecordNumber;

    /**
     * 成交方式-编号
     */
    @ExcelProperty(value = "成交方式-编号")
    private String tradeTermsCode;

    /**
     * 成交方式-名称
     */
    @ExcelProperty(value = "成交方式-名称")
    private String tradeTermsName;

    /**
     * 贸易国（地区）-编号
     */
    @ExcelProperty(value = "贸易国", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "地=区")
    private String tradingCountryCode;

    /**
     * 贸易国（地区）-名称
     */
    @ExcelProperty(value = "贸易国", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "地=区")
    private String tradingCountryName;

    /**
     * 运输工具名称（运输工具名称及航次号）
     */
    @ExcelProperty(value = "运输工具名称", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "运=输工具名称及航次号")
    private String transportationTools;

    /**
     * 监管方式-编号
     */
    @ExcelProperty(value = "监管方式-编号")
    private String modeOfTradeCode;

    /**
     * 监管方式-名称
     */
    @ExcelProperty(value = "监管方式-名称")
    private String modeOfTradeName;

    /**
     * 随附单证及编号
     */
    @ExcelProperty(value = "随附单证及编号")
    private String attachmentsAndNumbers;

    /**
     * 申报单位
     */
    @ExcelProperty(value = "申报单位")
    private String filingEntity;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

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
