package org.smartlink.common.ocr.autoinv.response;

import lombok.Data;
import java.util.List;

/**
 * autoinv OCR 识别结果响应类
 */
@Data
public class AutoinvResult {
    /**
     * 识别结果列表
     */
    private List<AutoinvIdentifyResult> result;

    /**
     * 识别用时（毫秒）
     */
    private Integer recognize_time;

    /**
     * 状态码（1：识别成功，0：识别失败）
     */
    private String code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 请求唯一标识符
     */
    private String request_id;
}
