package org.smartlink.server.nc.strategy;


import org.smartlink.server.nc.properties.NcProperties;

/**
 * @description: NC业务方法工厂抽象类
 * @author: L
 * @create:
 **/
public abstract class AbstractNCStrategy implements INcStrategy {

    protected NcProperties properties;

    public boolean isInit = false;

    public void init(NcProperties properties) {
        this.properties = properties;
    }
}
