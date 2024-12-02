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
 * 海关进口增值税专用缴款书对象
 * Author: L
 * Date:
 */
@Data
@TableName("data_customs_invoice")
public class DataCustomsInvoice extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 关联图片表id
     */
    @FieldName(value="关联图片表id")
    private String fileId;

    /**
     * 填发日期
     */
    @FieldName(value="填发日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 缴款单位账号
     */
    @FieldName(value="缴款单位账号")
    private String corporateAccountNo;
    /**
     * 合同号
     */
    @FieldName(value="合同号")
    private String contractNo;
    /**
     * 缴款单位开户银行
     */
    @FieldName(value="缴款单位开户银行")
    private String corporateBank;
    /**
     * 缴款书抬头
     */
    @FieldName(value="缴款书抬头")
    private String customsName;
    /**
     * 科目
     */
    @FieldName(value="科目")
    private String subject;
    /**
     * 收入系统
     */
    @FieldName(value="收入系统")
    private String revenueSys;
    /**
     * 收入机关
     */
    @FieldName(value="收入机关")
    private String revenueOrg;
    /**
     * 币种
     */
    @FieldName(value="币种")
    private String currencyCode;
    /**
     * 提/装货单号
     */
    @FieldName(value="提/装货单号")
    private String loadBillNo;
    /**
     * 金额(大写)
     */
    @FieldName(value="金额(大写)")
    private String moneyUppercase;
    /**
     * 总计
     */
    @FieldName(value="总计")
    private BigDecimal invoiceTotal;
    /**
     * 号码
     */
    @FieldName(value="号码")
    private String customsNo;
    /**
     * 缴款书抬头
     */
    @FieldName(value="缴款书抬头")
    private String corporateName;
    /**
     * 发票类型
     */
    @FieldName(value="发票类型")
    private String invoiceType;
    /**
     * 报关单编号
     */
    @FieldName(value="报关单编号")
    private String customsBillNo;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;

    /**
     * 税务云token
     */
    @FieldName(value="税务云token")
    private String saveToken;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @FieldName(value="是否删除标识")
    private String deleteFlag;
    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;
    /**
     * 置信度
     */
    @FieldName(value="置信度")
    private String confidence;
    /**
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

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
