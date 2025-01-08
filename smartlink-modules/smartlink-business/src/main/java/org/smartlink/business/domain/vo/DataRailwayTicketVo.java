package org.smartlink.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.business.domain.DataRailwayTicket;
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
 * 火车票视图对象 data_railway_ticket
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataRailwayTicket.class)
public class DataRailwayTicketVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 标题
     */
    @ExcelProperty(value = "标题")
    private String title;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private Date invoiceDate;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 座位等级
     */
    @ExcelProperty(value = "座位等级")
    private String seat;

    /**
     * 座位号
     */
    @ExcelProperty(value = "座位号")
    private String seatNum;

    /**
     * 检票口
     */
    @ExcelProperty(value = "检票口")
    private String wicket;

    /**
     * 取票地址
     */
    @ExcelProperty(value = "取票地址")
    private String ticketAddress;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String idNumber;

    /**
     * 序列号
     */
    @ExcelProperty(value = "序列号")
    private String serialNumber;

    /**
     * 到达车站
     */
    @ExcelProperty(value = "到达车站")
    private String stationGetOff;

    /**
     * 启始车站
     */
    @ExcelProperty(value = "启始车站")
    private String stationGetOn;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private String invoiceTime;

    /**
     * 合计
     */
    @ExcelProperty(value = "合计")
    private Long invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 车次号
     */
    @ExcelProperty(value = "车次号")
    private String trainNumber;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @ExcelProperty(value = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]")
    private String region;

    /**
     * 睿真token
     */
    @ExcelProperty(value = "睿真token")
    private String saveToken;

    /**
     * 商务类型  退 售 改签 退差
     */
    @ExcelProperty(value = "商务类型  退 售 改签 退差")
    private String typeOfBusiness;

    /**
     * 退差内容
     */
    @ExcelProperty(value = "退差内容")
    private String refundContent;

    /**
     * 售票内容
     */
    @ExcelProperty(value = "售票内容")
    private String ticketContent;

    /**
     * 购买方名称 （仅在电子凭证时返回）
     */
    @ExcelProperty(value = "购买方名称 ", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String buyer;

    /**
     * 社会统一信用代码 （仅在电子凭证时返回）
     */
    @ExcelProperty(value = "社会统一信用代码 ", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String buyerTaxId;

    /**
     * 原发票号码（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "原发票号码", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String numberOfOriginalInvoice;

    /**
     * 空调类型（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "空调类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String airConditioning;

    /**
     * 凭证种类（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "凭证种类", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String typeOfVoucher;

    /**
     * 火车票类型（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "火车票类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String typeOfRailwayTicket;

    /**
     * 折扣标志（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "折扣标志", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String discountMark;

    /**
     * 退票金额（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "退票金额", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private Long amountRefunded;

    /**
     * 原火车票价款（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "原火车票价款", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private Long fareOfOriginalRailwayTicket;

    /**
     * 原火车票起点站（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "原火车票起点站", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String departureStationOfOriginalRailwayTicket;

    /**
     * 原火车票终点站（仅在电子凭证时返回）
     */
    @ExcelProperty(value = "原火车票终点站", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String destinationStationOfOriginalRailwayTicket;

    /**
     * 购买方地址电话 （仅在电子凭证时返回）
     */
    @ExcelProperty(value = "购买方地址电话 ", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String buyerAddrTel;

    /**
     * 购买方银行及账号 （仅在电子凭证时返回）
     */
    @ExcelProperty(value = "购买方银行及账号 ", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子凭证时返回")
    private String buyerBankAccount;

    /**
     * 电子票标记
     */
    @ExcelProperty(value = "电子票标记")
    private String electronicMark;

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
