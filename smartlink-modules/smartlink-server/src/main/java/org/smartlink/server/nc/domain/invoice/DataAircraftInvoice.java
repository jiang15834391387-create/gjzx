package org.smartlink.server.nc.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 机打发票对象 data_aircraft_invoice
 *
 * @author L
 * @date
 */
@Data
@TableName("data_aircraft_invoice")
public class DataAircraftInvoice extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;
    /**
     * 关联图片表id
     */
    @FieldName(value="关联图片表id")
    private String fileId;
    /**
     * 购方单位
     */
    @FieldName(value="购方单位")
    private String buyerName;
    /**
     * 纳税人识别号
     */
    @FieldName(value="纳税人识别号")
    private String buyerTaxid;
    /**
     * 种类
     */
    @FieldName(value="种类")
    private String category;
    /**
     * 校验码
     */
    @FieldName(value="校验码")
    private String checkCode;
    /**
     * 所属城市
     */
    @FieldName(value="所属城市")
    private String city;
    /**
     * 发票代码
     */
    @FieldName(value="发票代码")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 日期
     */
    @FieldName(value="日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 地区(省)
     */
    @FieldName(value="地区(省)")
    private String province;
    /**
     * 销方单位名称
     */
    @FieldName(value="销方单位名称")
    private String sellerName;
    /**
     * 销方税号
     */
    @FieldName(value="销方税号")
    private String sellerTaxid;
    /**
     * 总价
     */
    @FieldName(value="总价")
    private BigDecimal invoiceTotal;
    /**
     * 税前金额
     */
    @FieldName(value="税前金额")
    private BigDecimal pretaxAmount;
    /**
     * 是否为浙江/广东通用机打电子发票
     */
    @FieldName(value="是否为浙江/广东通用机打电子发票")
    private String electronicMark;
    /**
     * 是否有公司印章（0: 没有; 1: 有）
     */
    @FieldName(value="是否有公司印章")
    private String companySeal;
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
     * 租户id
     */
    @FieldName(value="租户id")
    private String tenantId;

    /**
     * 置信度
     */
    @FieldName(value="置信度")
    private String confidence;
    /**
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;

    /**
     * OCR详情
     */
    @TableField(exist = false)
    private List<DataOcrDetails> details;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 推送ncc台账图片id
     */
    @FieldName(value="推送ncc台账图片id")
    private String ncImageId;
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
