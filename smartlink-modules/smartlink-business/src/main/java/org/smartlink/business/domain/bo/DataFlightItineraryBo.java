package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataFlightItinerary;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 航空电子行程单业务对象 data_flight_itinerary
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataFlightItinerary.class, reverseConvertGenerate = false)
public class DataFlightItineraryBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 图片表主键
     */
    @NotBlank(message = "图片表主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 旅客姓名
     */
    @NotBlank(message = "旅客姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 销售单位代号
     */
    @NotBlank(message = "销售单位代号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String agentCode;

    /**
     * 民航发展基金
     */
    @NotBlank(message = "民航发展基金不能为空", groups = { AddGroup.class, EditGroup.class })
    private String caacDevelopmentFund;

    /**
     * 校验码
     */
    @NotBlank(message = "校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkCode;

    /**
     * 填开日期
     */
    @NotBlank(message = "填开日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 票价
     */
    @NotBlank(message = "票价不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fare;

    /**
     * 燃油附加费
     */
    @NotBlank(message = "燃油附加费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fuelSurcharge;

    /**
     * 保险费
     */
    @NotBlank(message = "保险费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String insurance;

    /**
     * 国内国际标签
     */
    @NotBlank(message = "国内国际标签不能为空", groups = { AddGroup.class, EditGroup.class })
    private String internationalFlag;

    /**
     * 填开单位
     */
    @NotBlank(message = "填开单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String issueBy;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 税额
     */
    @NotBlank(message = "税额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tax;

    /**
     * 总计
     */
    @NotBlank(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 印刷序号
     */
    @NotBlank(message = "印刷序号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String printNumber;

    /**
     * 签注
     */
    @NotBlank(message = "签注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String endorsement;

    /**
     * 1 电子票标记（仅在电子票时返回）
     */
    @NotBlank(message = "1 电子票标记（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 出票状态（仅在电子票时返回）
     */
    @NotBlank(message = "出票状态（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String issuingStatus;

    /**
     * 二维码（仅在电子票时返回）
     */
    @NotBlank(message = "二维码（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String qrcode;

    /**
     * 收据号码（仅在电子票时返回）
     */
    @NotBlank(message = "收据号码（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiptNumber;

    /**
     * GP订单号
     */
    @NotBlank(message = "GP订单号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String numberOfGpOrder;

    /**
     * 提示信息（仅在电子票时返回）
     */
    @NotBlank(message = "提示信息（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String promptInformation;

    /**
     * 其他税费（仅在电子票时返回）
     */
    @NotBlank(message = "其他税费（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String otherTaxes;

    /**
     * 购买方名称（仅在电子票时返回）
     */
    @NotBlank(message = "购买方名称（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyer;

    /**
     * 销售方名称（仅在电子票时返回）
     */
    @NotBlank(message = "销售方名称（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seller;

    /**
     * 购买方纳税人识别号（仅在电子票时返回）
     */
    @NotBlank(message = "购买方纳税人识别号（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerTaxId;

    /**
     * 增值税税率（仅在电子票时返回）
     */
    @NotBlank(message = "增值税税率（仅在电子票时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxRate;

    /**
     * 商务类型 售 或 退
     */
    @NotBlank(message = "商务类型 售 或 退不能为空", groups = { AddGroup.class, EditGroup.class })
    private String typeOfBusiness;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

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
