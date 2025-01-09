package org.smartlink.common.entity.domain.business.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 机动车销售发票业务对象 data_motor_vehicle_sale
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataMotorVehicleSale.class, reverseConvertGenerate = false)
public class DataMotorVehicleSaleBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 购买方名称
     */
    @NotBlank(message = "购买方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerName;

    /**
     * 购买方税号
     */
    @NotBlank(message = "购买方税号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerId;

    /**
     * 车辆识别代码
     */
    @NotBlank(message = "车辆识别代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carCode;

    /**
     * 发动机号码
     */
    @NotBlank(message = "发动机号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carEngineCode;

    /**
     * 厂牌型号
     */
    @NotBlank(message = "厂牌型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carModel;

    /**
     * 合格证号
     */
    @NotBlank(message = "合格证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String certificateNumber;

    /**
     * 进口证明书号
     */
    @NotBlank(message = "进口证明书号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String certificateOfImport;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @NotBlank(message = "是否查验标识，（0查验失败，1查验成功）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkInvoice;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空", groups = { AddGroup.class, EditGroup.class })
    private String city;

    /**
     * 发票代码
     */
    @NotBlank(message = "发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 商检单号
     */
    @NotBlank(message = "商检单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String commodityInspectionNo;

    /**
     * 开票日期
     */
    @NotBlank(message = "开票日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 开票人
     */
    @NotBlank(message = "开票人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String drawer;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 限乘人数
     */
    @NotBlank(message = "限乘人数不能为空", groups = { AddGroup.class, EditGroup.class })
    private String limitedPeopleCount;

    /**
     * 机打代码
     */
    @NotBlank(message = "机打代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String machineCode;

    /**
     * 机打号码
     */
    @NotBlank(message = "机打号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String machineNumber;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 税前金额
     */
    @NotBlank(message = "税前金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String preTaxAmount;

    /**
     * 产地
     */
    @NotBlank(message = "产地不能为空", groups = { AddGroup.class, EditGroup.class })
    private String produceArea;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 省
     */
    @NotBlank(message = "省不能为空", groups = { AddGroup.class, EditGroup.class })
    private String province;

    /**
     * 销售方名称
     */
    @NotBlank(message = "销售方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seller;

    /**
     * 销售方地址
     */
    @NotBlank(message = "销售方地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerAddress;

    /**
     * 销售单位开户账号
     */
    @NotBlank(message = "销售单位开户账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerBankAccount;

    /**
     * 销售单位开户银行
     */
    @NotBlank(message = "销售单位开户银行不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerBankName;

    /**
     * 销售方手机号
     */
    @NotBlank(message = "销售方手机号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerPhone;

    /**
     * 销售方税号
     */
    @NotBlank(message = "销售方税号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerTaxid;

    /**
     * 税额
     */
    @NotBlank(message = "税额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tax;

    /**
     * 主管税务机关
     */
    @NotBlank(message = "主管税务机关不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxAuthorities;

    /**
     * 主管税务机关代码
     */
    @NotBlank(message = "主管税务机关代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxAuthoritiesCode;

    /**
     * 完税凭证号码
     */
    @NotBlank(message = "完税凭证号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxPaymentCertificateNo;

    /**
     * 税率
     */
    @NotBlank(message = "税率不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxRate;

    /**
     * 税控码
     */
    @NotBlank(message = "税控码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxCode;

    /**
     * 二维码
     */
    @NotBlank(message = "二维码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String qrCode;

    /**
     * 吨位
     */
    @NotBlank(message = "吨位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tonnage;

    /**
     * 总计
     */
    @NotBlank(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

    /**
     * 是否有公司印章
     */
    @NotBlank(message = "是否有公司印章不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companySeal;

    /**
     * 发票联
     */
    @NotBlank(message = "发票联不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pageNumber;

    /**
     * 发票联次
     */
    @NotBlank(message = "发票联次不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceSheet;

    /**
     * 车辆类型
     */
    @NotBlank(message = "车辆类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String vehicleType;

    /**
     * 数电票号码
     */
    @NotBlank(message = "数电票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicNumber;

    /**
     * 电子票标记（仅在电子票时返回）
     */
    @NotBlank(message = "电子票标记（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
