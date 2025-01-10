package org.smartlink.common.ocr.properties;

import lombok.Data;
import org.smartlink.common.ocr.glority.config.GlorityOcrProperties;

/**
 * @author lqm
 * @title 识别配置
 * @description 识别配置
 */
@Data
public class OcrProperties {
    /**
     * 配置KEY
     */
    private String configKey;
    /**
     * 配置详情
     */
    private GlorityOcrProperties detailInfo;
    /**
     * 状态（是否启用）
     */
    private String status;
    /**
     * 扩展字段
     */
    private String ext1;
    /**
     * 备注
     */
    private String remark;
}
