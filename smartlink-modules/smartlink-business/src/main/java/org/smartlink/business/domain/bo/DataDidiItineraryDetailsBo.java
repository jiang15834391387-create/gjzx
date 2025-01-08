package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataDidiItineraryDetails;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 滴滴行程单明细业务对象 data_didi_itinerary_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataDidiItineraryDetails.class, reverseConvertGenerate = false)
public class DataDidiItineraryDetailsBo extends BaseEntity {

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
     * 车型
     */
    @NotBlank(message = "车型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String carType;

    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空", groups = { AddGroup.class, EditGroup.class })
    private String city;

    /**
     * 里程（公里）
     */
    @NotBlank(message = "里程（公里）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String mileage;

    /**
     * 终点
     */
    @NotBlank(message = "终点不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOff;

    /**
     * 起点
     */
    @NotBlank(message = "起点不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetOn;

    /**
     * 下单时间
     */
    @NotBlank(message = "下单时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String timeOrder;

    /**
     * 上车时间
     */
    @NotBlank(message = "上车时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String timeGetOn;

    /**
     * 下车时间
     */
    @NotBlank(message = "下车时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String timeGetOff;

    /**
     * 服务商
     */
    @NotBlank(message = "服务商不能为空", groups = { AddGroup.class, EditGroup.class })
    private String producer;

    /**
     * 总计
     */
    @NotBlank(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

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
