package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 增值税发票明细视图对象 data_ocr_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataOcrDetails.class)
public class DataOcrDetailsVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 主表id
     */
    @ExcelProperty(value = "主表id")
    private String ocrId;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 金额
     */
    @ExcelProperty(value = "金额")
    private String detailAmount;

    /**
     * 数量
     */
    @ExcelProperty(value = "数量")
    private String detailsCount;

    /**
     * 明细编号
     */
    @ExcelProperty(value = "明细编号")
    private String detailNo;

    /**
     * 明细名称
     */
    @ExcelProperty(value = "明细名称")
    private String name;

    /**
     * 商品编码
     */
    @ExcelProperty(value = "商品编码")
    private String commodityCode;

    /**
     * 货物或应税劳务名称
     */
    @ExcelProperty(value = "货物或应税劳务名称")
    private String commodityName;

    /**
     * 单价
     */
    @ExcelProperty(value = "单价")
    private String price;

    /**
     * 税率
     */
    @ExcelProperty(value = "税率")
    private String taxRate;

    /**
     * 规格型号
     */
    @ExcelProperty(value = "规格型号")
    private String standard;

    /**
     * 税额
     */
    @ExcelProperty(value = "税额")
    private String tax;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private String unit;

    /**
     * 通行日起止
     */
    @ExcelProperty(value = "通行日起止")
    private String currentDateEnd;

    /**
     * 通行日起
     */
    @ExcelProperty(value = "通行日起")
    private String currentDateStart;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String licensePlateNum;

    /**
     * 车辆类型
     */
    @ExcelProperty(value = "车辆类型")
    private String vehicleType;

    /**
     * 用车时间
     */
    @ExcelProperty(value = "用车时间 ")
    private String usageTime;

    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    @ExcelProperty(value = "特殊政策标识", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=-正常票,1=-免税,2=-不征税,3=-零税率")
    private String specialMark;

    /**
     * 建筑服务发生地（ service_type为建筑服务，返回此字段）
     */
    @ExcelProperty(value = "建筑服务发生地", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为建筑服务，返回此字段")
    private String placeOfBuildingService;

    /**
     * 建筑项目名称（ service_type为建筑服务，返回此字段）
     */
    @ExcelProperty(value = "建筑项目名称", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为建筑服务，返回此字段")
    private String buildingName;

    /**
     * 产权证书/不动产权证号（service_type为不动产经营租赁服务，返回字段）
     */
    @ExcelProperty(value = "产权证书/不动产权证号", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为不动产经营租赁服务，返回字段")
    private String titleCertificateNumber;

    /**
     * 面积单位（service_type为不动产经营租赁服务，返回字段）
     */
    @ExcelProperty(value = "面积单位", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为不动产经营租赁服务，返回字段")
    private String areaUnit;

    /**
     * 运输工具类型（service_type为货物运输服务，返回此字段）
     */
    @ExcelProperty(value = "运输工具类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为货物运输服务，返回此字段")
    private String transportType;

    /**
     * 运输工具牌号（service_type为货物运输服务，返回此字段）
     */
    @ExcelProperty(value = "运输工具牌号", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为货物运输服务，返回此字段")
    private String transportNumber;

    /**
     * 起始地（service_type为货物运输服务，返回此字段）
     */
    @ExcelProperty(value = "起始地", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为货物运输服务，返回此字段")
    private String from;

    /**
     * 到达地（service_type为货物运输服务，返回此字段）
     */
    @ExcelProperty(value = "到达地", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为货物运输服务，返回此字段")
    private String to;

    /**
     * 运输货物名称（service_type为货物运输服务，返回此字段）
     */
    @ExcelProperty(value = "运输货物名称", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为货物运输服务，返回此字段")
    private String goodsName;

    /**
     * 出行人（service_type为旅客运输服务，返回此字段）
     */
    @ExcelProperty(value = "出行人", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为旅客运输服务，返回此字段")
    private String passenger;

    /**
     * 出行人（service_type为旅客运输服务，返回此字段）
     */
    @ExcelProperty(value = "有效身份证号", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "有效身份证号（service_type为旅客运输服务，返回此字段）")
    private String userId;

    /**
     * 出行日期（service_type为旅客运输服务，返回此字段）
     */
    @ExcelProperty(value = "出行日期", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为旅客运输服务，返回此字段")
    private String travelDate;

    /**
     * 等级（service_type为旅客运输服务，返回此字段）
     */
    @ExcelProperty(value = "等级", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "s=ervice_type为旅客运输服务，返回此字段")
    private String seat;

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
