package org.smartlink.web.domain.invoice.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.web.domain.modle.BaseEntity;

import java.util.Date;

/**
 * 航空电子行程单明细业务对象 data_flights
 *
 * @author L
 * @date
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("航空电子行程单明细业务对象")
public class DataFlightsBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 承运人
     */
    @ApiModelProperty(value = "承运人", required = true)
    @NotBlank(message = "承运人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carrier;

    /**
     * 乘机日期
     */
    @ApiModelProperty(value = "乘机日期", required = true)
    @NotNull(message = "乘机日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date invoiceDate;

    /**
     * 图片表主键
     */
    @ApiModelProperty(value = "图片表主键", required = true)
    @NotBlank(message = "图片表主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 航班号
     */
    @ApiModelProperty(value = "航班号", required = true)
    @NotBlank(message = "航班号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String flightNumber;

    /**
     * 出发站
     */
    @ApiModelProperty(value = "出发站", required = true)
    @NotBlank(message = "出发站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOn;

    /**
     * 座位等级
     */
    @ApiModelProperty(value = "座位等级", required = true)
    @NotBlank(message = "座位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seat;

    /**
     * 客票生效日期
     */
    @ApiModelProperty(value = "客票生效日期", required = true)
    @NotBlank(message = "客票生效日期", groups = { AddGroup.class, EditGroup.class })
    private Date effectiveDate;

    /**
     * 有效截至日期
     */
    @ApiModelProperty(value = "有效截至日期", required = true)
    @NotBlank(message = "有效截至日期", groups = { AddGroup.class, EditGroup.class })
    private Date expiryDate;

    /**
     * 舱位等级
     */
    @ApiModelProperty(value = "舱位等级", required = true)
    @NotBlank(message = "舱位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String spaceLevel;

    /**
     * 免费行李
     */
    @ApiModelProperty(value = "免费行李", required = true)
    @NotBlank(message = "免费行李不能为空", groups = { AddGroup.class, EditGroup.class })
    private String allow;

    /**
     * 舱位等级
     */
    @ApiModelProperty(value = "舱位等级", required = true)
    @NotBlank(message = "舱位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fareBasis;

    /**
     * 身份证号
     */
    @ApiModelProperty(value = "身份证号", required = true)
    @NotBlank(message = "身份证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userId;

    /**
     * 乘机人姓名
     */
    @ApiModelProperty(value = "乘机人姓名", required = true)
    @NotBlank(message = "乘机人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String userName;

    /**
     * 乘机时间
     */
    @ApiModelProperty(value = "乘机时间", required = true)
    @NotBlank(message = "乘机时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTime;

    /**
     * 到达站
     */
    @ApiModelProperty(value = "到达站", required = true)
    @NotBlank(message = "到达站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOff;

    /**
     * 税务云token
     */
    @ApiModelProperty(value = "税务云token", required = true)
    @NotBlank(message = "税务云token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String saveToken;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ApiModelProperty(value = "是否删除标识 0-不删除  1-删除", required = true)
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;


    /**
     * 置信度
     */
    @ApiModelProperty(value = "置信度", required = true)
    @NotBlank(message = "置信度不能为空", groups = { AddGroup.class, EditGroup.class })
    private String confidence;


}
