package org.smartlink.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.business.domain.DataPassengerCar;
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
 * 客运汽车票视图对象 data_passenger_car
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataPassengerCar.class)
public class DataPassengerCarVo implements Serializable {

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
     * 发票代码
     */
    @ExcelProperty(value = "发票代码")
    private String invoiceCode;

    /**
     * 日期
     */
    @ExcelProperty(value = "日期")
    private Date invoiceDate;

    /**
     * 出发车站
     */
    @ExcelProperty(value = "出发车站")
    private String stationGeton;

    /**
     * 达到车站
     */
    @ExcelProperty(value = "达到车站")
    private String stationGetoff;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 时间
     */
    @ExcelProperty(value = "时间")
    private String invoiceTime;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private Long invoiceTotal;

    /**
     * 姓名
     */
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    private String userId;

    /**
     * 是否盖章（0: 没有; 1: 有）
     */
    @ExcelProperty(value = "是否盖章", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=:,没=有;,1=:,有=")
    private String companySeal;

    /**
     * 车次
     */
    @ExcelProperty(value = "车次")
    private String busNumber;

    /**
     * 睿真token
     */
    @ExcelProperty(value = "睿真token")
    private String saveToken;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @ExcelProperty(value = "是否查验标识，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=查验失败，1查验成功")
    private String checkInvoice;

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
