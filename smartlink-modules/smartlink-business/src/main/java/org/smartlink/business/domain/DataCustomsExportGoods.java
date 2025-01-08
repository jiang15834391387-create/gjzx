package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 海关出口货物对象 data_customs_export_goods
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_customs_export_goods")
public class DataCustomsExportGoods extends TenantEntity {

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
     * 杂费
     */
    private Long additionalExpress;

    /**
     * 支付特许权使用费确认
     */
    private String confirmOfPayRoyalties;

    /**
     * 特殊关系确认
     */
    private String confirmOfSpecialRelationship;

    /**
     * 消费使用单位-编号
     */
    private String consumptionCompanyCode;

    /**
     * 消费使用单位-名称
     */
    private String consumptionCompanyName;

    /**
     * 合同协议号
     */
    private String contractNumber;

    /**
     * 海关编号
     */
    private String customsNumber;

    /**
     * 申报日期
     */
    private String dateOfApplication;

    /**
     * 出口日期
     */
    private String dateOfExport;

    /**
     * 提运单号
     */
    private String deliveryNumber;

    /**
     * 启运国（地区）-编号
     */
    private String departureCountryCode;

    /**
     * 启运国（地区）-名称
     */
    private String departureCountryName;

    /**
     * 境内收发货人-编号
     */
    private String executiveCompanyCode;

    /**
     * 境内收发货人-名称
     */
    private String executiveCompanyName;

    /**
     * 运费
     */
    private Long freight;

    /**
     * 毛重
     */
    private Long grossWeight;

    /**
     * 保费
     */
    private Long insurancePremium;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 征免性质-编号
     */
    private String kindOfTaxCode;

    /**
     * 征免性质-名称
     */
    private String kindOfTaxName;

    /**
     * 标记唛码及备注
     */
    private String marksAndRemarks;

    /**
     * 运输方式-编号
     */
    private String modeOfTransportationCode;

    /**
     * 运输方式-名称
     */
    private String modeOfTransportationName;

    /**
     * 净重
     */
    private String netWeight;

    /**
     * 件数
     */
    private String numberOfPackages;

    /**
     * 境外收货人-编号
     */
    private String overseasConsigneeCode;

    /**
     * 境外收货人-名称
     */
    private String overseasConsigneeName;

    /**
     * 出境关别-编号
     */
    private String portOfExportCode;

    /**
     * 出境关别-名称
     */
    private String portOfExportName;

    /**
     * 预录入编号
     */
    private String preRecordNumber;

    /**
     * 成交方式-编号
     */
    private String tradeTermsCode;

    /**
     * 成交方式-名称
     */
    private String tradeTermsName;

    /**
     * 贸易国（地区）-编号
     */
    private String tradingCountryCode;

    /**
     * 贸易国（地区）-名称
     */
    private String tradingCountryName;

    /**
     * 运输工具名称（运输工具名称及航次号）
     */
    private String transportationTools;

    /**
     * 监管方式-编号
     */
    private String modeOfTradeCode;

    /**
     * 监管方式-名称
     */
    private String modeOfTradeName;

    /**
     * 随附单证及编号
     */
    private String attachmentsAndNumbers;

    /**
     * 申报单位
     */
    private String filingEntity;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

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
