package org.smartlink.common.entity.domain.business.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.tenant.core.TenantEntity;

import java.io.Serial;

/**
 * 火车票对象 data_railway_ticket
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_railway_ticket")
public class DataRailwayTicket extends TenantEntity {

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
     * 日期
     */
    private String invoiceDate;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 座位等级
     */
    private String seat;

    /**
     * 座位号
     */
    private String seatNum;

    /**
     * 检票口
     */
    private String wicket;

    /**
     * 取票地址
     */
    private String ticketAddress;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 到达车站
     */
    private String stationGetOff;

    /**
     * 启始车站
     */
    private String stationGetOn;

    /**
     * 时间
     */
    private String invoiceTime;

    /**
     * 时间
     */
    private String kind;

    /**
     * 开票时间（电子票）
     */
    private String dateOfIssue;

    /**
     * -起始站拼音
     */
    private String phonicsOfDepartureStation;

    /**
     * 终点站拼音
     */
    private String phonicsOfDestinationStation;

    /**
     * 旋转角度
     */
    private String orientation;

    /**
     * 合计
     */
    private String invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    private String invoiceStamp;

    /**
     * 车次号
     */
    private String trainNumber;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String region;

    /**
     * 商务类型  退 售 改签 退差
     */
    private String typeOfBusiness;

    /**
     * 退差内容
     */
    private String refundContent;

    /**
     * 售票内容
     */
    private String ticketContent;

    /**
     * 购买方名称 （仅在电子凭证时返回）
     */
    private String buyer;

    /**
     * 社会统一信用代码 （仅在电子凭证时返回）
     */
    private String buyerTaxId;

    /**
     * 原发票号码（仅在电子凭证时返回）
     */
    private String numberOfOriginalInvoice;

    /**
     * 空调类型（仅在电子凭证时返回）
     */
    private String airConditioning;

    /**
     * 凭证种类（仅在电子凭证时返回）
     */
    private String typeOfVoucher;

    /**
     * 火车票类型（仅在电子凭证时返回）
     */
    private String typeOfRailwayTicket;

    /**
     * 折扣标志（仅在电子凭证时返回）
     */
    private String discountMark;

    /**
     * 退票金额（仅在电子凭证时返回）
     */
    private String amountRefunded;

    /**
     * 原火车票价款（仅在电子凭证时返回）
     */
    private String fareOfOriginalRailwayTicket;

    /**
     * 原火车票起点站（仅在电子凭证时返回）
     */
    private String departureStationOfOriginalRailwayTicket;

    /**
     * 原火车票终点站（仅在电子凭证时返回）
     */
    private String destinationStationOfOriginalRailwayTicket;

    /**
     * 购买方地址电话 （仅在电子凭证时返回）
     */
    private String buyerAddrTel;

    /**
     * 购买方银行及账号 （仅在电子凭证时返回）
     */
    private String buyerBankAccount;

    /**
     * 电子票标记
     */
    private String electronicMark;

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
