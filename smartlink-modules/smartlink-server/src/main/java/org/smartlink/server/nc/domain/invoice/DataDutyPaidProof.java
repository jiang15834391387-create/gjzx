package org.smartlink.server.nc.domain.invoice;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.server.nc.annotation.FieldName;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 完税证明对象 data_duty_paid_proof
 *
 * @author L
 * @date
 */
@Data
@TableName("data_duty_paid_proof")
public class DataDutyPaidProof extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    @FieldName(value="主键")
    private String id;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;
    /**
     * 购买方名称
     */
    @FieldName(value="购买方名称")
    private String buyerName;
    /**
     * 购买方纳税人识别号
     */
    @FieldName(value="购买方纳税人识别号")
    private String buyerTaxId;
    /**
     * 日期
     */
    @FieldName(value="日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 图片表主键
     */
    @FieldName(value="图片表主键")
    private String fileId;
    /**
     * 发票号码
     */
    @FieldName(value="发票号码")
    private String invoiceNumber;
    /**
     * 总计
     */
    @FieldName(value="总计")
    private BigDecimal invoiceTotal;
    /**
     * 大写金额
     */
    @FieldName(value="大写金额")
    private String totalUppercase;
    /**
     * 主管税务机关
     */
    @FieldName(value="主管税务机关")
    private String taxAuthority;
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
     * 台账推送业务系统成功标识
     */
    @FieldName(value="台账推送业务系统成功标识")
    private String pushBusinessInfoFlag;
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
