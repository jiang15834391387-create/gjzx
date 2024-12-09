package org.smartlink.server.nc.ocr.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.server.nc.ocr.service.ccint.CcintStrategy;
import org.smartlink.server.nc.ocr.service.glority.GlorityStrategy;
import org.smartlink.server.nc.ocr.service.yesfp.YesfpStrategy;

/**
 * OCR服务商枚举
 *
 * @author L
 */
@Getter
@AllArgsConstructor
public enum OcrEnumd {

    /**
     * 合合
     */
    CCINT("CCINT", CcintStrategy.class),

    /**
     * 票小秘
     */
    GLORITY("GLORITY", GlorityStrategy.class),
    /**
     * 税务云
     */
    YESFP("YESFP", YesfpStrategy.class),
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
