package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ocr明细对象 data_ocr_details
 *
 * @author L
 * @date
 */
@Data
@TableName("data_ocr_details")
public class DataOcrDetails extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    @FieldName(value="主键")
    private String id;
    /**
     * 金额
     */
    @FieldName(value="金额")
    private BigDecimal detailAmount;
    /**
     * 数量
     */
    @FieldName(value="计数")
    private BigDecimal detailsCount;
    /**
     * 明细编号
     */
    @FieldName(value="明细编号")
    private String detailNo;
    /**
     * 图片表id
     */
    @FieldName(value="图片表id")
    private String fileId;
    /**
     * 明细名称
     */
    @FieldName(value="明细名称")
    private String name;
    /**
     * 商品编码
     */
    @FieldName(value="商品编码")
    private String commodityCode;
    /**
     * 货物或应税劳务名称
     */
    @FieldName(value="货物或应税劳务名称")
    private String commodityName;
    /**
     * 单价
     */
    @FieldName(value="单价")
    private BigDecimal price;
    /**
     * 税率
     */
    @FieldName(value="税率")
    private String taxRate;
    /**
     * 规格型号
     */
    @FieldName(value="规格型号")
    private String standard;
    /**
     * 税额
     */
    @FieldName(value="税额")
    private BigDecimal tax;
    /**
     * 单位
     */
    @FieldName(value="单位")
    private String unit;
    /**
     * 通行日起止
     */
    @FieldName(value="通行日起止")
    private Date currentDateEnd;
    /**
     * 通行日起
     */
    @FieldName(value="通行日起")
    private Date currentDateStart;
    /**
     * 车牌号
     */
    @FieldName(value="车牌号")
    private String licensePlateNum;
    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    @FieldName(value="特殊政策标识")
    private String specialMark;
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
     *  ocr表id
     */
    @FieldName(value = "ocr表id")
    private String ocrId;

}
