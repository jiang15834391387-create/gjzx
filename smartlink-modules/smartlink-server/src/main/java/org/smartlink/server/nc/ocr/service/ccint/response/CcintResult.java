package org.smartlink.server.nc.ocr.service.ccint.response;

import lombok.Data;

/**
 * @description: 合合实体bean
 * @author: L
 * @create:
 **/
@Data
public class CcintResult {

    /**
     * 处理时间
     */
    private int cost_time;
    /**
     * 识别结果
     */
    private CcResponseBean result;
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应信息
     */
    private String message;
}
