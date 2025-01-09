package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataDidiItineraryDetails;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 滴滴行程单明细视图对象 data_didi_itinerary_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataDidiItineraryDetails.class)
public class DataDidiItineraryDetailsVo implements Serializable {

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
     * 车型
     */
    @ExcelProperty(value = "车型")
    private String carType;

    /**
     * 城市
     */
    @ExcelProperty(value = "城市")
    private String city;

    /**
     * 里程（公里）
     */
    @ExcelProperty(value = "里程", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "公=里")
    private String mileage;

    /**
     * 终点
     */
    @ExcelProperty(value = "终点")
    private String stationGetOff;

    /**
     * 起点
     */
    @ExcelProperty(value = "起点")
    private String stationGetOn;

    /**
     * 下单时间
     */
    @ExcelProperty(value = "下单时间")
    private String timeOrder;

    /**
     * 上车时间
     */
    @ExcelProperty(value = "上车时间")
    private String timeGetOn;

    /**
     * 下车时间
     */
    @ExcelProperty(value = "下车时间")
    private String timeGetOff;

    /**
     * 服务商
     */
    @ExcelProperty(value = "服务商")
    private String producer;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String invoiceTotal;

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
