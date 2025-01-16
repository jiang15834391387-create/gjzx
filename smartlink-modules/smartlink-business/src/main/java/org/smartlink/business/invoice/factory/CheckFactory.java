package org.smartlink.business.invoice.factory;


import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.invoice.enumd.CheckEnum;
import org.smartlink.business.invoice.service.ICheckStrategy;
import org.smartlink.business.invoice.service.abstractd.AbstractCheckStrategy;
import org.smartlink.common.check.constant.CheckConstant;
import org.smartlink.common.check.exception.CheckException;
import org.smartlink.common.check.properties.CheckProperties;
import org.smartlink.common.check.properties.RuiZhenCheckProperties;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;

/**
 * 查验工厂
 *
 */
@Slf4j
public class CheckFactory {

    /**
     * 初始化工厂
     */
    public static void init() {
        log.info("初始化查验工厂");
        RedisUtils.subscribe(CheckConstant.CACHE_CONFIG_KEY, String.class, type -> {
            AbstractCheckStrategy strategy = getStrategy(type);
            // 未初始化不处理
            if (strategy.isInit) {
                refresh(type);
                log.info("订阅刷新查验厂商配置 => " + type);
            }
        });
    }

    /**
     * 获取默认实例
     */
    public static ICheckStrategy instance() {
        Object cacheMapValue = RedisUtils.getCacheMapValue(CheckConstant.CHECK_CONFIG_KEY, CheckConstant.CHECK_CONFIG);
        log.info("默认查验厂商为:{}", cacheMapValue);
        RuiZhenCheckProperties properties = JsonUtils.parseObject(cacheMapValue.toString(), RuiZhenCheckProperties.class);
        log.info("默认查验厂商为:{}", properties.getRegenai());
        String type = properties.getRegenai();
        if (StringUtils.isEmpty(type)) {
            throw new CheckException("查验服务类型无法找到!");
        }
        return instance(type);
    }

    /**
     * 根据类型获取实例
     */
    private static ICheckStrategy instance(String type) {
        CheckEnum checkEnum = CheckEnum.find(type);
        if (checkEnum == null) {
            throw new CheckException("查验服务类型无法找到!");
        }
        AbstractCheckStrategy strategy = getStrategy(type);
        if (!strategy.isInit) {
            refresh(type);
        }
        return strategy;
    }

    private static void refresh(String type) {
        Object cacheMapValue = RedisUtils.getCacheMapValue(CheckConstant.CHECK_CONFIG_KEY, CheckConstant.CHECK_CONFIG);
        log.info("默认查验厂商为:{}", cacheMapValue);
        RuiZhenCheckProperties properties = JsonUtils.parseObject(cacheMapValue.toString(), RuiZhenCheckProperties.class);
        CheckProperties checkProperties = new CheckProperties();
        checkProperties.setDetailInfo(JsonUtils.toJsonString(properties));
        if (properties == null) {
           log.error("查验系统异常" + type + "配置信息不存在!{} ",properties);
           throw new CheckException("查验系统异常" + type + "配置信息不存在!");
        }
        getStrategy(type).init(checkProperties);
    }

    private static AbstractCheckStrategy getStrategy(String type) {
        CheckEnum checkEnum = CheckEnum.find(type);
        if (checkEnum == null) {
            throw new CheckException("查验服务类型无法找到!");
        }
        return (AbstractCheckStrategy) SpringUtils.getBean(checkEnum.getBeanClass());
    }
}
