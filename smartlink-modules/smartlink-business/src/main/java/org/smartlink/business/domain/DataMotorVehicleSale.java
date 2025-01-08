package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 机动车销售发票对象 data_motor_vehicle_sale
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_motor_vehicle_sale")
public class DataMotorVehicleSale extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 标题
     */
    private String title;

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
    private String invoiceDate;

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
    private String limitedPeopleCount;

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
    private String preTaxAmount;

    /**
     * 产地
     */
    private String produceArea;

    /**
     * 发票消费类型
     */
    private String kind;

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
    private String tax;

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
    private String invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 是否有公司印章
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
     * 数电票号码
     */
    private String electronicNumber;

    /**
     * 电子票标记（仅在电子票时返回）
     */
    private String electronicMark;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
