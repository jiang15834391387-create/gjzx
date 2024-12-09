package org.smartlink.server.nc.ocr.service.yesfp.reponse;

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
public class OpenApiAccessTokenData {

    private String access_token;
    //过期秒
    private Long expire;
    //自定义过期时间
    private Long expiredAt;

    public boolean expired() {
        return !(expiredAt != null && System.currentTimeMillis() + 5000 < expiredAt); //设定提前5s更新token
    }

    public void setExpiredAt(Long requestCurrentTimeMillis) {
        this.expiredAt = requestCurrentTimeMillis + (expire * 1000);
    }

}
