package org.smartlink.common.entity.domain.business.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataElectronicTransportationGoods;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 货物运输电子收款凭证业务对象 data_electronic_transportation_goods
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataElectronicTransportationGoods.class, reverseConvertGenerate = false)
public class DataElectronicTransportationGoodsBo extends BaseEntity {

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
     * 申请日期
     */
    @NotBlank(message = "申请日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String date;

    /**
     * 托运人证照号码
     */
    @NotBlank(message = "托运人证照号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessLicenseNumber;

    /**
     * 电子收款凭证号
     */
    @NotBlank(message = "电子收款凭证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicReceiptNumber;

    /**
     * 服务商
     */
    @NotBlank(message = "服务商不能为空", groups = { AddGroup.class, EditGroup.class })
    private String producer;

    /**
     * 托运人名称
     */
    @NotBlank(message = "托运人名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String shipper;

    /**
     * 费用合计小写
     */
    @NotBlank(message = "费用合计小写不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalPrice;

    /**
     * 费用合计大写
     */
    @NotBlank(message = "费用合计大写不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalCn;

    /**
     * 承运人姓名
     */
    @NotBlank(message = "承运人姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transporter;

    /**
     * 承运人身份证号
     */
    @NotBlank(message = "承运人身份证号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String transporterIdNumber;

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
