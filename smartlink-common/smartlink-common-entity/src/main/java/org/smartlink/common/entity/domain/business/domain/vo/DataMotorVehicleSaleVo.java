package org.smartlink.common.entity.domain.business.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;

import java.io.Serial;
import java.io.Serializable;


/**
 * 机动车销售发票视图对象 data_motor_vehicle_sale
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataMotorVehicleSale.class)
public class DataMotorVehicleSaleVo implements Serializable {

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
     * 购买方名称
     */
    @ExcelProperty(value = "购买方名称")
    private String buyerName;

    /**
     * 购买方税号
     */
    @ExcelProperty(value = "购买方税号")
    private String buyerId;

    /**
     * 车辆识别代码
     */
    @ExcelProperty(value = "车辆识别代码")
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
     * 合格证号
     */
    @ExcelProperty(value = "合格证号")
    private String certificateNumber;

    /**
     * 进口证明书号
     */
    @ExcelProperty(value = "进口证明书号")
    private String certificateOfImport;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @ExcelProperty(value = "是否查验标识，", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=查验失败，1查验成功")
    private String checkInvoice;

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
     * 商检单号
     */
    @ExcelProperty(value = "商检单号")
    private String commodityInspectionNo;

    /**
     * 开票日期
     */
    @ExcelProperty(value = "开票日期")
    private String invoiceDate;

    /**
     * 开票人
     */
    @ExcelProperty(value = "开票人")
    private String drawer;

    /**
     * 图片表id
     */
    @ExcelProperty(value = "图片表id")
    private String fileId;

    /**
     * 限乘人数
     */
    @ExcelProperty(value = "限乘人数")
    private String limitedPeopleCount;

    /**
     * 机打代码
     */
    @ExcelProperty(value = "机打代码")
    private String machineCode;

    /**
     * 机打号码
     */
    @ExcelProperty(value = "机打号码")
    private String machineNumber;

    /**
     * 发票号码
     */
    @ExcelProperty(value = "发票号码")
    private String invoiceNumber;

    /**
     * 税前金额
     */
    @ExcelProperty(value = "税前金额")
    private String preTaxAmount;

    /**
     * 产地
     */
    @ExcelProperty(value = "产地")
    private String produceArea;

    /**
     * 发票消费类型
     */
    @ExcelProperty(value = "发票消费类型")
    private String kind;

    /**
     * 省
     */
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 总计 大写
     */
    private String totalCn;

    /**
     * 销售方名称
     */
    @ExcelProperty(value = "销售方名称")
    private String seller;

    /**
     * 销售方地址
     */
    @ExcelProperty(value = "销售方地址")
    private String sellerAddress;

    /**
     * 销售单位开户账号
     */
    @ExcelProperty(value = "销售单位开户账号")
    private String sellerBankAccount;

    /**
     * 销售单位开户银行
     */
    @ExcelProperty(value = "销售单位开户银行")
    private String sellerBankName;

    /**
     * 销售方手机号
     */
    @ExcelProperty(value = "销售方手机号")
    private String sellerPhone;

    /**
     * 图片旋转角度
     */
    @ExcelProperty(value = "图片旋转角度")
    private String orientation;

    /**
     * 销售方税号
     */
    @ExcelProperty(value = "销售方税号")
    private String sellerTaxid;

    /**
     * 税额
     */
    @ExcelProperty(value = "税额")
    private String tax;

    /**
     * 主管税务机关
     */
    @ExcelProperty(value = "主管税务机关")
    private String taxAuthorities;

    /**
     * 主管税务机关代码
     */
    @ExcelProperty(value = "主管税务机关代码")
    private String taxAuthoritiesCode;

    /**
     * 完税凭证号码
     */
    @ExcelProperty(value = "完税凭证号码")
    private String taxPaymentCertificateNo;

    /**
     * 税率
     */
    @ExcelProperty(value = "税率")
    private String taxRate;

    /**
     * 税控码
     */
    @ExcelProperty(value = "税控码")
    private String taxCode;

    /**
     * 二维码
     */
    @ExcelProperty(value = "二维码")
    private String qrCode;

    /**
     * 吨位
     */
    @ExcelProperty(value = "吨位")
    private String tonnage;

    /**
     * 总计
     */
    @ExcelProperty(value = "总计")
    private String invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    @ExcelProperty(value = "发票专用章存在性判断")
    private String invoiceStamp;

    /**
     * 是否有公司印章
     */
    @ExcelProperty(value = "是否有公司印章")
    private String companySeal;

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
     * 车辆类型
     */
    @ExcelProperty(value = "车辆类型")
    private String vehicleType;

    /**
     * 数电票号码
     */
    @ExcelProperty(value = "数电票号码")
    private String electronicNumber;

    /**
     * 电子票标记（仅在电子票时返回）
     */
    @ExcelProperty(value = "电子票标记", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "仅=在电子票时返回")
    private String electronicMark;

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
