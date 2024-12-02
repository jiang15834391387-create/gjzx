package org.smartlink.web.ocr.service.nccbip;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;

/**
 * @description: BIP坐标工具类
 * @author: L
 * @create:
 **/
public class BIPCoordinateUtil {

    public static String getCoordinateStr(Integer[] coordinate){
        if(ObjectUtil.isNotEmpty(coordinate) && coordinate.length == 4){
            JSONObject coordinateJson = new JSONObject();
            coordinateJson.put("topLeft",new int[]{coordinate[0],coordinate[1]});
            coordinateJson.put("topRight",new int[]{coordinate[2],coordinate[1]});
            coordinateJson.put("bottomLeft",new int[]{coordinate[0],coordinate[3]});
            coordinateJson.put("bottomRight",new int[]{coordinate[2],coordinate[3]});
            return coordinateJson.toJSONString();
        }
        return null;
    }

    public static String getCciCoordinateStr(Integer[] coordinate){
        if(ObjectUtil.isNotEmpty(coordinate) && coordinate.length == 8){
            JSONObject coordinateJson = new JSONObject();
            coordinateJson.put("topLeft",new int[]{coordinate[0],coordinate[1]});
            coordinateJson.put("topRight",new int[]{coordinate[2],coordinate[3]});
            coordinateJson.put("bottomLeft",new int[]{coordinate[4],coordinate[5]});
            coordinateJson.put("bottomRight",new int[]{coordinate[6],coordinate[7]});
            return coordinateJson.toJSONString();
        }
        return null;
    }
}
