package org.smartlink.server.nc.domain.invoice.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 航空电子行程单明细视图对象 data_flights
 *
 * @author L
 * @date
 */
@Data
@ApiModel("航空电子行程单明细视图对象")
@ExcelIgnoreUnannotated
public class DataFlightsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 承运人
     */
    @ExcelProperty(value = "承运人")
    @ApiModelProperty("承运人")
    private String carrier;

    /**
     * 乘机日期
     */
    @ExcelProperty(value = "乘机日期")
    @ApiModelProperty("乘机日期")
    private Date invoiceDate;

    /**
     * 图片表主键
     */
    @ExcelProperty(value = "图片表主键")
    @ApiModelProperty("图片表主键")
    private String fileId;

    /**
     * 航班号
     */
    @ExcelProperty(value = "航班号")
    @ApiModelProperty("航班号")
    private String flightNumber;

    /**
     * 出发站
     */
    @ExcelProperty(value = "出发站")
    @ApiModelProperty("出发站")
    private String stationGetOn;

    /**
     * 座位等级
     */
    @ExcelProperty(value = "座位等级")
    @ApiModelProperty("座位等级")
    private String seat;

    /**
     * 客票生效日期
     */
    @ExcelProperty(value = "客票生效日期")
    @ApiModelProperty("客票生效日期")
    private Date effectiveDate;

    /**
     * 有效截至日期
     */
    @ExcelProperty(value = "有效截至日期")
    @ApiModelProperty("有效截至日期")
    private Date expiryDate;

    /**
     * 舱位等级
     */
    @ExcelProperty(value = "舱位等级")
    @ApiModelProperty("舱位等级")
    private String spaceLevel;

    /**
     * 免费行李
     */
    @ExcelProperty(value = "免费行李")
    @ApiModelProperty("免费行李")
    private String allow;

    /**
     * 舱位等级
     */
    @ExcelProperty(value = "舱位等级")
    @ApiModelProperty("舱位等级")
    private String fareBasis;

    /**
     * 身份证号
     */
    @ExcelProperty(value = "身份证号")
    @ApiModelProperty("身份证号")
    private String userId;

    /**
     * 乘机人姓名
     */
    @ExcelProperty(value = "乘机人姓名")
    @ApiModelProperty("乘机人姓名")
    private String userName;

    /**
     * 乘机时间
     */
    @ExcelProperty(value = "乘机时间")
    @ApiModelProperty("乘机时间")
    private String invoiceTime;

    /**
     * 到达站
     */
    @ExcelProperty(value = "到达站")
    @ApiModelProperty("到达站")
    private String stationGetOff;

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

    /**
     * 置信度
     */
    @ExcelProperty(value = "置信度")
    @ApiModelProperty("置信度")
    private String confidence;


}
