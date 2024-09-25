package org.smartlink.common.oss.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
 * ClassName: util1
 * Package: org.smartlink.web.util
 * Description:
 *
 * @Author 张志强
 * @Create 2024/9/19 19:09
 * @Version 1.0
 */
public class ResponseUtil {

    /**
     * 解析响应为一个map进行返回
     * @param response
     * @return
     * @throws IOException
     */
    public static Map<String, Object> handleResponse(HttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // 将HTTP响应体转换为字符串
        String responseBody = EntityUtils.toString(response.getEntity());

        // 将字符串转换为Map对象
        Map<String, Object> mapResponse = mapper.readValue(responseBody, Map.class);
        return mapResponse;
    }


}
