package org.smartlink.common.entity.domain.business.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataCustomsSpecialPayment;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 海关专用缴款书业务对象 data_customs_special_payment
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataCustomsSpecialPayment.class, reverseConvertGenerate = false)
public class DataCustomsSpecialPaymentBo extends BaseEntity {

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
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 缴款单位(人)帐号
     */
    @NotBlank(message = "缴款单位(人)帐号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String account;

    /**
     * 缴款单位(人)开户银行
     */
    @NotBlank(message = "缴款单位(人)开户银行不能为空", groups = { AddGroup.class, EditGroup.class })
    private String accountBank;

    /**
     * 缴款单位(人)公司名称
     */
    @NotBlank(message = "缴款单位(人)公司名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companyName;

    /**
     * 合同（批文）号
     */
    @NotBlank(message = "合同（批文）号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String contractNumber;

    /**
     * 备注币种
     */
    @NotBlank(message = "备注币种不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currencyComment;

    /**
     * 海关名称
     */
    @NotBlank(message = "海关名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String customsName;

    /**
     * 报关单编号
     */
    @NotBlank(message = "报关单编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String customsNumber;

    /**
     * 提/装货单号
     */
    @NotBlank(message = "提/装货单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deliveryNumber;

    /**
     * 填制单位
     */
    @NotBlank(message = "填制单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fillingCompany;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 缴费类型
     */
    @NotBlank(message = "缴费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String paymentType;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remarks;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 填制单位章
     */
    @NotBlank(message = "填制单位章不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seal;

    /**
     * 号码
     */
    @NotBlank(message = "号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String number;

    /**
     * 海关口岸代码
     */
    @NotBlank(message = "海关口岸代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String portCode;

    /**
     * 收入机关
     */
    @NotBlank(message = "收入机关不能为空", groups = { AddGroup.class, EditGroup.class })
    private String revenueAgency;

    /**
     * 科目
     */
    @NotBlank(message = "科目不能为空", groups = { AddGroup.class, EditGroup.class })
    private String subject;

    /**
     * 备注计税汇率
     */
    @NotBlank(message = "备注计税汇率不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxExchangeRateComment;

    /**
     * 合计金额(小写)
     */
    @NotBlank(message = "合计金额(小写)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

    /**
     * 合计金额(大写)
     */
    @NotBlank(message = "合计金额(大写)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalWords;

    /**
     * 运输工具名称（运输工具名称及航次号）
     */
    @NotBlank(message = "运输工具名称（运输工具名称及航次号）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transportationTools;

    /**
     * 收入系统
     */
    @NotBlank(message = "收入系统不能为空", groups = { AddGroup.class, EditGroup.class })
    private String incomeSystem;

    /**
     * 收款国库
     */
    @NotBlank(message = "收款国库不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiptTreasury;

    /**
     * 预算级次
     */
    @NotBlank(message = "预算级次不能为空", groups = { AddGroup.class, EditGroup.class })
    private String budgetLevel;

    /**
     * 申请单位编号
     */
    @NotBlank(message = "申请单位编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String applicationUnitNumber;

    /**
     * 缴款期限
     */
    @NotBlank(message = "缴款期限不能为空", groups = { AddGroup.class, EditGroup.class })
    private String paymentDeadline;

    /**
     * 角度
     */
    @NotBlank(message = "角度", groups = { AddGroup.class, EditGroup.class })
    private String orientation;

    /**
     * 日期
     */
    @NotBlank(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

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
