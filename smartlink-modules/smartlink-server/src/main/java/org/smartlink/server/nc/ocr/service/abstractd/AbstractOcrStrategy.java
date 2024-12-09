package org.smartlink.server.nc.ocr.service.abstractd;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.properties.OcrProperties;
import org.smartlink.server.nc.ocr.service.IOcrStrategy;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;

import java.util.List;

/**
 * 合合（禹水），票小秘，税务云
 *
 * @author L
 */
public abstract class AbstractOcrStrategy implements IOcrStrategy {

    protected OcrProperties properties;

    public boolean isInit = false;

    public void init(OcrProperties properties) {
        this.properties = properties;
    }

    @Override
    public abstract List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, byte[] bytes) throws Exception;
}
