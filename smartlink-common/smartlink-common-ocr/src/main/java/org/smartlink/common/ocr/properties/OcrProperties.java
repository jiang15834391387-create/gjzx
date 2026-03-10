package org.smartlink.common.ocr.properties;

import lombok.Data;
import org.smartlink.common.ocr.autoinv.config.AutoinvOcrProperties;
import org.smartlink.common.ocr.glority.config.GlorityOcrProperties;
import org.smartlink.common.ocr.ofd.config.OfdOcrProperties;
import org.smartlink.common.ocr.xml.config.XmlOcrProperties;

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
     * 票小秘配置详情
     */
    private GlorityOcrProperties detailInfo;
    /**
     * XML发票识别配置详情
     */
    private XmlOcrProperties xmlDetailInfo;
    /**
     * OFD发票识别配置详情
     */
    private OfdOcrProperties ofdDetailInfo;
    /**
     * OFD发票识别配置详情
     */
    private AutoinvOcrProperties autoinvOcrProperties;
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
