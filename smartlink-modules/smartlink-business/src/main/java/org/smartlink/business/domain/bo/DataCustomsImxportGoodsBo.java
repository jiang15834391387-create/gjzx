package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataCustomsImxportGoods;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 海关进口货物报关单业务对象 data_customs_imxport_goods
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataCustomsImxportGoods.class, reverseConvertGenerate = false)
public class DataCustomsImxportGoodsBo extends BaseEntity {

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
     * 杂费
     */
    @NotBlank(message = "杂费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String additionalExpress;

    /**
     * 支付特许权使用费确认
     */
    @NotBlank(message = "支付特许权使用费确认不能为空", groups = { AddGroup.class, EditGroup.class })
    private String confirmOfPayRoyalties;

    /**
     * 特殊关系确认
     */
    @NotBlank(message = "特殊关系确认不能为空", groups = { AddGroup.class, EditGroup.class })
    private String confirmOfSpecialRelationship;

    /**
     * 消费使用单位-编号
     */
    @NotBlank(message = "消费使用单位-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String consumptionCompanyCode;

    /**
     * 消费使用单位-名称
     */
    @NotBlank(message = "消费使用单位-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String consumptionCompanyName;

    /**
     * 合同协议号
     */
    @NotBlank(message = "合同协议号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractNumber;

    /**
     * 海关编号
     */
    @NotBlank(message = "海关编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String customsNumber;

    /**
     * 申报日期
     */
    @NotBlank(message = "申报日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dateOfApplication;

    /**
     * 进口日期
     */
    @NotBlank(message = "进口日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String dateOfImport;

    /**
     * 提运单号
     */
    @NotBlank(message = "提运单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deliveryNumber;

    /**
     * 启运国（地区）-编号
     */
    @NotBlank(message = "启运国（地区）-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String departureCountryCode;

    /**
     * 启运国（地区）-名称
     */
    @NotBlank(message = "启运国（地区）-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String departureCountryName;

    /**
     * 境内收发货人-编号
     */
    @NotBlank(message = "境内收发货人-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String executiveCompanyCode;

    /**
     * 境内收发货人-名称
     */
    @NotBlank(message = "境内收发货人-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String executiveCompanyName;

    /**
     * 运费
     */
    @NotBlank(message = "运费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String freight;

    /**
     * 毛重
     */
    @NotBlank(message = "毛重不能为空", groups = { AddGroup.class, EditGroup.class })
    private String grossWeight;

    /**
     * 保费
     */
    @NotBlank(message = "保费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String insurancePremium;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 征免性质-编号
     */
    @NotBlank(message = "征免性质-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kindOfTaxCode;

    /**
     * 征免性质-名称
     */
    @NotBlank(message = "征免性质-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kindOfTaxName;

    /**
     * 标记唛码及备注
     */
    @NotBlank(message = "标记唛码及备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String marksAndRemarks;

    /**
     * 运输方式-编号
     */
    @NotBlank(message = "运输方式-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modeOfTransportationCode;

    /**
     * 运输方式-名称
     */
    @NotBlank(message = "运输方式-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modeOfTransportationName;

    /**
     * 净重
     */
    @NotBlank(message = "净重不能为空", groups = { AddGroup.class, EditGroup.class })
    private String netWeight;

    /**
     * 件数
     */
    @NotBlank(message = "件数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String numberOfPackages;

    /**
     * 境外发货人-编号
     */
    @NotBlank(message = "境外发货人-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String overseasConsigneeCode;

    /**
     * 境外发货人-名称
     */
    @NotBlank(message = "境外发货人-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String overseasConsigneeName;

    /**
     * 入境关别-编号
     */
    @NotBlank(message = "入境关别-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String portOfImportCode;

    /**
     * 如境关别-名称
     */
    @NotBlank(message = "如境关别-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String portOfImportName;

    /**
     * 预录入编号
     */
    @NotBlank(message = "预录入编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String preRecordNumber;

    /**
     * 成交方式-编号
     */
    @NotBlank(message = "成交方式-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tradeTermsCode;

    /**
     * 成交方式-名称
     */
    @NotBlank(message = "成交方式-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tradeTermsName;

    /**
     * 贸易国（地区）-编号
     */
    @NotBlank(message = "贸易国（地区）-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tradingCountryCode;

    /**
     * 贸易国（地区）-名称
     */
    @NotBlank(message = "贸易国（地区）-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tradingCountryName;

    /**
     * 运输工具名称（运输工具名称及航次号）
     */
    @NotBlank(message = "运输工具名称（运输工具名称及航次号）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transportationTools;

    /**
     * 监管方式-编号
     */
    @NotBlank(message = "监管方式-编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modeOfTradeCode;

    /**
     * 监管方式-名称
     */
    @NotBlank(message = "监管方式-名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String modeOfTradeName;

    /**
     * 随附单证及编号
     */
    @NotBlank(message = "随附单证及编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String attachmentsAndNumbers;

    /**
     * 申报单位
     */
    @NotBlank(message = "申报单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String filingEntity;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

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
