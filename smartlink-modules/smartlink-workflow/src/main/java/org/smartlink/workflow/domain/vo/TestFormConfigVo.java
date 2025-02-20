package org.smartlink.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.TestFormConfig;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 单配置视图对象 test_form_config
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestFormConfig.class)
public class TestFormConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 表单id
     */
    @ExcelProperty(value = "表单id")
    private Long formId;

    private String fieldTypeData;

    /**
     * 字段英文名称
     */
    private String fieldValue;
    /**
     * 字段名称
     */
    @ExcelProperty(value = "字段名称")
    private String fieldName;

    /**
     * 字段名称
     */
    @ExcelProperty(value = "字段名称")
    private String fieldType;

    /**
     * 是否必填（0否 1是）
     */
    @ExcelProperty(value = "是否必填", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=否,1=是")
    private Integer fieldRequired;
}
