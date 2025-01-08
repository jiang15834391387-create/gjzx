package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataRailwayTicket;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 火车票业务对象 data_railway_ticket
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataRailwayTicket.class, reverseConvertGenerate = false)
public class DataRailwayTicketBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date invoiceDate;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 座位等级
     */
    @NotBlank(message = "座位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seat;

    /**
     * 座位号
     */
    @NotBlank(message = "座位号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seatNum;

    /**
     * 检票口
     */
    @NotBlank(message = "检票口不能为空", groups = { AddGroup.class, EditGroup.class })
    private String wicket;

    /**
     * 取票地址
     */
    @NotBlank(message = "取票地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ticketAddress;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String idNumber;

    /**
     * 序列号
     */
    @NotBlank(message = "序列号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String serialNumber;

    /**
     * 到达车站
     */
    @NotBlank(message = "到达车站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOff;

    /**
     * 启始车站
     */
    @NotBlank(message = "启始车站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOn;

    /**
     * 时间
     */
    @NotBlank(message = "时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTime;

    /**
     * 合计
     */
    @NotNull(message = "合计不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long invoiceTotal;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

    /**
     * 车次号
     */
    @NotBlank(message = "车次号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String trainNumber;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 睿真token
     */
    @NotBlank(message = "睿真token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String saveToken;

    /**
     * 商务类型  退 售 改签 退差
     */
    @NotBlank(message = "商务类型  退 售 改签 退差不能为空", groups = { AddGroup.class, EditGroup.class })
    private String typeOfBusiness;

    /**
     * 退差内容
     */
    @NotBlank(message = "退差内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String refundContent;

    /**
     * 售票内容
     */
    @NotBlank(message = "售票内容不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ticketContent;

    /**
     * 购买方名称 （仅在电子凭证时返回）
     */
    @NotBlank(message = "购买方名称 （仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyer;

    /**
     * 社会统一信用代码 （仅在电子凭证时返回）
     */
    @NotBlank(message = "社会统一信用代码 （仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerTaxId;

    /**
     * 原发票号码（仅在电子凭证时返回）
     */
    @NotBlank(message = "原发票号码（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String numberOfOriginalInvoice;

    /**
     * 空调类型（仅在电子凭证时返回）
     */
    @NotBlank(message = "空调类型（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String airConditioning;

    /**
     * 凭证种类（仅在电子凭证时返回）
     */
    @NotBlank(message = "凭证种类（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String typeOfVoucher;

    /**
     * 火车票类型（仅在电子凭证时返回）
     */
    @NotBlank(message = "火车票类型（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String typeOfRailwayTicket;

    /**
     * 折扣标志（仅在电子凭证时返回）
     */
    @NotBlank(message = "折扣标志（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String discountMark;

    /**
     * 退票金额（仅在电子凭证时返回）
     */
    @NotNull(message = "退票金额（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long amountRefunded;

    /**
     * 原火车票价款（仅在电子凭证时返回）
     */
    @NotNull(message = "原火车票价款（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long fareOfOriginalRailwayTicket;

    /**
     * 原火车票起点站（仅在电子凭证时返回）
     */
    @NotBlank(message = "原火车票起点站（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String departureStationOfOriginalRailwayTicket;

    /**
     * 原火车票终点站（仅在电子凭证时返回）
     */
    @NotBlank(message = "原火车票终点站（仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String destinationStationOfOriginalRailwayTicket;

    /**
     * 购买方地址电话 （仅在电子凭证时返回）
     */
    @NotBlank(message = "购买方地址电话 （仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerAddrTel;

    /**
     * 购买方银行及账号 （仅在电子凭证时返回）
     */
    @NotBlank(message = "购买方银行及账号 （仅在电子凭证时返回）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerBankAccount;

    /**
     * 电子票标记
     */
    @NotBlank(message = "电子票标记不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

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

    /**
     * 置信度
     */
    @NotBlank(message = "置信度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String confidence;

    /**
     * 查验结果
     */
    @NotBlank(message = "查验结果不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkResult;

    /**
     * 入台账标识
     */
    @NotBlank(message = "入台账标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String pushBusinessInfoFlag;


}
