package org.smartlink.server.nc.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.token.LoginVo;

public interface ExternalTokenService {

    /**
     * 将租户id放置当前登录用户中
     * @param tenantId 租户id
     * @return token
     */
    String getBIPToken(String tenantId,String userName,String userCode,String userId);

    /**
     * 单点登录
     * @param userId 用户id
     * @return TokenValue
     */
    String getNccToken(String userId,String userNo);

    R<LoginVo> getToken();
}
