package org.smartlink.server.nc.domain;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.server.nc.domain.modle.BaseEntity;


/**
 * 任务、图片中间关联业务对象 data_cm_info
 *
 * @author ruoyi
 * @date 2022-03-31
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务、图片中间关联业务对象")
public class DataCmInfoBo extends BaseEntity {

    /**
     *
     */
    @ApiModelProperty(value = "", required = true)
    @NotBlank(message = "不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 批次号
     */
    @ApiModelProperty(value = "批次号", required = true)
    @NotBlank(message = "批次号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String batchId;

    /**
     * 业务流水号
     */
    @ApiModelProperty(value = "业务流水号", required = true)
    @NotBlank(message = "业务流水号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String businessSerialNo;

    /**
     * 友报账编号
     */
    @ApiModelProperty(value = "友报账编号", required = true)
    @NotBlank(message = "友报账编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String barCode;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ApiModelProperty(value = "是否删除标识 0-不删除  1-删除", required = true)
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String remark;


}
