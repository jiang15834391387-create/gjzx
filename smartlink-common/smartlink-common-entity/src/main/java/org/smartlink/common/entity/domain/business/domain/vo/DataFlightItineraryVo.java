package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 航空电子行程单视图对象 data_flight_itinerary
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataFlightItinerary.class)
public class DataFlightItineraryVo implements Serializable {

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
     * 旅客姓名
     */
    @ExcelProperty(value = "旅客姓名")
    private String userName;

    /**
     * 销售单位代号
     */
    @ExcelProperty(value = "销售单位代号")
    private String agentCode;

    /**
     * 民航发展基金
     */
    @ExcelProperty(value = "民航发展基金")
    private String caacDevelopmentFund;

    /**
     * 校验码
     */
    @ExcelProperty(value = "校验码")
    private String checkCode;

    /**
     * 填开日期
     */
    @ExcelProperty(value = "填开日期")
    private String invoiceDate;

    /**
     * 票价
     */
    @ExcelProperty(value = "票价")
    private String fare;

    /**
     * 燃油附加费
     */
    @ExcelProperty(value = "燃油附加费")
    private String fuelSurcharge;

    /**
     * 保险费
     */
    @ExcelProperty(value = "保险费")
    private String insurance;

    /**
     * 国内国际标签
     */
    @ExcelProperty(value = "国内国际标签")
    private String internationalFlag;

    /**
     * 填开单位
     */
    @ExcelProperty(value = "填开单位")
    private String issueBy;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 税额
     */
    @ExcelProperty(value = "税额")
    private String tax;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String invoiceTotal;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 印刷序号
     */
    @ExcelProperty(value = "印刷序号")
    private String printNumber;

    /**
     * 签注
     */
    @ExcelProperty(value = "签注")
    private String endorsement;

    /**
     * 1 电子票标记（仅在电子票时返回）
     */
    @ExcelProperty(value = "1 电子票标记", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String electronicMark;

    /**
     * 出票状态（仅在电子票时返回）
     */
    @ExcelProperty(value = "出票状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String issuingStatus;

    /**
     * 二维码（仅在电子票时返回）
     */
    @ExcelProperty(value = "二维码", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String qrcode;

    /**
     * 收据号码（仅在电子票时返回）
     */
    @ExcelProperty(value = "收据号码", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String receiptNumber;

    /**
     * GP订单号
     */
    @ExcelProperty(value = "GP订单号")
    private String numberOfGpOrder;

    /**
     * 提示信息（仅在电子票时返回）
     */
    @ExcelProperty(value = "提示信息", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String promptInformation;

    /**
     * 其他税费（仅在电子票时返回）
     */
    @ExcelProperty(value = "其他税费", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String otherTaxes;

    /**
     * 购买方名称（仅在电子票时返回）
     */
    @ExcelProperty(value = "购买方名称", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String buyer;

    /**
     * 销售方名称（仅在电子票时返回）
     */
    @ExcelProperty(value = "销售方名称", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String seller;

    /**
     * 购买方纳税人识别号（仅在电子票时返回）
     */
    @ExcelProperty(value = "购买方纳税人识别号", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String buyerTaxId;

    /**
     * 增值税税率（仅在电子票时返回）
     */
    @ExcelProperty(value = "增值税税率", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String taxRate;

    /**
     * 商务类型 售 或 退
     */
    @ExcelProperty(value = "商务类型 售 或 退")
    private String typeOfBusiness;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

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
