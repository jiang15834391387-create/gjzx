package org.smartlink.web.domain.ybz.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


/**
 * @author L
 * @title 删除图片入参
 * @description 删除图片入参
 * @date 2022-06
 */
@Data
public class YbzDeleteImageRequest {
    @JsonProperty("barcode")
    @NotBlank(message = "barcode不能为空")
    String barcode;
    @JsonProperty("state")
    String state;
    @JsonProperty("imageKey")
    String imageKey;
}
