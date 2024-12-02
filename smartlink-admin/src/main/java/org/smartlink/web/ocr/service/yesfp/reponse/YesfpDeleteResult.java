package org.smartlink.web.ocr.service.yesfp.reponse;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title: YesfpDeleteResult </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
public class YesfpDeleteResult {
    /**
     * 请求状态. 0000:成功, 其他:失败
     */
    private String code;
    // yesfp3.0
    private String msg;
    // bip
    private String message;
}
