package org.smartlink.server.nc.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 二手车销售统一发票对象 data_used_car_sales
 *
 * @author L
 * @date
 */
@Data
@TableName("data_used_car_sales")
public class DataUsedCarSales extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    @FieldName(value="")
    private String id;
    /**
     * 经营、拍卖单位
     */
    @FieldName(value="经营、拍卖单位")
    private String businessUnit;
    /**
     * 经营、拍卖单位纳税人识别号
     */
    @FieldName(value="经营、拍卖单位纳税人识别号")
    private String businessUnitTaxNo;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 经营、拍卖单位地址
     */
    @FieldName(value="经营、拍卖单位地址")
    private String businessUnitAddress;
    /**
     * 开户银行及账号
     */
    @FieldName(value="开户银行及账号")
    private String sellerAccount;
    /**
     * 经营、拍卖单位电话
     */
    @FieldName(value="经营、拍卖单位电话")
    private String businessUnitPhone;
    /**
     * 买方单位/个人
     */
    @FieldName(value="买方单位/个人")
    private String buyerName;
    /**
     * 买方单位代码
     */
    @FieldName(value="买方单位代码")
    private String buyerId;
    /**
     * 买方电话
     */
    @FieldName(value="买方电话")
    private String buyerPhone;
    /**
     * 买房单位地址
     */
    @FieldName(value="买房单位地址")
    private String buyerAddress;
    /**
     * 车架号/车辆识别代码
     */
    @FieldName(value="车架号/车辆识别代码")
    private String carCode;
    /**
     * 发动机号码
     */
    @FieldName(value="发动机号码")
    private String carEngineCode;
    /**
     * 厂牌型号
     */
    @FieldName(value="厂牌型号")
    private String carModel;
    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @FieldName(value="是否查验标识")
    private String checkInvoice;
    /**
     * 所属城市
     */
    @FieldName(value="所属城市")
    private String city;
    /**
     * 发票代码
     */
    @FieldName(value="发票代码")
    private String invoiceCode;
    /**
     * 二手车市场名称
     */
    @FieldName(value="二手车市场名称")
    private String companyName;
    /**
     * 二手车市场纳税人识别号
     */
    @FieldName(value="二手车市场纳税人识别号")
    private String companyTaxId;
    /**
     * 发票日期
     */
    @FieldName(value="发票日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 图片表id
     */
    @FieldName(value="图片表id")
    private String fileId;
    /**
     * 二手车市场开户银行及账号
     */
    @FieldName(value="二手车市场开户银行及账号")
    private String lemonMarketBankAndCcount;
    /**
     * 二手车市场电话
     */
    @FieldName(value="二手车市场电话")
    private String lemonMarketPhone;
    /**
     * 二手车市场地址
     */
    @FieldName(value="二手车市场地址")
    private String lemonMarketAddress;
    /**
     * 车牌号
     */
    @FieldName(value="车牌号")
    private String licensePlate;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 省
     */
    @FieldName(value="省")
    private String province;
    /**
     * 登记证号
     */
    @FieldName(value="登记证号")
    private String regisTrationNumber;
    /**
     * 销售方名称
     */
    @FieldName(value="销售方名称")
    private String sellerName;
    /**
     * 销售方税号
     */
    @FieldName(value="销售方税号")
    private String sellerId;
    /**
     * 销售方手机
     */
    @FieldName(value="销售方手机")
    private String sellerPhone;
    /**
     * 销售方地址
     */
    @FieldName(value="销售方地址")
    private String sellerAddress;
    /**
     * 价税合计
     */
    @FieldName(value="价税合计")
    private BigDecimal invoiceTotal;
    /**
     * 发票联
     */
    @FieldName(value="发票联")
    private String pageNumber;
    /**
     * 发票联次
     */
    @FieldName(value="发票联次")
    private String invoiceSheet;
    /**
     * 发票专用章存在性判断
     */
    @FieldName(value="发票专用章存在性判断")
    private String invoiceStamp;
    /**
     * 盖章存在性判断
     */
    @FieldName(value="盖章存在性判断")
    private String companySeal;
    /**
     * 大写金额，（加税合计）
     */
    @FieldName(value="大写金额")
    private String totalUppercase;
    /**
     * 机器码
     */
    @FieldName(value="机器码")
    private String machineCode;
    /**
     * 税控码
     */
    @FieldName(value="税控码")
    private String taxCode;
    /**
     * 转入地车辆管理所名称
     */
    @FieldName(value="转入地车辆管理所名称")
    private String vehicleManageName;
    /**
     * 车辆类型
     */
    @FieldName(value="车辆类型")
    private String vehicleType;
    /**
     * 税务云token
     */
    @FieldName(value="税务云token")
    private String saveToken;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @FieldName(value="是否删除标识")
    private String deleteFlag;
    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;
    /**
     * 置信度
     */
    @FieldName(value="置信度")
    private String confidence;
    /**
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

    @TableField(exist = false)
    private String ncImageId;

    @FieldName(value = "坐标")
    private String coordinateStr;
    /**
     * 暂存状态
     */
    @FieldName(value = "暂存状态")
    private String isStaging;
}
