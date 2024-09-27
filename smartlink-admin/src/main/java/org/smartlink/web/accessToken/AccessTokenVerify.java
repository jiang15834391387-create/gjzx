package org.smartlink.web.accessToken;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: GuiGuLogin
 * Package: com.atguigu.tingshu.common.login
 * Description:
 *
 * @Author zzq
 * @Create 2023/12/22 16:22
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessTokenVerify {
    boolean required() default true;
}

