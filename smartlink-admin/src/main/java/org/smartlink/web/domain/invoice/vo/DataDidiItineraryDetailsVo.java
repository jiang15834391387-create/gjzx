package org.smartlink.web.domain.invoice.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import org.smartlink.web.annotation.ExcelDictFormat;

import java.math.BigDecimal;


/**
 * 滴滴行程单明细视图对象 data_didi_itinerary_details
 *
 * @author L
 * @date
 */
@Data
@ApiModel("滴滴行程单明细视图对象")
@ExcelIgnoreUnannotated
public class DataDidiItineraryDetailsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 车型
     */
    @ExcelProperty(value = "车型")
    @ApiModelProperty("车型")
    private String carType;

    /**
     * 城市
     */
    @ExcelProperty(value = "城市")
    @ApiModelProperty("城市")
    private String city;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    @ApiModelProperty("图片表主键")
    private String fileId;

    /**
     * 里程（公里）
     */
    @ExcelProperty(value = "里程", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "公=里")
    @ApiModelProperty("里程（公里）")
    private String mileage;

    /**
     * 终点
     */
    @ExcelProperty(value = "终点")
    @ApiModelProperty("终点")
    private String stationGetOff;

    /**
     * 起点
     */
    @ExcelProperty(value = "起点")
    @ApiModelProperty("起点")
    private String stationGetOn;

    /**
     * 下单时间
     */
    @ExcelProperty(value = "下单时间")
    @ApiModelProperty("下单时间")
    private String timeOrder;

    /**
     * 上车时间
     */
    @ExcelProperty(value = "上车时间")
    @ApiModelProperty("上车时间")
    private String timeGetOn;

    /**
     * 下车时间
     */
    @ExcelProperty(value = "下车时间")
    @ApiModelProperty("下车时间")
    private String timeGetOff;

    /**
     * 服务商
     */
    @ExcelProperty(value = "服务商")
    @ApiModelProperty("服务商")
    private String producer;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    @ApiModelProperty("总计")
    private BigDecimal invoiceTotal;

    /**
     * 税务云token
     */
    @ExcelProperty(value = "税务云token")
    @ApiModelProperty("税务云token")
    private String saveToken;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    @ApiModelProperty("是否删除标识 0-不删除  1-删除")
    private String deleteFlag;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ApiModelProperty("备注")
    private String remark;


}
