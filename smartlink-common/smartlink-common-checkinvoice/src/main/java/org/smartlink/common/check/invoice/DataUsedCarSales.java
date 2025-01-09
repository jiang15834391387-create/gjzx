package org.smartlink.common.check.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.common.check.doman.InvoiceBaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 二手车销售统一发票对象 data_used_car_sales
 *
 */
@Data
@TableName("data_used_car_sales")
public class DataUsedCarSales extends InvoiceBaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private String id;
    /**
     * 经营、拍卖单位
     */
    private String businessUnit;
    /**
     * 经营、拍卖单位纳税人识别号
     */
    private String businessUnitTaxNo;

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 经营、拍卖单位地址
     */
    private String businessUnitAddress;
    /**
     * 开户银行及账号
     */
    private String sellerAccount;
    /**
     * 经营、拍卖单位电话
     */
    private String businessUnitPhone;
    /**
     * 买方单位/个人
     */
    private String buyerName;
    /**
     * 买方单位代码
     */
    private String buyerId;
    /**
     * 买方电话
     */
    private String buyerPhone;
    /**
     * 买房单位地址
     */
    private String buyerAddress;
    /**
     * 车架号/车辆识别代码
     */
    private String carCode;
    /**
     * 发动机号码
     */
    private String carEngineCode;
    /**
     * 厂牌型号
     */
    private String carModel;
    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    private String checkInvoice;
    /**
     * 所属城市
     */
    private String city;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 二手车市场名称
     */
    private String companyName;
    /**
     * 二手车市场纳税人识别号
     */
    private String companyTaxId;
    /**
     * 发票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 图片表id
     */
    private String fileId;
    /**
     * 二手车市场开户银行及账号
     */
    private String lemonMarketBankAndCcount;
    /**
     * 二手车市场电话
     */
    private String lemonMarketPhone;
    /**
     * 二手车市场地址
     */
    private String lemonMarketAddress;
    /**
     * 车牌号
     */
    private String licensePlate;
    /**
     * 发票号码
     */
    private String invoiceNumber;
    /**
     * 省
     */
    private String province;
    /**
     * 登记证号
     */
    private String regisTrationNumber;
    /**
     * 销售方名称
     */
    private String sellerName;
    /**
     * 销售方税号
     */
    private String sellerId;
    /**
     * 销售方手机
     */
    private String sellerPhone;
    /**
     * 销售方地址
     */
    private String sellerAddress;
    /**
     * 价税合计
     */
    private BigDecimal invoiceTotal;
    /**
     * 发票联
     */
    private String pageNumber;
    /**
     * 发票联次
     */
    private String invoiceSheet;
    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;
    /**
     * 盖章存在性判断
     */
    private String companySeal;
    /**
     * 大写金额，（加税合计）
     */
    private String totalUppercase;
    /**
     * 机器码
     */
    private String machineCode;
    /**
     * 税控码
     */
    private String taxCode;
    /**
     * 转入地车辆管理所名称
     */
    private String vehicleManageName;
    /**
     * 车辆类型
     */
    private String vehicleType;
    /**
     * 税务云token
     */
    private String saveToken;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 置信度
     */
    private String confidence;
    /**
     * 台账推送业务系统成功标识
     */
    private String pushBusinessInfoFlag;

}
