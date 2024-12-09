package org.smartlink.server.nc.constant;

/**
 * 识别常量
 *
 * @author L
 */
public interface OcrConstant {

    /**
     * 识别模块KEY
     */
    String SYS_OCR_KEY = "sys_ocr:";

    /**
     * 识别配置KEY
     */
    String OCR_CONFIG_KEY = "OcrConfig";

    /**
     * 缓存配置KEY
     */
    String CACHE_CONFIG_KEY = SYS_OCR_KEY + OCR_CONFIG_KEY;

    /**
     * BIP税务云网关接口地址缓存
     */
    String CACHE_BIP_YESFP_GATEWAY_KEY = "sys_bip_gateway_address";

    /**
     * BIP税务云token接口地址缓存
     */
    String CACHE_BIP_YESFP_TOEKEN_KEY = "sys_bip_token_address";

    /**
     * BIP税务云网关接口地址缓存过期时间 / 每天更新一次(默认)
     */
    String CACHE_BIP_YESFP_URL_EXPIRE_TIME = "sys_bip_yesfp_url_expire_time";

    /**
     * BIP税务云功能接口缓存认证token信息
     */
    String CACHE_BIP_YESFP_REQUEST_TOKEN_INFO = "sys_bip_yesfp_request_token_info";

}
