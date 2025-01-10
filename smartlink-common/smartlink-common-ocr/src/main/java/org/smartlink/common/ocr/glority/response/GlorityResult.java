package org.smartlink.common.ocr.glority.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 票小秘返回
 *
 * @author lqm
 */
@Getter
@Setter
public class GlorityResult {

    /**
     * 请求状态. 1:成功, 0:失败
     */
    private String result;

    private String error;

    private String message;

    private GlorityResponse response;


}
