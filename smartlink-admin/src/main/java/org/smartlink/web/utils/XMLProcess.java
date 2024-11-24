package org.smartlink.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Program：imagesystem
 * @Description：xml解析工具类
 * @Author：L
 * @Create:
 */

public class XMLProcess {


    private static final Logger logger = LoggerFactory.getLogger(XMLProcess.class);
    //头记录
    private static String startStr = "";
    //尾记录
    private static String endStr = "";

    /**
     * 解析webService返回
     * @param body 源字符串
     * @param tag 需要查询的标签
     * @param containsHand 是否包含标签
     * @return
     */
    public static String getXMLResult(String body, String tag, boolean containsHand) {

        String result = "";
        if (!"".equals(body) && null != body) {

            if (body.indexOf("&gt;") != -1) {
                body = body.replaceAll("&gt;", ">");
            }
            if (body.indexOf("&lt;") != -1) {
                body = body.replaceAll("&lt;", "<");
            }
            try {
                merge(tag);
                int startNum = body.indexOf(startStr);
                int endNum = body.indexOf(endStr);
                if (-1 != startNum && -1 != endNum) {
                    if (!containsHand) {
                        startNum += startStr.length();
                    } else {
                        endNum += endStr.length();
                    }
                    result = body.substring(startNum, endNum);
                }else{
                    result = "解析xml出错，找不到头标签或尾标签！";
                }

            } catch (Exception e) {
                e.printStackTrace();
                result = "解析xml出错，请联系影像厂商解决！";
            } finally {
                return result;
            }

        } else {
            result = "xml为空，请联系影像厂商解决！";
        }
        return result;
    }


   private static void merge(String str) {
        startStr = "<" + str + ">";
        endStr = "</" + str + ">";
    }


}
