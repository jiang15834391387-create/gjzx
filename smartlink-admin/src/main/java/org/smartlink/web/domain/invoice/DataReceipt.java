package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 小票对象 data_receipt
 *
 * @author L
 * @date
 */
@Data
@TableName("data_receipt")
public class DataReceipt extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 币种
     */
    @FieldName(value="币种")
    private String currencyCode;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 日期
     */
    @FieldName(value="日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 时间
     */
    @FieldName(value="时间")
    private String time;
    /**
     * 折扣
     */
    @FieldName(value="折扣")
    private String discount;
    /**
     * 图片表id
     */
    @FieldName(value="图片表id")
    private String fileId;
    /**
     * 店名
     */
    @FieldName(value="店名")
    private String storeName;
    /**
     * 小计
     */
    @FieldName(value="小计")
    private BigDecimal subTotal;
    /**
     * 税费
     */
    @FieldName(value="税费")
    private BigDecimal tax;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 发票代码
     */
    @FieldName(value="发票代码")
    private String invoiceCode;
    /**
     * 小费
     */
    @FieldName(value="小费")
    private String tips;
    /**
     * 总计
     */
    @FieldName(value="总计")
    private BigDecimal invoiceTotal;
    /**
     * 发票专用章存在性判断
     */
    @FieldName(value="发票专用章存在性判断")
    private String invoiceStamp;
    /**
     * 税务云token
     */
    @FieldName(value="税务云token")
    private String saveToken;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @FieldName(value="是否删除标识")
    private String deleteFlag;
    /**
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;
    /**
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;
    /**
     * 坐标
     */
    @FieldName(value = "坐标")
    private String coordinateStr;
    /**
     * 暂存状态
     */
    @FieldName(value = "暂存状态")
    private String isStaging;

}
