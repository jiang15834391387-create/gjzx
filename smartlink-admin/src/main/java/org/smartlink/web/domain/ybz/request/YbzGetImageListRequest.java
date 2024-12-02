package org.smartlink.web.domain.ybz.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author L
 * @title 友报账获取影像列表入参
 * @description 友报账获取影像列表入参
 * @date
 */
@NoArgsConstructor
@Data
public class YbzGetImageListRequest {
    @JsonProperty("tenanId")
    private String tenanId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("barCodes")
    private List<String> barCodes;
}
