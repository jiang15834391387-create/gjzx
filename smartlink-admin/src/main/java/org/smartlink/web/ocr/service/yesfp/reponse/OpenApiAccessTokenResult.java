package org.smartlink.web.ocr.service.yesfp.reponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本类主要用于
 *
 * @author L
 * @since
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiAccessTokenResult {

    private String code;

    private String message;

    private OpenApiAccessTokenData data;

}
