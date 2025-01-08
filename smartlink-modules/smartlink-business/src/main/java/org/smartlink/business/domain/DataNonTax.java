package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 非税收入类票据对象 data_non_tax
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_non_tax")
public class DataNonTax extends TenantEntity {

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
     * 票据号码
     */
    private String invoiceNumber;

    /**
     * 票据代码
     */
    private String invoiceCode;

    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 复核人
     */
    private String checker;

    /**
     * 收款人
     */
    private String receiver;

    /**
     * 是否为电子非税收入票据(1：是)
     */
    private String electronicMark;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 其他信息
     */
    private String otherInfo;

    /**
     * 收款单位
     */
    private String payee;

    /**
     * 交款人
     */
    private String payer;

    /**
     * 交款人统一社会信用代码
     */
    private String socialCreditCode;

    /**
     * 区块标识
     */
    private String blockChain;

    /**
     * 缴款码
     */
    private String paymentCode;

    /**
     * 执收单位编码
     */
    private String payeeCode;

    /**
     * 交款人账号
     */
    private String payerAccountNumber;

    /**
     * 交款人开户银行
     */
    private String payerAccountOpeningBank;

    /**
     * 收款人账号
     */
    private String receiverAccountNumber;

    /**
     * 收款人开户银行
     */
    private String receiverAccountOpeningBank;

    /**
     * 经办人
     */
    private String handler;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 合计金额(小写)
     */
    private Long invoiceTotal;

    /**
     * 合计金额(大写)
     */
    private String totalWords;

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
