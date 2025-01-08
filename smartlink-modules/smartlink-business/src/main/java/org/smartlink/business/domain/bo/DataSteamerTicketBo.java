package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataSteamerTicket;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 船票业务对象 data_steamer_ticket
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataSteamerTicket.class, reverseConvertGenerate = false)
public class DataSteamerTicketBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

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
     * 币种
     */
    @NotBlank(message = "币种不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currencyCode;

    /**
     * 日期
     */
    @NotBlank(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

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
     * 省
     */
    @NotBlank(message = "省不能为空", groups = { AddGroup.class, EditGroup.class })
    private String province;

    /**
     * 到达站
     */
    @NotBlank(message = "到达站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOff;

    /**
     * 出发站
     */
    @NotBlank(message = "出发站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOn;

    /**
     * 时间
     */
    @NotBlank(message = "时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTime;

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
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

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
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
