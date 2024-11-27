package org.smartlink.web.ocr.service.yesfp.utils;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONObject;

/**
 * @description: 税务云坐标工具类
 * @author: L
 * @create:
 **/
public class YesfpCoordinateUtil {

    public static String[] getCoordinateArr(JSONObject coordinate){
        return new  String[]{coordinate.getString("startX"),coordinate.getString("startY"),coordinate.getString("endX"),coordinate.getString("endY")};
    }

    public static String getCoordinateStr(JSONObject coordinate){
        int[] intRegion = {Convert.toInt(coordinate.getString("startX")), Convert.toInt(coordinate.getString("startY")), Convert.toInt(coordinate.getString("endX")), Convert.toInt(coordinate.getString("endY"))};
        // int[] intRegion = Arrays.stream(strings).mapToInt(Integer::parseInt).toArray();
        JSONObject coordinateJson = new JSONObject();
        coordinateJson.put("topLeft",new int[]{intRegion[0],intRegion[1]});
        coordinateJson.put("bottomLeft",new int[]{intRegion[0],intRegion[3]});
        coordinateJson.put("topRight",new int[]{intRegion[2],intRegion[1]});
        coordinateJson.put("bottomRight",new int[]{intRegion[2],intRegion[3]});
        return coordinateJson.toJSONString();
    }

}
