package org.smartlink.server.nc.ocr.properties;

import lombok.Data;

/**
 * @author L
 * @title 识别配置
 * @description 识别配置
 * @date
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
    private String detailInfo;
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
