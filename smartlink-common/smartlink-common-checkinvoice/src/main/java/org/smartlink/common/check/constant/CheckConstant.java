package org.smartlink.common.check.constant;

/**
 * 查验常量
 *
 */
public interface CheckConstant {

    /**
     * 查验成功
     */
    String SUCCESS_CHECK = "1";

    /**
     * 查验失败
     */
    String ERROE_CHECK="0";
    /**
     * 查验模块KEY
     */
    String SYS_CHECK_KEY = "sys_check:";

    /**
     * 查验配置KEY
     */
    String CHECK_CONFIG_KEY = "CheckConfig";

    /**
     * 缓存配置KEY
     */
    String CACHE_CONFIG_KEY = SYS_CHECK_KEY + CHECK_CONFIG_KEY;


}
