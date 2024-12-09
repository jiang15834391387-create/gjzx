package org.smartlink.server.nc.invoice.enumd;


import org.smartlink.server.nc.invoice.service.impl.BaiWangCheckStrategy;
import org.smartlink.server.nc.invoice.service.impl.HangXinCheckStrategy;
import org.smartlink.server.nc.invoice.service.impl.YesfpCheckStrategy;

public enum CheckEnum {


    /**
     * 航信
     */
    HANG_XIN("HangXin", HangXinCheckStrategy.class),

    /**
     * 百望
     */
    BAI_WANG("BaiWang", BaiWangCheckStrategy.class),
    /**
     * 税务云
     */
    YESFP("Yesfp", YesfpCheckStrategy.class),
    ;


    CheckEnum(String name, Class<?> beanClass) {
        this.name = name;
        this.beanClass = beanClass;
    }


    private final String name;

    private final Class<?> beanClass;

    public static CheckEnum find(String name) {
        for (CheckEnum checkEnum : values()) {
            if (checkEnum.getName().equals(name)) {
                return checkEnum;
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }
}
