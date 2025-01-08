package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataFlightsItineraryDetail;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 航空电子行程单明细业务对象 data_flights_itinerary_detail
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataFlightsItineraryDetail.class, reverseConvertGenerate = false)
public class DataFlightsItineraryDetailBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 承运人
     */
    @NotBlank(message = "承运人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carrier;

    /**
     * 乘机日期
     */
    @NotBlank(message = "乘机日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 图片表主键
     */
    @NotBlank(message = "图片表主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 航班号
     */
    @NotBlank(message = "航班号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String flightNumber;

    /**
     * 出发站
     */
    @NotBlank(message = "出发站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOn;

    /**
     * 座位等级
     */
    @NotBlank(message = "座位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seat;

    /**
     * 客票生效日期
     */
    @NotBlank(message = "客票生效日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String effectiveDate;

    /**
     * 有效截至日期
     */
    @NotBlank(message = "有效截至日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String expiryDate;

    /**
     * 主表id
     */
    @NotBlank(message = "主表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ocrId;

    /**
     * 舱位等级
     */
    @NotBlank(message = "舱位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String spaceLevel;

    /**
     * 免费行李
     */
    @NotBlank(message = "免费行李不能为空", groups = { AddGroup.class, EditGroup.class })
    private String allow;

    /**
     * 客票级别
     */
    @NotBlank(message = "客票级别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fareBasis;

    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

    /**
     * 乘机人姓名
     */
    @NotBlank(message = "乘机人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 乘机时间
     */
    @NotBlank(message = "乘机时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTime;

    /**
     * 到达站
     */
    @NotBlank(message = "到达站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOff;

    /**
     * flight_segment航段序号
     */
    @NotBlank(message = "flight_segment航段序号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String flightSegment;

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
