package org.smartlink.business.domain.vo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.business.domain.DataTaxiTickets;
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
 * 出租车发票视图对象 data_taxi_tickets
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataTaxiTickets.class)
public class DataTaxiTicketsVo implements Serializable {

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
     * 城市
     */
    @ExcelProperty(value = "城市")
    private String city;

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
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String licensePlate;

    /**
     * 里程
     */
    @ExcelProperty(value = "里程")
    private String mileage;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 发票所属地区
     */
    @ExcelProperty(value = "发票所属地区")
    private String place;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 下车时间
     */
    @ExcelProperty(value = "下车时间")
    private String timeGetOff;

    /**
     * 上车时间
     */
    @ExcelProperty(value = "上车时间")
    private String timeGetOn;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private Long invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 燃油附加费
     */
    @ExcelProperty(value = "燃油附加费")
    private Long fuelSurcharge;

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
