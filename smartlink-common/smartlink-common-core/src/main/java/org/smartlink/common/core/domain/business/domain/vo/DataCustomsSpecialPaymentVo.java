package org.smartlink.common.core.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.business.domain.DataCustomsSpecialPayment;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 海关专用缴款书视图对象 data_customs_special_payment
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataCustomsSpecialPayment.class)
public class DataCustomsSpecialPaymentVo implements Serializable {

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
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 缴款单位(人)帐号
     */
    @ExcelProperty(value = "缴款单位(人)帐号")
    private String account;

    /**
     * 缴款单位(人)开户银行
     */
    @ExcelProperty(value = "缴款单位(人)开户银行")
    private String accountBank;

    /**
     * 缴款单位(人)公司名称
     */
    @ExcelProperty(value = "缴款单位(人)公司名称")
    private String companyName;

    /**
     * 合同（批文）号
     */
    @ExcelProperty(value = "合同", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "批=文")
    private String contractNumber;

    /**
     * 备注币种
     */
    @ExcelProperty(value = "备注币种")
    private String currencyComment;

    /**
     * 海关名称
     */
    @ExcelProperty(value = "海关名称")
    private String customsName;

    /**
     * 报关单编号
     */
    @ExcelProperty(value = "报关单编号")
    private String customsNumber;

    /**
     * 提/装货单号
     */
    @ExcelProperty(value = "提/装货单号")
    private String deliveryNumber;

    /**
     * 填制单位
     */
    @ExcelProperty(value = "填制单位")
    private String fillingCompany;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 缴费类型
     */
    @ExcelProperty(value = "缴费类型")
    private String paymentType;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remarks;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 填制单位章
     */
    @ExcelProperty(value = "填制单位章")
    private String seal;

    /**
     * 号码
     */
    @ExcelProperty(value = "号码")
    private String number;

    /**
     * 海关口岸代码
     */
    @ExcelProperty(value = "海关口岸代码")
    private String portCode;

    /**
     * 收入机关
     */
    @ExcelProperty(value = "收入机关")
    private String revenueAgency;

    /**
     * 科目
     */
    @ExcelProperty(value = "科目")
    private String subject;

    /**
     * 备注计税汇率
     */
    @ExcelProperty(value = "备注计税汇率")
    private String taxExchangeRateComment;

    /**
     * 合计金额(小写)
     */
    @ExcelProperty(value = "合计金额(小写)")
    private String invoiceTotal;

    /**
     * 合计金额(大写)
     */
    @ExcelProperty(value = "合计金额(大写)")
    private String totalWords;

    /**
     * 运输工具名称（运输工具名称及航次号）
     */
    @ExcelProperty(value = "运输工具名称", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "运=输工具名称及航次号")
    private String transportationTools;

    /**
     * 收入系统
     */
    @ExcelProperty(value = "收入系统")
    private String incomeSystem;

    /**
     * 收款国库
     */
    @ExcelProperty(value = "收款国库")
    private String receiptTreasury;

    /**
     * 预算级次
     */
    @ExcelProperty(value = "预算级次")
    private String budgetLevel;

    /**
     * 申请单位编号
     */
    @ExcelProperty(value = "申请单位编号")
    private String applicationUnitNumber;

    /**
     * 缴款期限
     */
    @ExcelProperty(value = "缴款期限")
    private String paymentDeadline;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String invoiceDate;

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
