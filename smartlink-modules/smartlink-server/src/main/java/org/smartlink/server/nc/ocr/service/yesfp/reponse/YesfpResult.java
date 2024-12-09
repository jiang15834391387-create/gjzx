package org.smartlink.server.nc.ocr.service.yesfp.reponse;

import com.alibaba.fastjson.JSONArray;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: YesfpResult </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class YesfpResult {
    /**
     * yesfp3.0:请求状态. 0000:成功, 其他:失败
     * bip:请求状态. 200:成功, 其他:失败
     */
    private String code;
    /**
     * bip返回提示信息
     */
    private String message;
    /**
     * yesfp3.0返回信息
     */
    private String msg;

    // yesfp3.0
    private JSONArray datas;
    // bip
    private JSONArray data;
}
