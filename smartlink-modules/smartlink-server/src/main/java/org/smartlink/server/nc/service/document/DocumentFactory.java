package org.smartlink.server.nc.service.document;


import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.server.nc.service.document.enumd.DocEnumd;
import org.smartlink.server.nc.service.document.exception.DocumentException;
import org.smartlink.server.nc.service.document.strategy.DocumentStrategy;


/**
 * @author L
 * @title 附件处理
 * @description 附件处理工厂
 * @date
 */
@Slf4j
public class DocumentFactory {
    /**
     * 根据类型获取实例
     */
    public static DocumentStrategy instance(String type) {
        DocEnumd enumd = DocEnumd.find(type);
        if (enumd == null) {
            throw new DocumentException("附件处理服务类型无法找到!");
        }
        return getStrategy(type);
    }

    private static DocumentStrategy getStrategy(String type) {
        DocEnumd enumd = DocEnumd.find(type);
        return (DocumentStrategy) SpringUtils.getBean(enumd.getBeanClass());
    }
}
