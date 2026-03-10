package org.smartlink.common.ocr.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.common.ocr.autoinv.AutoinvStrategy;
import org.smartlink.common.ocr.glority.GlorityStrategy;
import org.smartlink.common.ocr.ofd.OfdStrategy;
import org.smartlink.common.ocr.xml.XmlStrategy;

/**
 * OCR服务商枚举
 *
 * @author lqm
 */
@Getter
@AllArgsConstructor
public enum OcrEnumd {

    /**
     * 票小秘
     */
    GLORITY("GLORITY", GlorityStrategy.class),

    /**
     * XML发票识别
     */
    XML("XML", XmlStrategy.class),

    /**
     * OFD发票识别
     */
    OFD("OFD", OfdStrategy.class),

    /**
     * autoinv发票识别
     */
    AUTOINV("AUTOINV", AutoinvStrategy.class),
    ;


    private final String value;

    private final Class<?> beanClass;

    public static OcrEnumd find(String value) {
        for (OcrEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
