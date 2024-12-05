package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 承兑汇票对象 data_aircraft_invoice
 *
 * @author L
 * @date
 */
@Data
@TableName("data_acceptance_bill")
public class DataAcceptanceBill extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value = "主键")
    private String id;
    /**
     * 关联图片表id
     */
    @FieldName(value = "关联图片表id")
    private String fileId;

    /**
     * 发票票据号码
     */
    @FieldName(value = "票据号码")
    private String invoiceNumber;
    /**
     * 出票日期
     */
    @FieldName(value = "出票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    /**
     * 汇票到日期
     */
    @FieldName(value = "汇票到日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date mailDate;

    /**
     * 出票人全称
     */
    @FieldName(value = "出票人全称")
    private String disbursementName;

    /**
     * 出票人账号
     */
    @FieldName(value = "出票人账号")
    private String disbursementAccount;

    /**
     * 出票人开户银行
     */
    @FieldName(value = "出票人开户银行")
    private String disbursementBank;

    /**
     * 收款人全称
     */
    @FieldName(value = "收款人全称")
    private String payeeName;

    /**
     * 收款人账号
     */
    @FieldName(value = "收款人账号")
    private String payeeAccount;

    /**
     * 收款人开户银行
     */
    @FieldName(value = "收款人开户银行")
    private String payeeBank;

    /**
     * 开户行行号
     */
    @FieldName(value = "开户行行号")
    private String accountLineNumber;

    /**
     * 开户行名称
     */
    @FieldName(value = "开户行名称")
    private String accountName;

    /**
     * 票据金额
     */
    @FieldName(value = "票据金额")
    private BigDecimal invoiceTotal;

    /**
     * 票据金额大写金额
     */
    @FieldName(value = "票据金额大写金额")
    private String totalUppercase;

    /**
     * 票据状态
     */
    @FieldName(value = "票据状态")
    private BigDecimal invoiceType;

    /**
     * 税务云token
     */
    @FieldName(value = "税务云token")
    private String saveToken;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @FieldName(value = "是否删除标识")
    private String deleteFlag;

    /**
     * 租户id
     */
    @FieldName(value = "租户id")
    private String tenantId;

    /**
     * 置信度
     */
    @FieldName(value = "置信度")
    private String confidence;
    /**
     * 台账推送业务系统成功标识
     */
    @FieldName(value = "台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;
    /**
     * 坐标
     */
    @FieldName(value = "坐标")
    private String coordinateStr;
    /**
     * 暂存状态
     */
    @FieldName(value = "暂存状态")
    private String isStaging;

}
