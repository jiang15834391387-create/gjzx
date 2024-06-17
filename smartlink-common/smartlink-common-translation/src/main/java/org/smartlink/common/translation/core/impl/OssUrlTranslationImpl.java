package org.smartlink.common.translation.core.impl;

import org.smartlink.common.core.service.OssService;
import org.smartlink.common.translation.annotation.TranslationType;
import org.smartlink.common.translation.constant.TransConstant;
import org.smartlink.common.translation.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * OSS翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = TransConstant.OSS_ID_TO_URL)
public class OssUrlTranslationImpl implements TranslationInterface<String> {

    private final OssService ossService;

    @Override
    public String translation(Object key, String other) {
        if (key instanceof String ids) {
            return ossService.selectUrlByIds(ids);
        } else if (key instanceof Long id) {
            return ossService.selectUrlByIds(id.toString());
        }
        return null;
    }
}
