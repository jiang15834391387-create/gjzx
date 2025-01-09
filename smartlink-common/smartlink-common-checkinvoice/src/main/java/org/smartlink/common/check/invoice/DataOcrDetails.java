package org.smartlink.common.check.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.smartlink.common.check.doman.InvoiceBaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ocr明细对象 data_ocr_details
 *
 * @author ruoyi
 * @date 2022-04-01
 */
@Data
@TableName("data_ocr_details")
public class DataOcrDetails extends InvoiceBaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 金额
     */
    private BigDecimal detailAmount;
    /**
     * 数量
     */
    private BigDecimal detailsCount;
    /**
     * 明细编号
     */
    private String detailNo;
    /**
     * 图片表id
     */
    private String fileId;
    /**
     * 明细名称
     */
    private String name;
    /**
     * 商品编码
     */
    private String commodityCode;
    /**
     * 货物或应税劳务名称
     */
    private String commodityName;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private String taxRate;
    /**
     * 规格型号
     */
    private String standard;
    /**
     * 税额
     */
    private BigDecimal tax;
    /**
     * 单位
     */
    private String unit;
    /**
     * 通行日起止
     */
    private Date currentDateEnd;
    /**
     * 通行日起
     */
    private Date currentDateStart;
    /**
     * 车牌号
     */
    private String licensePlateNum;
    /**
     * 特殊政策标识（0-正常票 1-免税 2-不征税  3-零税率）
     */
    private String specialMark;
    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 置信度
     */
    private String confidence;

    /**
     *  ocr表id
     */
    private String ocrId;

}
