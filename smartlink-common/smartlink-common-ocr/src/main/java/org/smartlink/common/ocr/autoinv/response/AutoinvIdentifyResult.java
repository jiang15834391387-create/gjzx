package org.smartlink.common.ocr.autoinv.response;

import cn.hutool.json.JSONObject;
import lombok.Data;
import java.util.List;

/**
 * autoinv OCR 单个发票识别结果类
 */
@Data
public class AutoinvIdentifyResult extends JSONObject {
    /**
     * 识别坐标（左上，右上，右下，左下 的横纵坐标）
     */
    private List<Double> coordinate;

    /**
     * 发票种类
     */
    private String data_type;

    /**
     * 发票种类编码
     */
    private String type;

    /**
     * 状态码
     */
    private String code;

    /**
     * 状态信息
     */
    private String msg;
}

