package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataDutyPaidProof;
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
 * 完税证明视图对象 data_duty_paid_proof
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataDutyPaidProof.class)
public class DataDutyPaidProofVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    private String fileId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 票证字轨
     */
    @ExcelProperty(value = "票证字轨")
    private String serialNumber;

    /**
     * 购买方名称
     */
    @ExcelProperty(value = "购买方名称")
    private String buyerName;

    /**
     * 缴款人识别号
     */
    @ExcelProperty(value = "缴款人识别号")
    private String buyerTaxId;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private String invoiceDate;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String invoiceTotal;

    /**
     * 大写金额
     */
    @ExcelProperty(value = "大写金额")
    private String totalUppercase;

    /**
     * 主管税务机关
     */
    @ExcelProperty(value = "主管税务机关")
    private String taxAuthority;

    /**
     * 征收机关代码
     */
    @ExcelProperty(value = "征收机关代码")
    private String taxAgencyCode;

    /**
     * 纳税人开户行
     */
    @ExcelProperty(value = "纳税人开户行")
    private String buyerDepositBank;

    /**
     * 纳税人账号
     */
    @ExcelProperty(value = "纳税人账号")
    private String buyerAccount;

    /**
     * 税款限缴期限
     */
    @ExcelProperty(value = "税款限缴期限")
    private String taxPaymentLimitedTime;

    /**
     * 收款国库
     */
    @ExcelProperty(value = "收款国库")
    private String receivingTreasury;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

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
