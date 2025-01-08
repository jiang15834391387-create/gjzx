package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serial;

/**
 * 货物运输电子收款凭证对象 data_electronic_transportation_goods
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_electronic_transportation_goods")
public class DataElectronicTransportationGoods extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;

    /**
     * 图片表id
     */
    private String fileId;

    /**
     * 申请日期
     */
    private Date date;

    /**
     * 托运人证照号码
     */
    private String businessLicenseNumber;

    /**
     * 电子收款凭证号
     */
    private String electronicReceiptNumber;

    /**
     * 服务商
     */
    private String producer;

    /**
     * 托运人名称
     */
    private String shipper;

    /**
     * 费用合计小写
     */
    private Long totalPrice;

    /**
     * 费用合计大写
     */
    private String totalCn;

    /**
     * 承运人姓名
     */
    private String transporter;

    /**
     * 承运人身份证号
     */
    private String transporterIdNumber;

    /**
     * 坐标
     */
    private String region;

    /**
     * token
     */
    private String saveToken;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本号
     */
    @Version
    private Long version;

    /**
     * 查验结果
     */
    private String checkResult;

    /**
     * 入台账标识
     */
    private String pushBusinessInfoFlag;


}
