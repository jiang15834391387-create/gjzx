package org.smartlink.server.nc.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.server.nc.strategy.Nc65ServiceStrategy;
import org.smartlink.server.nc.strategy.NccBipServiceStrategy;
import org.smartlink.server.nc.strategy.NccServiceStrategy;


/**
 * 对象存储服务商枚举
 *
 * @author L
 */
@Getter
@AllArgsConstructor
public enum NcConfigEnumd {

    /**
     * NC Cloud:1909 2005 2105 2111 2207
     */
    NCC("NCC", NccServiceStrategy.class),
    /**
     * BIP
     */
    NCCBIP("NCCBIP", NccBipServiceStrategy.class),
    /**
     * NC65
     */
    NC65("NC65", Nc65ServiceStrategy.class);

    private final String value;

    private final Class<?> beanClass;

    public static NcConfigEnumd find(String value) {
        for (NcConfigEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
