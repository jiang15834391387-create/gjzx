package org.smartlink.web.service;

import org.smartlink.common.core.domain.model.PasswordLoginBody;
import org.smartlink.system.domain.vo.SysClientVo;
import org.smartlink.web.domain.vo.LoginVo;

public interface ILoginService {
    LoginVo applogin(PasswordLoginBody loginBody, SysClientVo client);

}
