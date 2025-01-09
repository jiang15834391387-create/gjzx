package org.dromara.common.ocr.abstractd;



import org.dromara.common.ocr.core.IOcrStrategy;
import org.dromara.common.ocr.entity.IdentificationData;
import org.dromara.common.ocr.properties.OcrProperties;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.List;

/**
 * 票小秘
 *
 * @author lqm
 */
public abstract class AbstractOcrStrategy implements IOcrStrategy {

    protected OcrProperties properties;

    public boolean isInit = false;

    public void init(OcrProperties properties) {
        this.properties = properties;
    }

    @Override
    public abstract List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, byte[] bytes);
}
