package org.smartlink.server.nc.properties;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.NcConstant;


/**
 * @author L
 * @title NC参数配置
 * @description NC参数配置
 * @date
 */
@Data
public class NcProperties {
    /**
     * 配置KEY
     */
    private String configKey;
    /**
     * 配置详情
     */
    private String detailInfo;
    /**
     * 状态（是否启用）
     */
    private String status;
    /**
     * 扩展字段
     */
    private String ext1;
    /**
     * 备注
     */
    private String remark;

    /**
     * 获取默认缓存的NC参数实例对象
     * @return 结果
     */
    public static JSONObject getDefaultPropertiesInfo(){
        String key = RedisUtils.getCacheObject(NcConstant.CACHE_CONFIG_KEY).toString();
        String obj = RedisUtils.getCacheObject(NcConstant.SYS_NC_KEY + key).toString();
        JSONObject cache = JSONObject.parseObject(obj);
        String detailInfo = cache.getString("detailInfo");
        return JSONObject.parseObject(detailInfo);
    }

    /**
     * 获取默认缓存的NC参数实例对象启用状态
     * @return 结果
     */
    public static String getDefaultPropertiesStatus(){
        String key = RedisUtils.getCacheObject(NcConstant.CACHE_CONFIG_KEY).toString();
        String obj = RedisUtils.getCacheObject(NcConstant.SYS_NC_KEY + key).toString();
        JSONObject cache = JSONObject.parseObject(obj);
        String status = cache.getString("status");
        return status;
    }

}
