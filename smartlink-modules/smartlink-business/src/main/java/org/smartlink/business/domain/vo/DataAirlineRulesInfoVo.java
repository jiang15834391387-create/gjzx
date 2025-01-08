package org.smartlink.business.domain.vo;

import org.smartlink.business.domain.DataAirlineRulesInfo;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 飞机票仓位信息视图对象 data_airline_rules_info
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataAirlineRulesInfo.class)
public class DataAirlineRulesInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long airlineId;

    /**
     * 公司名称
     */
    @ExcelProperty(value = "公司名称")
    private String airlineName;

    /**
     * 公司代码
     */
    @ExcelProperty(value = "公司代码")
    private String airlineCode;

    /**
     * 舱位等级
     */
    @ExcelProperty(value = "舱位等级")
    private String airlineInfoGrade;

    /**
     * 舱位代码
     */
    @ExcelProperty(value = "舱位代码")
    private String airlineInfoCode;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String airlineInfoRemarks;

    /**
     * 版本号
     */
    @ExcelProperty(value = "版本号")
    private Long versionCode;

    /**
     * 版本名称
     */
    @ExcelProperty(value = "版本名称")
    private String versionName;

    /**
     * 是否删除标识 0-不删除  1-删除
     */
    @ExcelProperty(value = "是否删除标识 0-不删除  1-删除")
    private String deleteFlag;


}
