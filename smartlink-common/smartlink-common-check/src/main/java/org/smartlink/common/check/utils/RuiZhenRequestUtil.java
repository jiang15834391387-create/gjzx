package org.smartlink.common.check.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.properties.RuiZhenCheckProperties;

import java.lang.reflect.Field;

/**
 * 睿真查验工具类
 *
 */
@Slf4j
public class RuiZhenRequestUtil {

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

    /**
     * 功能：编码byte[]
     *
     * @param data 源
     * @return {@link Character}
     */
    public static char[] encode(byte[] data) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = ALPHABET[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = ALPHABET[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = ALPHABET[val & 0x3F];
            val >>= 6;
            out[index] = ALPHABET[val & 0x3F];
        }
        return out;
    }


    /**
     * 获取全部的参数
     *
     * @param paramDTO               入参(五要素)
     * @param ruiZhenCheckProperties 查验属性
     * @return 完整参数
     */
    public static String getGlobalInfo(InvoiceCheckParamDTO paramDTO, RuiZhenCheckProperties ruiZhenCheckProperties) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        JSONObject dtoJson = null;
        try {
            dtoJson = RuiZhenRequestUtil.convertToJSONObject(paramDTO);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 生成 Token
        String token = RuiZhenRequestUtil
            .generateToken(ruiZhenCheckProperties.getAppKey(), timestamp, ruiZhenCheckProperties.getAppSecret());
        // 创建最终 JSON 对象
        JSONObject resultJson = new JSONObject();
        resultJson.put("app_key", ruiZhenCheckProperties.getAppKey());
        // 将 dtoJson 中的所有键值对添加到外层
        for (String key : dtoJson.keySet()) {
            resultJson.put(key, dtoJson.get(key));
        }
        resultJson.put("timestamp", timestamp);
        resultJson.put("token", token);
        log.info("最终参数：{}", resultJson.toString());
        return resultJson.toString();
    }

    public static String generateToken(String appkey, String timestamp, String appSecret) {
        return new Md5Hash(appkey + "+" +timestamp +"+" +appSecret).toString();
    }
    public static JSONObject convertToJSONObject(Object obj) throws IllegalAccessException {
        JSONObject jsonObject = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            jsonObject.put(field.getName(), field.get(obj));
        }
        return jsonObject;
    }



}
