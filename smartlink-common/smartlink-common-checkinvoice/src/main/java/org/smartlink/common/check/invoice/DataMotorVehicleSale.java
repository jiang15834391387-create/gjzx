package org.smartlink.common.check.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.common.check.doman.InvoiceBaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 机动车销售发票对象 data_motor_vehicle_sale
 *
 * @author ruoyi
 * @date 2022-04-01
 */
@Data
@TableName("data_motor_vehicle_sale")
public class DataMotorVehicleSale extends InvoiceBaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 购买方名称
     */
    private String buyerName;
    /**
     * 购买方税号
     */
    private String buyerId;
    /**
     * 车辆识别代码
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
     * 合格证号
     */
    private String certificateNumber;
    /**
     * 进口证明书号
     */
    private String certificateOfImport;
    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    private String checkInvoice;
    /**
     * 城市
     */
    private String city;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 商检单号
     */
    private String commodityInspectionNo;
    /**
     * 开票日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 开票人
     */
    private String drawer;
    /**
     * 图片表id
     */
    private String fileId;
    /**
     * 限乘人数
     */
    private Long limitedPeopleCount;
    /**
     * 机打代码
     */
    private String machineCode;
    /**
     * 机打号码
     */
    private String machineNumber;
    /**
     * 发票号码
     */
    private String invoiceNumber;
    /**
     * 税前金额
     */
    private BigDecimal preTaxAmount;
    /**
     * 产地
     */
    private String produceArea;
    /**
     * 省
     */
    private String province;
    /**
     * 销售方名称
     */
    private String seller;
    /**
     * 销售方地址
     */
    private String sellerAddress;
    /**
     * 销售单位开户账号
     */
    private String sellerBankAccount;
    /**
     * 销售单位开户银行
     */
    private String sellerBankName;
    /**
     * 销售方手机号
     */
    private String sellerPhone;
    /**
     * 销售方税号
     */
    private String sellerTaxid;
    /**
     * 税额
     */
    private BigDecimal tax;
    /**
     * 主管税务机关
     */
    private String taxAuthorities;
    /**
     * 主管税务机关代码
     */
    private String taxAuthoritiesCode;
    /**
     * 完税凭证号码
     */
    private String taxPaymentCertificateNo;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 税控码
     */
    private String taxCode;
    /**
     * 二维码
     */
    private String qrCode;
    /**
     * 吨位
     */
    private String tonnage;
    /**
     * 总计
     */
    private BigDecimal invoiceTotal;
    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;
    /**
     * 盖章存在性判断
     */
    private String companySeal;
    /**
     * 发票联
     */
    private String pageNumber;
    /**
     * 发票联次
     */
    private String invoiceSheet;
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
