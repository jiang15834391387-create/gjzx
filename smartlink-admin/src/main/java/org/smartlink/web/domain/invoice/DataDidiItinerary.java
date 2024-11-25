package org.smartlink.web.domain.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.smartlink.web.annotation.FieldName;
import org.smartlink.web.domain.modle.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 滴滴行程单对象 data_didi_itinerary
 *
 * @author L
 * @date
 */
@Data
@TableName("data_didi_itinerary")
public class DataDidiItinerary extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 查验结果
     */
    @FieldName(value = "查验结果")
    private String checkResult;

    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    @FieldName(value="主键")
    private String id;
    /**
     * 日期
     */
    @FieldName(value="日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;
    /**
     * 行程结束时间
     */
    @FieldName(value="行程结束时间")
    private String timeGetOff;
    /**
     * 行程开始时间
     */
    @FieldName(value="行程开始时间")
    private String timeGetOn;
    /**
     * 图片表主键
     */
    @FieldName(value="图片表主键")
    private String fileId;
    /**
     * 行程人手机号
     */
    @FieldName(value="行程人手机号")
    private String phone;
    /**
     * 总计
     */
    @FieldName(value="总计")
    private BigDecimal invoiceTotal;
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


    @TableField(exist = false)
    private List<DataDidiItineraryDetails> details;
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
