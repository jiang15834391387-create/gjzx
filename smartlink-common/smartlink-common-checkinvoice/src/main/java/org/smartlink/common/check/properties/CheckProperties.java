package org.smartlink.common.check.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * 查验厂商配置项
 */
@Data
public class CheckProperties implements Serializable {

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
