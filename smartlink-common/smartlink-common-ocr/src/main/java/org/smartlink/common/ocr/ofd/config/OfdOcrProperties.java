package org.smartlink.common.ocr.ofd.config;

import lombok.Data;

/**
 * OFD发票识别配置类
 *
 * @author lqm
 * @date 2025-01-08
 */
@Data
public class OfdOcrProperties {
    /**
     * 是否启用OFD发票识别
     */
    private boolean enabled;

    /**
     * OFD解析器类型 (default, custom)
     */
    private String parserType = "default";

    /**
     * OFD中XML文件路径
     */
    private String xmlFilePath;
//        "Doc_0/Attachs/original_invoice.xml";

    /**
     * 扩展字段
     */
    private String ext1;

    /**
     * 备注
     */
    private String remark;
}
