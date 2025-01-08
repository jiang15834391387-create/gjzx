package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataElectronicTransportationGoods;
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
    @NotNull(message = "申请日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date date;

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
    @NotNull(message = "费用合计小写不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long totalPrice;

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
     * 坐标
     */
    @NotBlank(message = "坐标不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * token
     */
    @NotBlank(message = "token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String saveToken;

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
