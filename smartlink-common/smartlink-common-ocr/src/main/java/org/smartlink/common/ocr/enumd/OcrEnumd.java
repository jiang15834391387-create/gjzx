package org.smartlink.common.ocr.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.common.ocr.glority.GlorityStrategy;

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
