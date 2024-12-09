package org.smartlink.server.nc.domain.invoice.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 任务业务对象 data_current_task
 *
 * @author L
 * @date
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("任务业务对象")
public class DataCurrentTaskBo extends BaseEntity {

    /**
     * 业务流水号
     */
    @ApiModelProperty(value = "业务流水号", required = true)
    @NotBlank(message = "业务流水号不能为空", groups = {EditGroup.class})
    private String businessSerialNo;

    /**
     * 制单日期
     */
    @ApiModelProperty(value = "制单日期", required = true)
    @NotNull(message = "制单日期不能为空", groups = {AddGroup.class, EditGroup.class})
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date billDate;

    /**
     * 单据号
     */
    @ApiModelProperty(value = "单据号", required = true)
    @NotBlank(message = "单据号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String billNum;

    /**
     * 单据类型
     */
    @ApiModelProperty(value = "单据类型", required = true)
    @NotBlank(message = "单据类型不能为空", groups = {AddGroup.class, EditGroup.class})
    private String billType;

    /**
     * 单据总金额
     */
    @ApiModelProperty(value = "单据总金额", required = true)
    @NotBlank(message = "单据总金额不能为空", groups = {AddGroup.class, EditGroup.class})
    private String cash;

    /**
     * 所属组织机构id
     */
    @ApiModelProperty(value = "所属组织机构id", required = true)
    @NotBlank(message = "所属组织机构id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String groupId;

    /**
     * 识别类型 1:数影ocr  2.税务云ocr
     */
    @ApiModelProperty(value = "识别类型 1:数影ocr  2.税务云ocr", required = true)
    @NotBlank(message = "识别类型 1:数影ocr  2.税务云ocr不能为空", groups = {AddGroup.class, EditGroup.class})
    private String ocrType;

    /**
     * 驳回原因
     */
    @ApiModelProperty(value = "驳回原因", required = true)
    @NotBlank(message = "驳回原因不能为空", groups = {AddGroup.class, EditGroup.class})
    private String operateSuggest;

    /**
     * 机构号
     */
    @ApiModelProperty(value = "机构号", required = true)
    @NotBlank(message = "机构号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orgCode;

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称", required = true)
    @NotBlank(message = "机构名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String orgName;

    /**
     * 父单据类型编号
     */
    @ApiModelProperty(value = "父单据类型编号", required = true)
    @NotBlank(message = "父单据类型编号不能为空", groups = {AddGroup.class, EditGroup.class})
    private String pkBillType;

    /**
     * 扫描方式  1：单扫  2：批扫
     */
    @ApiModelProperty(value = "扫描方式  1：单扫  2：批扫", required = true)
    @NotBlank(message = "扫描方式  1：单扫  2：批扫不能为空", groups = {AddGroup.class, EditGroup.class})
    private String scanType;

    /**
     * 扫描方式名称
     */
    @ApiModelProperty(value = "扫描方式名称", required = true)
    @NotBlank(message = "扫描方式名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String scanTypeName;

    /**
     * 渠道系统编码
     */
    @ApiModelProperty(value = "渠道系统编码", required = true)
    @NotBlank(message = "渠道系统编码不能为空", groups = {AddGroup.class, EditGroup.class})
    private String systemCode;

    /**
     * 单据状态0待登记、1待扫描、2扫描完成、3驳回修改、4驳回重扫、5修改完成、6补扫完成、7未装册、8已装册
     */
    @ApiModelProperty(value = "单据状态0待登记、1待扫描、2扫描完成、3驳回修改、4驳回重扫、5修改完成、6补扫完成、7未装册、8已装册", required = true)
    @NotBlank(message = "单据状态0待登记、1待扫描、2扫描完成、3驳回修改、4驳回重扫、5修改完成、6补扫完成、7未装册、8已装册不能为空", groups = {AddGroup.class, EditGroup.class})
    private String taskState;

    /**
     * 制单人id
     */
    @ApiModelProperty(value = "制单人id", required = true)
    @NotBlank(message = "制单人id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String userId;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ApiModelProperty(value = "是否删除标识 0-不删除  1-删除", required = true)
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = {AddGroup.class, EditGroup.class})
    private String deleteFlag;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = true)
    @NotBlank(message = "备注不能为空", groups = {AddGroup.class, EditGroup.class})
    private String remark;

    /**
     * 单据影像文件数量
     */
    @FieldName(value = "单据影像文件数量")
    private String scanfileSum;

    /**
     * 版本号
     */
    @ApiModelProperty(value = "版本号", required = true)
    @NotBlank(message = "版本号", groups = {AddGroup.class, EditGroup.class})
    private Integer version;
    /**
     * 登录来源
     */
    @ApiModelProperty(value = "登录来源", required = true)
    @NotBlank(message = "登录来源", groups = {AddGroup.class, EditGroup.class})
    private String linksSource;

    /**
     * 批扫人名称
     */
    @ApiModelProperty(value = "批扫人名称", required = true)
    @NotBlank(message = "批扫人名称", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 批扫人编号
     */
    @ApiModelProperty(value = "批扫人编号", required = true)
    @NotBlank(message = "批扫人编号", groups = {AddGroup.class, EditGroup.class})
    private String code;

    /**
     * 批扫人编号
     */
    @ApiModelProperty(value = "批扫人编号", required = true)
    @NotBlank(message = "批扫人编号", groups = {AddGroup.class, EditGroup.class})
    private String message;

    /**
     * 暂存状态
     */
    @ApiModelProperty(value = "暂存状态", required = true)
    @NotBlank(message = "暂存状态", groups = {AddGroup.class, EditGroup.class})
    private String billSaved;

}
