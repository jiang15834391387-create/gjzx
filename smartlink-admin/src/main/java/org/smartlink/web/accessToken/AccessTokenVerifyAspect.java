package org.smartlink.web.accessToken;

import com.esotericsoftware.minlog.Log;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.smartlink.common.core.constant.CacheConstants;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.cache.CacheException;


/**
 * ClassName: GuiGuLoginAspect
 * Package: com.atguigu.tingshu.common.login
 * Description:
 *
 * @Author zzq
 * @Create 2023/12/22 16:26
 * @Version 1.0
 */
@Component
@Aspect
public class AccessTokenVerifyAspect {
    @SneakyThrows
    @Before("@annotation(accessTokenVerify)")
    public void beforeMethod(AccessTokenVerify accessTokenVerify) {
        if (accessTokenVerify.required()) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String Authorization = request.getHeader("Authorization");
            if (StringUtils.isEmpty(Authorization)) {
                throw new CacheException("请求头中无accessToken");
            }
            Object accessToken = RedisUtils.getCacheObject(CacheConstants.YINGXIANG_ACCESSTOKEN);
            Log.info("accessToken:" + accessToken);
            if (!Authorization.equals(accessToken)) {
                throw new CacheException("请求头中accessToken为空");
            }
        }

    }
}
