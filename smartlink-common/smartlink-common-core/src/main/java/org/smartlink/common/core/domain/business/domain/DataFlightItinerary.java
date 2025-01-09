package org.smartlink.common.core.domain.business.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 航空电子行程单对象 data_flight_itinerary
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_flight_itinerary")
public class DataFlightItinerary extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表主键
     */
    private String fileId;

    /**
     * 标题
     */
    private String title;

    /**
     * 旅客姓名
     */
    private String userName;

    /**
     * 销售单位代号
     */
    private String agentCode;

    /**
     * 民航发展基金
     */
    private String caacDevelopmentFund;

    /**
     * 校验码
     */
    private String checkCode;

    /**
     * 填开日期
     */
    private String invoiceDate;

    /**
     * 票价
     */
    private String fare;

    /**
     * 燃油附加费
     */
    private String fuelSurcharge;

    /**
     * 保险费
     */
    private String insurance;

    /**
     * 国内国际标签
     */
    private String internationalFlag;

    /**
     * 填开单位
     */
    private String issueBy;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 税额
     */
    private String tax;

    /**
     * 总计
     */
    private String invoiceTotal;

    /**
     * 发票消费类型
     */
    private String kind;

    /**
     * 印刷序号
     */
    private String printNumber;

    /**
     * 签注
     */
    private String endorsement;

    /**
     * 1 电子票标记（仅在电子票时返回）
     */
    private String electronicMark;

    /**
     * 出票状态（仅在电子票时返回）
     */
    private String issuingStatus;

    /**
     * 二维码（仅在电子票时返回）
     */
    private String qrcode;

    /**
     * 收据号码（仅在电子票时返回）
     */
    private String receiptNumber;

    /**
     * GP订单号
     */
    private String numberOfGpOrder;

    /**
     * 提示信息（仅在电子票时返回）
     */
    private String promptInformation;

    /**
     * 其他税费（仅在电子票时返回）
     */
    private String otherTaxes;

    /**
     * 购买方名称（仅在电子票时返回）
     */
    private String buyer;

    /**
     * 销售方名称（仅在电子票时返回）
     */
    private String seller;

    /**
     * 购买方纳税人识别号（仅在电子票时返回）
     */
    private String buyerTaxId;

    /**
     * 增值税税率（仅在电子票时返回）
     */
    private String taxRate;

    /**
     * 商务类型 售 或 退
     */
    private String typeOfBusiness;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

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
