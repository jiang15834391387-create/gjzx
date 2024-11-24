package org.smartlink.web.domain.invoice.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import org.smartlink.web.annotation.ExcelDictFormat;

import java.math.BigDecimal;
import java.util.Date;


/**
 * ocr明细视图对象 data_ocr_details
 *
 * @author L
 * @date
 */
@Data
@ApiModel("ocr明细视图对象")
@ExcelIgnoreUnannotated
public class DataOcrDetailsVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    @ApiModelProperty("主键")
    private String id;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    @ApiModelProperty("金额")
    private BigDecimal detailAmount;

    /**
     *
     */
    @ExcelProperty(value = "")
    @ApiModelProperty("")
    private BigDecimal detailsCount;

    /**
     * 明细编号
     */
    @ExcelProperty(value = "明细编号")
    @ApiModelProperty("明细编号")
    private String detailNo;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    @ApiModelProperty("图片表id")
    private String fileId;

    /**
     * 明细名称
     */
    @ExcelProperty(value = "明细名称")
    @ApiModelProperty("明细名称")
    private String name;

    /**
     * 商品编码
     */
    @ExcelProperty(value = "商品编码")
    @ApiModelProperty("商品编码")
    private String commodityCode;

    /**
     * 货物或应税劳务名称
     */
    @ExcelProperty(value = "货物或应税劳务名称")
    @ApiModelProperty("货物或应税劳务名称")
    private String commodityName;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    @ApiModelProperty("单价")
    private BigDecimal price;

    /**
     * 税率
     */
    @ExcelProperty(value = "税率")
    @ApiModelProperty("税率")
    private String taxRate;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    @ApiModelProperty("规格型号")
    private String standard;

    /**
     * 税额
     */
    @ExcelProperty(value = "税额")
    @ApiModelProperty("税额")
    private BigDecimal tax;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    @ApiModelProperty("单位")
    private String unit;

    /**
     * 通行日起止
     */
    @ExcelProperty(value = "通行日起止")
    @ApiModelProperty("通行日起止")
    private Date currentDateEnd;

    /**
     * 通行日起
     */
    @ExcelProperty(value = "通行日起")
    @ApiModelProperty("通行日起")
    private Date currentDateStart;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    @ApiModelProperty("车牌号")
    private String licensePlateNum;

    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    @ExcelProperty(value = "特殊政策标识", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=-正常票,1=-免税,2=-不征税,3=-零税率")
    @ApiModelProperty("特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）")
    private String specialMark;

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
