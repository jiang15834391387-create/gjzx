package org.smartlink.web.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.smartlink.common.core.domain.model.LoginBody;


/**
 * 忘记密码登录对象
 */
@Data
public class ForgetPasswordReq extends LoginBody {
    /**
     * 手机号
     */
    @NotBlank(message = "{user.phonenumber.not.blank}")
    private String phonenumber;

    /**
     * 短信code
     */
    @NotBlank(message = "{sms.code.not.blank}")
    private String smsCode;

    private String newPassword;
}
