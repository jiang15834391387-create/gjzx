package org.smartlink.web.domain.dto.bipresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author L
 * @title bip统一返回
 * @description bip统一返回
 * @date
 */
@NoArgsConstructor
@Data
public class BipResponse {
    @JsonProperty("code")
    private String code;
    @JsonProperty("message")
    private String message;
    private String imgOcrToKen;


}
