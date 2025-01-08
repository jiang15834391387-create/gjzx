package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataOcrDetails;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;

/**
 * 增值税发票明细业务对象 data_ocr_details
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataOcrDetails.class, reverseConvertGenerate = false)
public class DataOcrDetailsBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 主表id
     */
    @NotBlank(message = "主表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String ocrId;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 金额
     */
    @NotBlank(message = "金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String detailAmount;

    /**
     * 数量
     */
    @NotBlank(message = "数量不能为空", groups = { AddGroup.class, EditGroup.class })
    private String detailsCount;

    /**
     * 明细编号
     */
    @NotBlank(message = "明细编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String detailNo;

    /**
     * 明细名称
     */
    @NotBlank(message = "明细名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 商品编码
     */
    @NotBlank(message = "商品编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String commodityCode;

    /**
     * 货物或应税劳务名称
     */
    @NotBlank(message = "货物或应税劳务名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String commodityName;

    /**
     * 单价
     */
    @NotBlank(message = "单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private String price;

    /**
     * 税率
     */
    @NotBlank(message = "税率不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxRate;

    /**
     * 规格型号
     */
    @NotBlank(message = "规格型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String standard;

    /**
     * 税额
     */
    @NotBlank(message = "税额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tax;

    /**
     * 单位
     */
    @NotBlank(message = "单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unit;

    /**
     * 通行日起止
     */
    @NotBlank(message = "通行日起止不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currentDateEnd;

    /**
     * 通行日起
     */
    @NotBlank(message = "通行日起不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currentDateStart;

    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String licensePlateNum;

    /**
     * 车辆类型
     */
    @NotBlank(message = "车辆类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String vehicleType;

    /**
     * 用车时间 
     */
    @NotBlank(message = "用车时间 不能为空", groups = { AddGroup.class, EditGroup.class })
    private String usageTime;

    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    @NotBlank(message = "特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String specialMark;

    /**
     * 建筑服务发生地（ service_type为建筑服务，返回此字段）
     */
    @NotBlank(message = "建筑服务发生地（ service_type为建筑服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String placeOfBuildingService;

    /**
     * 建筑项目名称（ service_type为建筑服务，返回此字段）
     */
    @NotBlank(message = "建筑项目名称（ service_type为建筑服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buildingName;

    /**
     * 产权证书/不动产权证号（service_type为不动产经营租赁服务，返回字段）
     */
    @NotBlank(message = "产权证书/不动产权证号（service_type为不动产经营租赁服务，返回字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String titleCertificateNumber;

    /**
     * 面积单位（service_type为不动产经营租赁服务，返回字段）
     */
    @NotBlank(message = "面积单位（service_type为不动产经营租赁服务，返回字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String areaUnit;

    /**
     * 运输工具类型（service_type为货物运输服务，返回此字段）
     */
    @NotBlank(message = "运输工具类型（service_type为货物运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transportType;

    /**
     * 运输工具牌号（service_type为货物运输服务，返回此字段）
     */
    @NotBlank(message = "运输工具牌号（service_type为货物运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transportNumber;

    /**
     * 起始地（service_type为货物运输服务，返回此字段）
     */
    @NotBlank(message = "起始地（service_type为货物运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String from;

    /**
     * 到达地（service_type为货物运输服务，返回此字段）
     */
    @NotBlank(message = "到达地（service_type为货物运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String to;

    /**
     * 运输货物名称（service_type为货物运输服务，返回此字段）
     */
    @NotBlank(message = "运输货物名称（service_type为货物运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String goodsName;

    /**
     * 出行人（service_type为旅客运输服务，返回此字段）
     */
    @NotBlank(message = "出行人（service_type为旅客运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String passenger;

    /**
     * 出行日期（service_type为旅客运输服务，返回此字段）
     */
    @NotBlank(message = "出行日期（service_type为旅客运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String travelDate;

    /**
     * 等级（service_type为旅客运输服务，返回此字段）
     */
    @NotBlank(message = "等级（service_type为旅客运输服务，返回此字段）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String seat;

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
