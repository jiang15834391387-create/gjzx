package org.smartlink.web.properties;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.web.constant.NcConstant;

import java.io.Serializable;

/**
 * @description: BIP参数配置
 * @author: L
 * @create:
 **/
@Data
public class BipParamProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * token apiUrl
     */
    private String openApiUrl;
    /**
     * 查验Url
     */
    private String checkUrl;
    /**
     * Ocr Url
     */
    private String ocrUrl;
    /**
     * 发票删除 Url
     */
    private String delInvoiceUrl;
    /**
     * 提交Url
     */
    private String submitUrl;
    /**
     * 同步 查验
     */
    private String synInvoiceUrl;
    /**
     * 同步 OCR
     */
    private String synOcrUrl;
    /**
     * 测试 api
     */
    private String testUrl;
    /**
     * 同步专岗权限信息
     */
    private String synOrgUrl;
    /**
     * 专岗信息判断
     */
    private String synScanUrl;

    /**
     * api key
     */
    private String appKey;

    /**
     * api secret
     */
    private String appSecret;

    /**
     * 影像厂商编号
     */
    private String factoryCode;

    /**
     * 同步协同文件
     */
    private String syncFileUrl;

    /**
     * 删除协同文件
     */
    private String delFileUrl;
    /**
     * 推送票据中心
     */
    private String saveLedger;

    /**
     * 获取默认缓存的NC参数实例对象
     * @return 结果
     */
    public static String getDefaultPropertiesInfo(){
        String key = RedisUtils.getCacheObject(NcConstant.CACHE_CONFIG_KEY).toString();
        String obj = RedisUtils.getCacheObject(NcConstant.SYS_NC_KEY + key).toString();
        JSONObject cache = JSONObject.parseObject(obj);
        String detailInfo = cache.getString("detailInfo");
        return detailInfo;
    }
}
