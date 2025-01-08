package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataUsedCarSales;
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
 * 二手车销售统一发票视图对象 data_used_car_sales
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataUsedCarSales.class)
public class DataUsedCarSalesVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String  id;

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
     * 经营、拍卖单位
     */
    @ExcelProperty(value = "经营、拍卖单位")
    private String businessUnit;

    /**
     * 经营、拍卖单位纳税人识别号
     */
    @ExcelProperty(value = "经营、拍卖单位纳税人识别号")
    private String businessUnitTaxNo;

    /**
     * 经营、拍卖单位地址
     */
    @ExcelProperty(value = "经营、拍卖单位地址")
    private String businessUnitAddress;

    /**
     * 经营、拍卖单位开户银行、账号
     */
    @ExcelProperty(value = "经营、拍卖单位开户银行、账号")
    private String sellerAccount;

    /**
     * 经营、拍卖单位电话
     */
    @ExcelProperty(value = "经营、拍卖单位电话")
    private String businessUnitPhone;

    /**
     * 买方单位/个人
     */
    @ExcelProperty(value = "买方单位/个人")
    private String buyerName;

    /**
     * 买方单位代码
     */
    @ExcelProperty(value = "买方单位代码")
    private String buyerId;

    /**
     * 买方电话
     */
    @ExcelProperty(value = "买方电话")
    private String buyerPhone;

    /**
     * 买房单位地址
     */
    @ExcelProperty(value = "买房单位地址")
    private String buyerAddress;

    /**
     * 车架号/车辆识别代码
     */
    @ExcelProperty(value = "车架号/车辆识别代码")
    private String carCode;

    /**
     * 发动机号码
     */
    @ExcelProperty(value = "发动机号码")
    private String carEngineCode;

    /**
     * 厂牌型号
     */
    @ExcelProperty(value = "厂牌型号")
    private String carModel;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @ExcelProperty(value = "是否查验标识，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=查验失败，1查验成功")
    private String checkInvoice;

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
     * 二手车市场名称
     */
    @ExcelProperty(value = "二手车市场名称")
    private String companyName;

    /**
     * 二手车市场纳税人识别号
     */
    @ExcelProperty(value = "二手车市场纳税人识别号")
    private String companyTaxId;

    /**
     * 发票日期
     */
    @ExcelProperty(value = "发票日期")
    private String invoiceDate;

    /**
     * 二手车市场开户银行及账号
     */
    @ExcelProperty(value = "二手车市场开户银行及账号")
    private String lemonMarketBankAndCcount;

    /**
     * 二手车市场电话
     */
    @ExcelProperty(value = "二手车市场电话")
    private String lemonMarketPhone;

    /**
     * 二手车市场地址
     */
    @ExcelProperty(value = "二手车市场地址")
    private String lemonMarketAddress;

    /**
     * 车牌号
     */
    @ExcelProperty(value = "车牌号")
    private String licensePlate;

    /**
     * 车辆类型
     */
    @ExcelProperty(value = "车辆类型")
    private String carType;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 登记证号
     */
    @ExcelProperty(value = "登记证号")
    private String regisTrationNumber;

    /**
     * 销售方名称
     */
    @ExcelProperty(value = "销售方名称")
    private String sellerName;

    /**
     * 销售方单位代码/个人身份证号
     */
    @ExcelProperty(value = "销售方单位代码/个人身份证号")
    private String sellerId;

    /**
     * 销售方手机
     */
    @ExcelProperty(value = "销售方手机")
    private String sellerPhone;

    /**
     * 销售方地址
     */
    @ExcelProperty(value = "销售方地址")
    private String sellerAddress;

    /**
     * 价税合计
     */
    @ExcelProperty(value = "价税合计")
    private String invoiceTotal;

    /**
     * 发票联
     */
    @ExcelProperty(value = "发票联")
    private String pageNumber;

    /**
     * 发票联次
     */
    @ExcelProperty(value = "发票联次")
    private String invoiceSheet;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 盖章存在性判断
     */
    @ExcelProperty(value = "盖章存在性判断")
    private String companySeal;

    /**
     * 大写金额，（加税合计）
     */
    @ExcelProperty(value = "大写金额，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "加=税合计")
    private String totalUppercase;

    /**
     * 机器码
     */
    @ExcelProperty(value = "机器码")
    private String machineCode;

    /**
     * 税控码
     */
    @ExcelProperty(value = "税控码")
    private String taxCode;

    /**
     * 转入地车辆管理所名称
     */
    @ExcelProperty(value = "转入地车辆管理所名称")
    private String vehicleManageName;

    /**
     * 车辆类型
     */
    @ExcelProperty(value = "车辆类型")
    private String vehicleType;

    /**
     * 机器编号
     */
    @ExcelProperty(value = "机器编号")
    private String machineId;

    /**
     * 机打号码
     */
    @ExcelProperty(value = "机打号码")
    private String machineNumber;

    /**
     * 数电票号码
     */
    @ExcelProperty(value = "数电票号码")
    private String electronicNumber;

    /**
     * 1 电子票标记（仅在电子票时返回）
     */
    @ExcelProperty(value = "1 电子票标记", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String electronicMark;

    /**
     * 开票人
     */
    @ExcelProperty(value = "开票人")
    private String issuer;

    /**
     * 转入地车辆管理所名称
     */
    @ExcelProperty(value = "转入地车辆管理所名称")
    private String destinationDepartmentOfMotorVehicles;

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
