package org.smartlink.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.workflow.domain.TestFormManage;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 单管理视图对象 test_form_manage
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TestFormManage.class)
public class TestFormManageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 单据名称
     */
    @ExcelProperty(value = "单据名称")
    private String formName;
    /**
     * 单据logo
     */
    private String formLogo;
    private String formType;



    /**
     * 单据绑定id
     */
    private Long formBindId;
    /**
     * 单据绑定名称
     */
    @ExcelProperty(value = "单据绑定名称")
    private String formBindName;
    /**
     * 是否绑定工作流程（0否，1是）
     */
    private Integer isBindModel;

    private String modelId;
    /**
     *
     */
    @ExcelProperty(value = "")
    private String remark;
    /**
     * 排序
     */
    private Long sort;
    /**
     * 是否删除（0否，1是）
     */
    @ExcelProperty(value = "是否删除", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=否，1是")
    private Integer isDeleted;
    private Long categoryId;
    private String categoryName;
    private String categoryType;
}
