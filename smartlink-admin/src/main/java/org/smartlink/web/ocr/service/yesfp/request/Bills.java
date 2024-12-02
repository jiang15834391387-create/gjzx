package org.smartlink.web.ocr.service.yesfp.request;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

/**
 * <p>Title: Bills </p>
 * <p>
 * <p>Description: TODO </p>
 *
 * @author L
 * @version 1.0.0
 * @date
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bills {

    private String imageId;

    private String billType;

    private JSONObject data;
}
