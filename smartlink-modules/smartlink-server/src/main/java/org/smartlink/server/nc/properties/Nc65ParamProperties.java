package org.smartlink.server.nc.properties;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: NC65参数配置
 * @author: L
 * @create:
 **/
@Data
public class Nc65ParamProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 影像系统访问地址
     */
    private String systemIp;
    /**
     * 影像系统访问端口
     */
    private String systemPort;
    /**
     * NC系统数据源
     */
    private String dataSource;
    /**
     * NC系统client id
     */
    private String clientId;
    /**
     * NC源路径
     */
    private String baseUrl;
    /**
     * 业务系统ws接口地址
     */
    private String wsUrl;
    /**
     * 保存台账开关
     */
    private String saveStandOff;

}
