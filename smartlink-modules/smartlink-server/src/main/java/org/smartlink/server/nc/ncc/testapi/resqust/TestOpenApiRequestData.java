package org.smartlink.server.nc.ncc.testapi.resqust;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 请求类data
 * @author: L
 * @create:
 **/
@Data
@Component
public class TestOpenApiRequestData {

    /**
     * 方法名
     */
    @Value("${wsdl.methodName}")
    private String methodName;

    @Value("${ncc.wsUrl}")
    private String wsUrl;

    @Value("${ncc.baseUrl}")
    private String baseUrl;
    /**
     * 厂商编码
     */
    @Value("${wsdl.factoryCode}")
    private String factoryCode;

    @Value("${wsdl.dataSource}")
    private String dataSource;

}
