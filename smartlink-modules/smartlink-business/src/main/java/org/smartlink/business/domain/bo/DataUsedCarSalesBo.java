package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataUsedCarSales;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 二手车销售统一发票业务对象 data_used_car_sales
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataUsedCarSales.class, reverseConvertGenerate = false)
public class DataUsedCarSalesBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String  id;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 经营、拍卖单位
     */
    @NotBlank(message = "经营、拍卖单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessUnit;

    /**
     * 经营、拍卖单位纳税人识别号
     */
    @NotBlank(message = "经营、拍卖单位纳税人识别号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessUnitTaxNo;

    /**
     * 经营、拍卖单位地址
     */
    @NotBlank(message = "经营、拍卖单位地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessUnitAddress;

    /**
     * 经营、拍卖单位开户银行、账号
     */
    @NotBlank(message = "经营、拍卖单位开户银行、账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerAccount;

    /**
     * 经营、拍卖单位电话
     */
    @NotBlank(message = "经营、拍卖单位电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessUnitPhone;

    /**
     * 买方单位/个人
     */
    @NotBlank(message = "买方单位/个人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerName;

    /**
     * 买方单位代码
     */
    @NotBlank(message = "买方单位代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerId;

    /**
     * 买方电话
     */
    @NotBlank(message = "买方电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerPhone;

    /**
     * 买房单位地址
     */
    @NotBlank(message = "买房单位地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerAddress;

    /**
     * 车架号/车辆识别代码
     */
    @NotBlank(message = "车架号/车辆识别代码不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @NotBlank(message = "是否查验标识，（0查验失败，1查验成功）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkInvoice;

    /**
     * 所属城市
     */
    @NotBlank(message = "所属城市不能为空", groups = { AddGroup.class, EditGroup.class })
    private String city;

    /**
     * 发票代码
     */
    @NotBlank(message = "发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 二手车市场名称
     */
    @NotBlank(message = "二手车市场名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companyName;

    /**
     * 二手车市场纳税人识别号
     */
    @NotBlank(message = "二手车市场纳税人识别号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companyTaxId;

    /**
     * 发票日期
     */
    @NotBlank(message = "发票日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 二手车市场开户银行及账号
     */
    @NotBlank(message = "二手车市场开户银行及账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String lemonMarketBankAndCcount;

    /**
     * 二手车市场电话
     */
    @NotBlank(message = "二手车市场电话不能为空", groups = { AddGroup.class, EditGroup.class })
    private String lemonMarketPhone;

    /**
     * 二手车市场地址
     */
    @NotBlank(message = "二手车市场地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String lemonMarketAddress;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String licensePlate;

    /**
     * 车辆类型
     */
    @NotBlank(message = "车辆类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carType;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 省
     */
    @NotBlank(message = "省不能为空", groups = { AddGroup.class, EditGroup.class })
    private String province;

    /**
     * 登记证号
     */
    @NotBlank(message = "登记证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String regisTrationNumber;

    /**
     * 销售方名称
     */
    @NotBlank(message = "销售方名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerName;

    /**
     * 销售方单位代码/个人身份证号
     */
    @NotBlank(message = "销售方单位代码/个人身份证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerId;

    /**
     * 销售方手机
     */
    @NotBlank(message = "销售方手机不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerPhone;

    /**
     * 销售方地址
     */
    @NotBlank(message = "销售方地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerAddress;

    /**
     * 价税合计
     */
    @NotBlank(message = "价税合计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

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
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

    /**
     * 盖章存在性判断
     */
    @NotBlank(message = "盖章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companySeal;

    /**
     * 大写金额，（加税合计）
     */
    @NotBlank(message = "大写金额，（加税合计）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalUppercase;

    /**
     * 机器码
     */
    @NotBlank(message = "机器码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String machineCode;

    /**
     * 税控码
     */
    @NotBlank(message = "税控码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxCode;

    /**
     * 转入地车辆管理所名称
     */
    @NotBlank(message = "转入地车辆管理所名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String vehicleManageName;

    /**
     * 车辆类型
     */
    @NotBlank(message = "车辆类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String vehicleType;

    /**
     * 机器编号
     */
    @NotBlank(message = "机器编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String machineId;

    /**
     * 机打号码
     */
    @NotBlank(message = "机打号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String machineNumber;

    /**
     * 数电票号码
     */
    @NotBlank(message = "数电票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicNumber;

    /**
     * 1 电子票标记（仅在电子票时返回）
     */
    @NotBlank(message = "1 电子票标记（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 开票人
     */
    @NotBlank(message = "开票人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String issuer;

    /**
     * 转入地车辆管理所名称
     */
    @NotBlank(message = "转入地车辆管理所名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String destinationDepartmentOfMotorVehicles;

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
