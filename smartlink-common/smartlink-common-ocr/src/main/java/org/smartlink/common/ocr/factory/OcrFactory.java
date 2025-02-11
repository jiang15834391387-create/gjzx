package org.smartlink.common.ocr.factory;




import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.file.Constants;
import org.smartlink.common.core.utils.file.ParamConstants;
import org.smartlink.common.ocr.abstractd.AbstractOcrStrategy;
import org.smartlink.common.ocr.constant.OcrConstant;
import org.smartlink.common.ocr.core.IOcrStrategy;
import org.smartlink.common.ocr.enumd.OcrEnumd;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.properties.OcrProperties;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.redis.utils.RedisUtils;

/**
 * 识别工厂
 *
 * @author lqm
 */
@Slf4j
public class OcrFactory {

    /**
     * 初始化工厂
     */
    public static void init() {
        log.info("初始化识别工厂");
        RedisUtils.subscribe(OcrConstant.CACHE_CONFIG_KEY, String.class, type -> {
            AbstractOcrStrategy strategy = getStrategy(type);
            // 未初始化不处理
            if (strategy.isInit) {
                refresh(type);
                log.info("订阅刷新识别厂商配置 => " + type);
            }
        });
    }

    /**
     * 获取默认实例
     */
    public static IOcrStrategy instance() {
        // 获取redis 默认厂商
        String type = RedisUtils.getCacheObject(OcrConstant.CACHE_CONFIG_KEY);
        log.info("默认识别厂商为:{}", type);
        if (StringUtils.isEmpty(type)) {
            throw new OcrException("识别服务类型无法找到!");
        }
        return instance(type);
    }

    /**
     * 根据类型获取实例
     */
    public static IOcrStrategy instance(String type) {
        OcrEnumd checkEnum = OcrEnumd.find(type);
        if (checkEnum == null) {
            throw new OcrException("识别服务类型无法找到!");
        }
        AbstractOcrStrategy strategy = getStrategy(type);
        //如果策略未初始化，调用 refresh(type) 方法执行初始化操作。
        if (!strategy.isInit) {
            refresh(type);
        }
        return strategy;
    }

    /**
     * 获取当前厂商识别实例
     */
    public static Class<?> instanceObj() {
        // 获取redis 默认厂商
//        String type = RedisUtils.getCacheObject(OcrConstant.CACHE_CONFIG_KEY);
        String type = RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, OcrConstant.CACHE_CONFIG_KEY);
        log.info("默认OCR识别为:{}", type);
        if (StringUtils.isEmpty(type)) {
            throw new OcrException("OCR识别服务类型无法找到!");
        }
        OcrEnumd factoryEnumd = OcrEnumd.find(type);
        if (factoryEnumd == null) {
            throw new OcrException("查验服务类型无法找到!");
        }
        return factoryEnumd.getBeanClass();
    }

    private static void refresh(String type) {
//        Object json = RedisUtils.getCacheObject(OcrConstant.SYS_OCR_KEY + type);
        Object json = RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, OcrConstant.SYS_OCR_KEY + type);
        OcrProperties properties = JsonUtils.parseObject(json.toString(), OcrProperties.class);
        if (properties == null) {
            throw new OcrException("识别系统异常, '" + type + "'配置信息不存在!");
        }
        getStrategy(type).init(properties);
    }

    private static
    AbstractOcrStrategy getStrategy(String type) {
        OcrEnumd checkEnum = OcrEnumd.find(type);
        if (checkEnum == null) {
            throw new OcrException("识别服务类型无法找到!");
        }
        return (AbstractOcrStrategy) SpringUtils.getBean(checkEnum.getBeanClass());
    }
}
