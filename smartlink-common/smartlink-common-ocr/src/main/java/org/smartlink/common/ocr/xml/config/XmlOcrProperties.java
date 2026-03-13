package org.smartlink.common.ocr.xml.config;

import lombok.Data;

/**
 * XML发票识别配置类
 *
 * @author lqm
 * @date 2025-01-08
 */
@Data
public class XmlOcrProperties {
    /**
     * 是否启用XML发票识别
     */
    private boolean enabled;

    /**
     * XML解析器类型 (default, jackson, dom4j)
     */
    private String parserType = "default";

    /**
     * 扩展字段
     */
    private String ext1;

    /**
     * 备注
     */
    private String remark;
}
