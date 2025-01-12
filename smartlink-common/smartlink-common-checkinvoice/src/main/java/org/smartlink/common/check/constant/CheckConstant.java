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

    //查验厂商
    String CHECK_SUPPLIER_RUIZHEN = "RuiZhen";

    //查验地址
    String CHECK_URL_RUIZHEN = "https://api.regenai.com/v1/item/invoice_validation";

    //查验AppKey
    String CHECK_APPKEY_RUIZHEN = "d7k9798mbip112a1";

    //查验AppSecret
    String CHECK_APPSECRET_RUIZHEN = "msq5883b3eqiel4dm64tdfnh19oc6poh5v915scv";

}
