package org.smartlink.common.ocr.glority.response;

import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

/**
 * 票小蜜返回数据
 *
 **/
@Getter
@Setter
public class IdentifyResults {

    /**
     * 发票类型
     */
    private String type;

    /**
     * 文件信息
     */
    private String message;

    /**
     * 发票顺时针旋转方向
     */
    private String orientation;

    /**
     * 单张发票区域: 左上点 和 右下点[x1, y1, x2, y2]
     */
    private String[] region;

    /**
     * 发票识别的具体信息, 不同发票类型会不一样
     */
    private JSONObject details;

    /**
     * extra
     */
    private JSONObject extra;


}
