package org.smartlink.server.nc.factory;


import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.NcConstant;
import org.smartlink.server.nc.enumd.NcConfigEnumd;
import org.smartlink.server.nc.exception.NCServiceException;
import org.smartlink.server.nc.properties.NcProperties;
import org.smartlink.server.nc.strategy.AbstractNCStrategy;
import org.smartlink.server.nc.strategy.INcStrategy;
import org.smartlink.server.nc.utils.StringUtils;


/**
 * NC参数实例工厂
 *
 * @author L
 */
@Slf4j
public class NcConfigFactory {

    /**
     * 获取默认实例
     */
    public static INcStrategy instance() {
        // 获取redis 默认参数
        String type = RedisUtils.getCacheObject(NcConstant.CACHE_CONFIG_KEY);
        log.info("默认NC业务系统参数服务类型为:{}", type);
        if (StringUtils.isEmpty(type)) {
            throw new NCServiceException("NC业务系统参数服务类型无法找到!");
        }
        return instance(type);
    }

    /**
     * 根据类型获取实例
     */
    private static INcStrategy instance(String type) {
        NcConfigEnumd ncConfigEnumd = NcConfigEnumd.find(type);
        if (ncConfigEnumd == null) {
            throw new NCServiceException("NC业务系统参数服务类型无法找到!");
        }
        AbstractNCStrategy strategy = getStrategy(type);
        if (!strategy.isInit) {
            refresh(type);
        }
        return strategy;
    }
    private static void refresh(String type) {
        Object json = RedisUtils.getCacheObject(NcConstant.SYS_NC_KEY + type);
        NcProperties properties = JsonUtils.parseObject(json.toString(), NcProperties.class);
        if (properties == null) {
            throw new NCServiceException("NC业务系统参数系统异常, '" + type + "'配置信息不存在!");
        }
        getStrategy(type).init(properties);
    }

    private static AbstractNCStrategy getStrategy(String type) {
        NcConfigEnumd ncConfigEnumd = NcConfigEnumd.find(type);
        if (ncConfigEnumd == null) {
            throw new NCServiceException("NC业务系统参数服务类型无法找到!");
        }
        return (AbstractNCStrategy) SpringUtils.getBean(ncConfigEnumd.getBeanClass());
    }
}
