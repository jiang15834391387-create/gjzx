package org.smartlink.common.ocr.abstractd;



import org.smartlink.common.ocr.core.IOcrStrategy;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.properties.OcrProperties;
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
    public abstract List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64);
}
