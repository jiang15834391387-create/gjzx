package org.smartlink.business.domain.bo;

import org.smartlink.business.domain.DataAirlineRulesInfo;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;

/**
 * 飞机票仓位信息业务对象 data_airline_rules_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = DataAirlineRulesInfo.class, reverseConvertGenerate = false)
public class DataAirlineRulesInfoBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long airlineId;

    /**
     * 公司名称
     */
    @NotBlank(message = "公司名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String airlineName;

    /**
     * 公司代码
     */
    @NotBlank(message = "公司代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String airlineCode;

    /**
     * 舱位等级
     */
    @NotBlank(message = "舱位等级不能为空", groups = { AddGroup.class, EditGroup.class })
    private String airlineInfoGrade;

    /**
     * 舱位代码
     */
    @NotBlank(message = "舱位代码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String airlineInfoCode;

    /**
     * 备注
     */
    @NotBlank(message = "备注不能为空", groups = { AddGroup.class, EditGroup.class })
    private String airlineInfoRemarks;

    /**
     * 版本号
     */
    @NotNull(message = "版本号不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long versionCode;

    /**
     * 版本名称
     */
    @NotBlank(message = "版本名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String versionName;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @NotBlank(message = "是否删除标识 0-不删除  1-删除不能为空", groups = { AddGroup.class, EditGroup.class })
    private String deleteFlag;


}
