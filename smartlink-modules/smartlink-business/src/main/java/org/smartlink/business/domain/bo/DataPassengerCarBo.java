package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataPassengerCar;
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
 * 客运汽车票业务对象 data_passenger_car
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataPassengerCar.class, reverseConvertGenerate = false)
public class DataPassengerCarBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { AddGroup.class, EditGroup.class })
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
     * 发票代码
     */
    @NotBlank(message = "发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date invoiceDate;

    /**
     * 出发车站
     */
    @NotBlank(message = "出发车站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGeton;

    /**
     * 达到车站
     */
    @NotBlank(message = "达到车站不能为空", groups = { AddGroup.class, EditGroup.class })
    private String stationGetoff;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 时间
     */
    @NotBlank(message = "时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTime;

    /**
     * 总计
     */
    @NotNull(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long invoiceTotal;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

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
     * 是否盖章（0: 没有; 1: 有）
     */
    @NotBlank(message = "是否盖章（0: 没有; 1: 有）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companySeal;

    /**
     * 车次
     */
    @NotBlank(message = "车次不能为空", groups = { AddGroup.class, EditGroup.class })
    private String busNumber;

    /**
     * 睿真token
     */
    @NotBlank(message = "睿真token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String saveToken;

    /**
     * 是否查验标识，（0查验失败，1查验成功）
     */
    @NotBlank(message = "是否查验标识，（0查验失败，1查验成功）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkInvoice;

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
