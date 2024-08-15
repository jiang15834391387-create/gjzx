package org.smartlink.server.nodeType.domain.vo;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import org.smartlink.common.excel.annotation.ExcelDictFormat;
import org.smartlink.common.excel.convert.ExcelDictConvert;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.smartlink.server.nodeType.domain.DataNodeType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;



/**
 * 树节点视图对象 data_node_type
 *
 * @author Lion Li
 * @date 2024-08-15
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = DataNodeType.class)
public class DataNodeTypeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private String id;

    /**
     * 节点类型
     */
    @ExcelProperty(value = "节点类型")
    private String nodeType;

    /**
     * 节点名称
     */
    @ExcelProperty(value = "节点名称")
    private String nodeName;

    /**
     * 节点编码
     */
    @ExcelProperty(value = "节点编码")
    private String nodeCode;

    /**
     * 父节点ID
     */
    @ExcelProperty(value = "父节点ID")
    private String parentId;


}
