package org.smartlink.web.utils;


import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

@Slf4j
public class Java2XmlUtil {
    //XML文件头
    private static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";

    public static String javaBeanToXml(Object obj) {
        String xml = "";
        if (Objects.isNull(obj)) {
            return xml;
        }
//        try {
//            XmlMapper xmlMapper = new XmlMapper();
//            xml = xmlMapper.writeValueAsString(obj);
//        } catch (Exception e) {
//            log.error("javaBeanToXml error, obj={}, xml={}", obj, xml, e);
//            return "";
//        }
        // 添加xml文件头
        return XML_HEAD + xml;
    }
}
