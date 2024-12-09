package org.smartlink.server.nc.ocr.service.yesfp.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author L
 * @title 税务云识别请求体
 * @description 税务云识别请求体
 * @date
 */
@NoArgsConstructor
@Data
public class YesfpOcrRequest {

    /**
     * 组织编码和纳税人识别号不能同时为空
     */
    @JsonProperty("nsrsbh")
    private String nsrsbh;
    /**
     * 组织编码和纳税人识别号不能同时为空
     */
    @JsonProperty("orgcode")
    private String orgcode;
    /**
     * base64编码图片
     *
     */
    @JsonProperty("file")
    private String file;
}
