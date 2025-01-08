package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataNonTax;
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
 * 非税收入类票据视图对象 data_non_tax
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataNonTax.class)
public class DataNonTaxVo implements Serializable {

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
     * 票据号码
     */
    @ExcelProperty(value = "票据号码")
    private String invoiceNumber;

    /**
     * 票据代码
     */
    @ExcelProperty(value = "票据代码")
    private String invoiceCode;

    /**
     * 校验码
     */
    @ExcelProperty(value = "校验码")
    private String checkCode;

    /**
     * 复核人
     */
    @ExcelProperty(value = "复核人")
    private String checker;

    /**
     * 收款人
     */
    @ExcelProperty(value = "收款人")
    private String receiver;

    /**
     * 是否为电子非税收入票据(1：是)
     */
    @ExcelProperty(value = "是否为电子非税收入票据(1：是)")
    private String electronicMark;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 其他信息
     */
    @ExcelProperty(value = "其他信息")
    private String otherInfo;

    /**
     * 收款单位
     */
    @ExcelProperty(value = "收款单位")
    private String payee;

    /**
     * 交款人
     */
    @ExcelProperty(value = "交款人")
    private String payer;

    /**
     * 交款人统一社会信用代码
     */
    @ExcelProperty(value = "交款人统一社会信用代码")
    private String socialCreditCode;

    /**
     * 区块标识
     */
    @ExcelProperty(value = "区块标识")
    private String blockChain;

    /**
     * 缴款码
     */
    @ExcelProperty(value = "缴款码")
    private String paymentCode;

    /**
     * 执收单位编码
     */
    @ExcelProperty(value = "执收单位编码")
    private String payeeCode;

    /**
     * 交款人账号
     */
    @ExcelProperty(value = "交款人账号")
    private String payerAccountNumber;

    /**
     * 交款人开户银行
     */
    @ExcelProperty(value = "交款人开户银行")
    private String payerAccountOpeningBank;

    /**
     * 收款人账号
     */
    @ExcelProperty(value = "收款人账号")
    private String receiverAccountNumber;

    /**
     * 收款人开户银行
     */
    @ExcelProperty(value = "收款人开户银行")
    private String receiverAccountOpeningBank;

    /**
     * 经办人
     */
    @ExcelProperty(value = "经办人")
    private String handler;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

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
