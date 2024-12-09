package org.smartlink.server.nc.ocr.service.yesfp.request;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: YesfpOcrSaveResult </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class YesfpOcrSaveResult {
    /**
     * 请求状态. 0000:成功, 其他:失败
     */
    private String code;
    // yesfp3.0
    private String msg;
    // bip
    private String message;
}
