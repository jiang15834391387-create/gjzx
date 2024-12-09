package org.smartlink.server.nc.domain.invoice.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ocr明细业务对象 data_ocr_details
 *
 * @author L
 * @date
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("ocr明细业务对象")
public class DataOcrDetailsBo extends BaseEntity {

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", required = true)
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额", required = true)
    @NotNull(message = "金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal detailAmount;

    /**
     *
     */
    @ApiModelProperty(value = "", required = true)
    @NotNull(message = "不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal detailsCount;

    /**
     * 明细编号
     */
    @ApiModelProperty(value = "明细编号", required = true)
    @NotBlank(message = "明细编号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String detailNo;

    /**
     * 图片表id
     */
    @ApiModelProperty(value = "图片表id", required = true)
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 明细名称
     */
    @ApiModelProperty(value = "明细名称", required = true)
    @NotBlank(message = "明细名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 商品编码
     */
    @ApiModelProperty(value = "商品编码", required = true)
    @NotBlank(message = "商品编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String commodityCode;

    /**
     * 货物或应税劳务名称
     */
    @ApiModelProperty(value = "货物或应税劳务名称", required = true)
    @NotBlank(message = "货物或应税劳务名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String commodityName;

    /**
     * 单价
     */
    @ApiModelProperty(value = "单价", required = true)
    @NotNull(message = "单价不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal price;

    /**
     * 税率
     */
    @ApiModelProperty(value = "税率", required = true)
    @NotBlank(message = "税率不能为空", groups = { AddGroup.class, EditGroup.class })
    private String taxRate;

    /**
     * 规格型号
     */
    @ApiModelProperty(value = "规格型号", required = true)
    @NotBlank(message = "规格型号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String standard;

    /**
     * 税额
     */
    @ApiModelProperty(value = "税额", required = true)
    @NotNull(message = "税额不能为空", groups = { AddGroup.class, EditGroup.class })
    private BigDecimal tax;

    /**
     * 单位
     */
    @ApiModelProperty(value = "单位", required = true)
    @NotBlank(message = "单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String unit;

    /**
     * 通行日起止
     */
    @ApiModelProperty(value = "通行日起止", required = true)
    @NotNull(message = "通行日起止不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date currentDateEnd;

    /**
     * 通行日起
     */
    @ApiModelProperty(value = "通行日起", required = true)
    @NotNull(message = "通行日起不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date currentDateStart;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号", required = true)
    @NotBlank(message = "车牌号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String licensePlateNum;

    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    @ApiModelProperty(value = "特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）", required = true)
    @NotBlank(message = "特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）不能为空", groups = { AddGroup.class, EditGroup.class })
    private String specialMark;

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
