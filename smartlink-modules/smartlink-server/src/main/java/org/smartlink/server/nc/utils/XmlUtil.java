package org.smartlink.server.nc.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import org.dom4j.Element;

/**
 * @description: xml工具类
 * @author: L
 * @create:
 **/
public class XmlUtil {

    /**
     * ncc xml请求报文md5校验
     */
    public static Boolean xmlDataVerify(Element root) {
        Element reqHead = root.element("ReqHead");
        if(ObjectUtil.isEmpty(reqHead)){
            reqHead = root.element("Head");
        }
        String clientCode = reqHead.elementText("clientcode").trim();
        String serviceCode = reqHead.elementText("servicecode").trim();
        String time = reqHead.elementText("time").trim();
        String ticket = reqHead.elementText("ticket").trim();
        String md5str = DigestUtil.md5Hex(clientCode+"&"+serviceCode+"&"+time);
        return StrUtil.equals(ticket, md5str);
    }

}
