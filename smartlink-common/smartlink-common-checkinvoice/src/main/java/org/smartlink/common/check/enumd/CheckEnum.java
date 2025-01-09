package org.smartlink.common.check.enumd;


import org.smartlink.common.check.service.impl.RuiZhenCheckStrategy;

public enum CheckEnum {


    /**
     * 睿真
     */
    HANG_XIN("RuiZhen", RuiZhenCheckStrategy.class);


    /**
     * 百望
     */
   // BAI_WANG("BaiWang", BaiWangCheckStrategy.class),


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
