package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 完税证明对象 data_duty_paid_proof
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_duty_paid_proof")
public class DataDutyPaidProof extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表主键
     */
    private String fileId;

    /**
     * 标题
     */
    private String title;

    /**
     * 票证字轨
     */
    private String serialNumber;

    /**
     * 购买方名称
     */
    private String buyerName;

    /**
     * 缴款人识别号
     */
    private String buyerTaxId;

    /**
     * 日期
     */
    private String invoiceDate;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 大写金额
     */
    private String totalUppercase;

    /**
     * 主管税务机关
     */
    private String taxAuthority;

    /**
     * 征收机关代码
     */
    private String taxAgencyCode;

    /**
     * 纳税人开户行
     */
    private String buyerDepositBank;

    /**
     * 纳税人账号
     */
    private String buyerAccount;

    /**
     * 税款限缴期限
     */
    private String taxPaymentLimitedTime;

    /**
     * 收款国库
     */
    private String receivingTreasury;

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
