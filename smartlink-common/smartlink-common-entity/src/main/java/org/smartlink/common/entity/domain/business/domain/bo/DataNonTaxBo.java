package org.smartlink.common.entity.domain.business.domain.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.entity.domain.business.domain.DataNonTax;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 非税收入类票据业务对象 data_non_tax
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataNonTax.class, reverseConvertGenerate = false)
public class DataNonTaxBo extends BaseEntity {

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
     * 旋转角度
     */
    @NotBlank(message = "旋转角度", groups = { AddGroup.class, EditGroup.class })
    private String orientation;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = { AddGroup.class, EditGroup.class })
    private String title;

    /**
     * 票据号码
     */
    @NotBlank(message = "票据号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 票据代码
     */
    @NotBlank(message = "票据代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceCode;

    /**
     * 校验码
     */
    @NotBlank(message = "校验码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checkCode;

    /**
     * 复核人
     */
    @NotBlank(message = "复核人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String checker;

    /**
     * 收款人
     */
    @NotBlank(message = "收款人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiver;

    /**
     * 是否为电子非税收入票据(1：是)
     */
    @NotBlank(message = "是否为电子非税收入票据(1：是)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String electronicMark;

    /**
     * 发票消费类型
     */
    @NotBlank(message = "发票消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String kind;

    /**
     * 其他信息
     */
    @NotBlank(message = "其他信息不能为空", groups = { AddGroup.class, EditGroup.class })
    private String otherInfo;

    /**
     * 收款单位
     */
    @NotBlank(message = "收款单位不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payee;

    /**
     * 交款人
     */
    @NotBlank(message = "交款人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payer;

    /**
     * 交款人统一社会信用代码
     */
    @NotBlank(message = "交款人统一社会信用代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String socialCreditCode;

    /**
     * 区块标识
     */
    @NotBlank(message = "区块标识不能为空", groups = { AddGroup.class, EditGroup.class })
    private String blockChain;

    /**
     * 缴款码
     */
    @NotBlank(message = "缴款码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String paymentCode;

    /**
     * 执收单位编码
     */
    @NotBlank(message = "执收单位编码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payeeCode;

    /**
     * 交款人账号
     */
    @NotBlank(message = "交款人账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payerAccountNumber;

    /**
     * 交款人开户银行
     */
    @NotBlank(message = "交款人开户银行不能为空", groups = { AddGroup.class, EditGroup.class })
    private String payerAccountOpeningBank;

    /**
     * 收款人账号
     */
    @NotBlank(message = "收款人账号不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiverAccountNumber;

    /**
     * 收款人开户银行
     */
    @NotBlank(message = "收款人开户银行不能为空", groups = { AddGroup.class, EditGroup.class })
    private String receiverAccountOpeningBank;

    /**
     * 经办人
     */
    @NotBlank(message = "经办人不能为空", groups = { AddGroup.class, EditGroup.class })
    private String handler;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    @NotBlank(message = "单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]不能为空", groups = { AddGroup.class, EditGroup.class })
    private String region;

    /**
     * 合计金额(小写)
     */
    @NotBlank(message = "合计金额(小写)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceTotal;

    /**
     * 合计金额(大写)
     */
    @NotBlank(message = "合计金额(大写)不能为空", groups = { AddGroup.class, EditGroup.class })
    private String totalWords;

    /**
     * 日期
     */
    @NotBlank(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceDate;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

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
