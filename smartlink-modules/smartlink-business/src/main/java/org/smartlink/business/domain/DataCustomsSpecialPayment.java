package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 海关专用缴款书对象 data_customs_special_payment
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_customs_special_payment")
public class DataCustomsSpecialPayment extends TenantEntity {

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
     * 标题
     */
    private String title;

    /**
     * 缴款单位(人)帐号
     */
    private String account;

    /**
     * 缴款单位(人)开户银行
     */
    private String accountBank;

    /**
     * 缴款单位(人)公司名称
     */
    private String companyName;

    /**
     * 合同（批文）号
     */
    private String contractNumber;

    /**
     * 备注币种
     */
    private String currencyComment;

    /**
     * 海关名称
     */
    private String customsName;

    /**
     * 报关单编号
     */
    private String customsNumber;

    /**
     * 提/装货单号
     */
    private String deliveryNumber;

    /**
     * 填制单位
     */
    private String fillingCompany;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 缴费类型
     */
    private String paymentType;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 填制单位章
     */
    private String seal;

    /**
     * 号码
     */
    private String number;

    /**
     * 海关口岸代码
     */
    private String portCode;

    /**
     * 收入机关
     */
    private String revenueAgency;

    /**
     * 科目
     */
    private String subject;

    /**
     * 备注计税汇率
     */
    private String taxExchangeRateComment;

    /**
     * 合计金额(小写)
     */
    private Long invoiceTotal;

    /**
     * 合计金额(大写)
     */
    private String totalWords;

    /**
     * 运输工具名称（运输工具名称及航次号）
     */
    private String transportationTools;

    /**
     * 收入系统
     */
    private String incomeSystem;

    /**
     * 收款国库
     */
    private String receiptTreasury;

    /**
     * 预算级次
     */
    private String budgetLevel;

    /**
     * 申请单位编号
     */
    private String applicationUnitNumber;

    /**
     * 缴款期限
     */
    private String paymentDeadline;

    /**
     * 日期
     */
    private Date invoiceDate;

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
