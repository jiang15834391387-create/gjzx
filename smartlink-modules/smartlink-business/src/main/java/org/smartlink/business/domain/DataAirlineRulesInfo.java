package org.smartlink.business.domain;

import org.smartlink.common.tenant.core.TenantEntity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 飞机票仓位信息对象 data_airline_rules_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("data_airline_rules_info")
public class DataAirlineRulesInfo extends TenantEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "airline_id")
    private Long airlineId;

    /**
     * 公司名称
     */
    private String airlineName;

    /**
     * 公司代码
     */
    private String airlineCode;

    /**
     * 舱位等级
     */
    private String airlineInfoGrade;

    /**
     * 舱位代码
     */
    private String airlineInfoCode;

    /**
     * 备注
     */
    private String airlineInfoRemarks;

    /**
     * 版本号
     */
    private Long versionCode;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    private String deleteFlag;

    /**
     * 版本号
     */
    @Version
    private Long version;


}
