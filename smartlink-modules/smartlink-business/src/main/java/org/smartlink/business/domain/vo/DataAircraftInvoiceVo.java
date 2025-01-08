package org.smartlink.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.business.domain.DataAircraftInvoice;
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
 * 机打发票视图对象 data_aircraft_invoice
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataAircraftInvoice.class)
public class DataAircraftInvoiceVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 关联图片表id
     */
    @ExcelProperty(value = "关联图片表id")
    private String fileId;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 购方单位
     */
    @ExcelProperty(value = "购方单位")
    private String buyerName;

    /**
     * 纳税人识别号
     */
    @ExcelProperty(value = "纳税人识别号")
    private String buyerTaxid;

    /**
     * 种类
     */
    @ExcelProperty(value = "种类")
    private String category;

    /**
     * 校验码
     */
    @ExcelProperty(value = "校验码")
    private String checkCode;

    /**
     * 所属城市
     */
    @ExcelProperty(value = "所属城市")
    private String city;

    /**
     * 发票代码
     */
    @ExcelProperty(value = "发票代码")
    private String invoiceCode;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private Date invoiceDate;

    /**
     * 地区(省)
     */
    @ExcelProperty(value = "地区(省)")
    private String province;

    /**
     * 销方单位名称
     */
    @ExcelProperty(value = "销方单位名称")
    private String sellerName;

    /**
     * 销方税号
     */
    @ExcelProperty(value = "销方税号")
    private String sellerTaxid;

    /**
     * 总价
     */
    @ExcelProperty(value = "总价")
    private Long invoiceTotal;

    /**
     * 是否为浙江/广东通用机打电子发票
     */
    @ExcelProperty(value = "是否为浙江/广东通用机打电子发票")
    private String electronicMark;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    @ExcelProperty(value = "是否有公司印章(0: 没有; 1: 有)")
    private String companySeal;

    /**
     * 大写合计金额
     */
    @ExcelProperty(value = "大写合计金额")
    private String moneyUppercase;

    /**
     * 税前金额
     */
    @ExcelProperty(value = "税前金额")
    private Long pretaxAmount;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 税务云token
     */
    @ExcelProperty(value = "税务云token")
    private String saveToken;

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

    /**
     * 置信度
     */
    @ExcelProperty(value = "置信度")
    private String confidence;

    /**
     * 查验结果
     */
    @ExcelProperty(value = "查验结果")
    private String checkResult;

    /**
     * 入台账标识
     */
    @ExcelProperty(value = "入台账标识")
    private String pushBusinessInfoFlag;


}
