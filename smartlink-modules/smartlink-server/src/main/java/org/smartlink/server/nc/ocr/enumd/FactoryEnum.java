package org.smartlink.server.nc.ocr.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.server.nc.ocr.service.factory.CcintFactory;
import org.smartlink.server.nc.ocr.service.factory.GlorityFactory;
import org.smartlink.server.nc.ocr.service.factory.YesfpFactory;

/**
 * OCR工厂枚举
 *
 * @author L
 */
@Getter
@AllArgsConstructor
public enum FactoryEnum {
    /**
     * 合合
     */
    CCINT("CCINT", CcintFactory.class),
    /**
     * 票小秘
     */
    GLORITY("GLORITY", GlorityFactory.class),
    /**
     * 税务云
     */
    YESFP("YESFP", YesfpFactory.class),
    ;

    private final String value;

    private final Class<?> beanClass;

    public static FactoryEnum find(String value) {
        for (FactoryEnum enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
