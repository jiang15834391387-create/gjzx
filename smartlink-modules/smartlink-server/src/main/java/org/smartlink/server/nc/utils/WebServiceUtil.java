package org.smartlink.server.nc.utils;

import cn.hutool.core.util.ObjectUtil;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

/**
 * @description: WebService请求工具类
 * @author: L
 * @create:
 **/
public class WebServiceUtil {

    /**
     * 提供给NCC发起WebService请求
     * @param urlWsdl ws地址
     * @param soap 参数
     * @param targetNamespace 接口命名空间
     * @return 返回结果
     * @throws Exception
     */
    public static String sendWebService(String urlWsdl, String soap, String targetNamespace) throws Exception {
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(urlWsdl);
        Object[] invoke = client.invoke(targetNamespace,soap);
        if(ObjectUtil.isEmpty(invoke)){
            throw new Exception("未获取到接口响应结果");
        }
        return invoke[0].toString();
    }

}
