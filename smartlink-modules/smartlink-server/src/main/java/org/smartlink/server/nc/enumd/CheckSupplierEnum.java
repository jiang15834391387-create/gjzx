package org.smartlink.server.nc.enumd;

/**
 * 查验供应商枚举
 *
 * @author L
 */
public enum CheckSupplierEnum {

    /**
     * 航信
     */
    HANG_XIN(20001, "航信"),
    /**
     * 百旺
     */
    BAI_WANG(20002, "百旺"),
    /**
     * 税务云
     */
    YESFP(20003, "税务云"),
    /**
     * NC65税务云
     */
    YESFP_NC_SIX_FIVE(20004, "NC65税务云");

    private final Integer code;
    private final String name;

    public static String getName(Integer code) {
        for (CheckSupplierEnum checkSupplierEnum : CheckSupplierEnum.values()) {
            if (checkSupplierEnum.code.equals(code)) {
                return checkSupplierEnum.name;
            }
        }
        return null;
    }

    CheckSupplierEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
