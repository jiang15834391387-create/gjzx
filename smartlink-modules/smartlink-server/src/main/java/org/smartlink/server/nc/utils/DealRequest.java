package org.smartlink.server.nc.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @description: NCC封装参数工具类
 * @author: L
 * @create:
 **/
public class DealRequest {

     private static final String level = "L0";

    public static String dealRequestBody(String source, String security_key) throws Exception {
        String result;
        if (!StringUtils.isEmpty(level) && !"L0".equals(level)) {
            if ("L1".equals(level)) {
                result = EncryptionUtil.symEncrypt(security_key, source);
            } else if ("L2".equals(level)) {
                result = CompressUtil.gzipCompress(source);
            } else if ("L3".equals(level)) {
                result = EncryptionUtil.symEncrypt(security_key, CompressUtil.gzipCompress(source));
            } else {
                if (!"L4".equals(level)) {
                    throw new Exception("无效的安全等级");
                }
                result = CompressUtil.gzipCompress(EncryptionUtil.symEncrypt(security_key, source));
            }
        } else {
            result = source;
        }

        return result;
    }
}
