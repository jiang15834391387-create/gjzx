package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataReceipt;
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
 * 小票业务对象 data_receipt
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataReceipt.class, reverseConvertGenerate = false)
public class DataReceiptBo extends BaseEntity {

    /**
     * 主键
     */
    @NotBlank(message = "主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private String id;

    /**
     * 币种
     */
    @NotBlank(message = "币种不能为空", groups = { AddGroup.class, EditGroup.class })
    private String currencyCode;

    /**
     * 日期
     */
    @NotNull(message = "日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date invoiceDate;

    /**
     * 时间
     */
    @NotBlank(message = "时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private String time;

    /**
     * 折扣
     */
    @NotBlank(message = "折扣不能为空", groups = { AddGroup.class, EditGroup.class })
    private String discount;

    /**
     * 图片表id
     */
    @NotBlank(message = "图片表id不能为空", groups = { AddGroup.class, EditGroup.class })
    private String fileId;

    /**
     * 店名
     */
    @NotBlank(message = "店名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String storeName;

    /**
     * 小计
     */
    @NotNull(message = "小计不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long subTotal;

    /**
     * 税费
     */
    @NotNull(message = "税费不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long tax;

    /**
     * 发票号码
     */
    @NotBlank(message = "发票号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceNumber;

    /**
     * 小费
     */
    @NotBlank(message = "小费不能为空", groups = { AddGroup.class, EditGroup.class })
    private String tips;

    /**
     * 总计
     */
    @NotNull(message = "总计不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long invoiceTotal;

    /**
     * 消费类型
     */
    @NotBlank(message = "消费类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String type;

    /**
     * 1:国际票 0:国内票
     */
    @NotBlank(message = "1:国际票 0:国内票不能为空", groups = { AddGroup.class, EditGroup.class })
    private String internationalMark;

    /**
     * 发票专用章存在性判断
     */
    @NotBlank(message = "发票专用章存在性判断不能为空", groups = { AddGroup.class, EditGroup.class })
    private String invoiceStamp;

    /**
     * token
     */
    @NotBlank(message = "token不能为空", groups = { AddGroup.class, EditGroup.class })
    private String saveToken;

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
