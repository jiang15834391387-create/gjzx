package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataAircraftInvoice;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 机打发票业务对象 data_aircraft_invoice
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataAircraftInvoice.class, reverseConvertGenerate = false)
public class DataAircraftInvoiceBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { EditGroup.class })
    private String id;

    /**
     * 关联图片表id
     */
    @NotBlank(message = "关联图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 购方单位
     */
    @NotBlank(message = "购方单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerName;

    /**
     * 纳税人识别号
     */
    @NotBlank(message = "纳税人识别号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String buyerTaxid;

    /**
     * 种类
     */
    @NotBlank(message = "种类不能为空", groups = { AddGroup.class, EditGroup.class })
    private String category;

    /**
     * 校验码
     */
    @NotBlank(message = "校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkCode;

    /**
     * 所属城市
     */
    @NotBlank(message = "所属城市不能为空", groups = { AddGroup.class, EditGroup.class })
    private String city;

    /**
     * 发票代码
     */
    @NotBlank(message = "发票代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date invoiceDate;

    /**
     * 地区(省)
     */
    @NotBlank(message = "地区(省)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String province;

    /**
     * 销方单位名称
     */
    @NotBlank(message = "销方单位名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerName;

    /**
     * 销方税号
     */
    @NotBlank(message = "销方税号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String sellerTaxid;

    /**
     * 总价
     */
    @NotNull(message = "总价不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long invoiceTotal;

    /**
     * 是否为浙江/广东通用机打电子发票
     */
    @NotBlank(message = "是否为浙江/广东通用机打电子发票不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 是否有公司印章(0: 没有; 1: 有)
     */
    @NotBlank(message = "是否有公司印章(0: 没有; 1: 有)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String companySeal;

    /**
     * 大写合计金额
     */
    @NotBlank(message = "大写合计金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private String moneyUppercase;

    /**
     * 税前金额
     */
    @NotNull(message = "税前金额不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long pretaxAmount;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 税务云token
     */
    @NotBlank(message = "税务云token不能为空", groups = { AddGroup.class, EditGroup.class })
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
