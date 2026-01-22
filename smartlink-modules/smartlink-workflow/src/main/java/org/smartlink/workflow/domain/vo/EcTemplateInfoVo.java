package org.smartlink.workflow.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import org.smartlink.workflow.domain.TemplateInfo;

import java.io.Serial;
import java.io.Serializable;


/**
 * 套版信息视图对象 ec_template_info
 *
 * @author Lion Li
 * @date 2025-04-02
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = TemplateInfo.class)
public class EcTemplateInfoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 套版名称
     */
    @ExcelProperty(value = "套版名称")
    private String templateName;

    /**
     * 套版编码
     */
    @ExcelProperty(value = "套版编码")
    private String templateCode;

    /**
     * 套版存储地址路径
     */
    @ExcelProperty(value = "套版存储地址路径")
    private String templateStoragePath;

    /**
     * 套版类型（http还是本地的这种）
     */
    @ExcelProperty(value = "套版类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "h=ttp还是本地的这种")
    private String templateType;

    /**
     * 套版表体行数(限制行数）
     */
    @ExcelProperty(value = "套版表体行数(限制行数）")
    private String templateNumberRows;


}
