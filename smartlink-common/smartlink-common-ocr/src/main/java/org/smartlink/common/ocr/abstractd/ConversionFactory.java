package org.smartlink.common.ocr.abstractd;


import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.factory.OcrFactory;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.List;

/**
 * <p>Title: ConversionFactory</p>
 * <p>将不同厂商识别转换工厂</p>
 * <p>Description: 获取ocr厂商</p>
 **/
public class ConversionFactory {

    public static ChangeIdentifyInfo getConversionFactory(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> o) {
        ChangeIdentifyInfo changeIdentifyInfo = null;
        try {
            Class<?> c = OcrFactory.instanceObj();
            Object obj = c.newInstance();
//            if (obj instanceof IdentificationFactory) {
                changeIdentifyInfo = ((IdentificationFactory) obj).conversionInfo(dataImageFilesInfo,o);
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return changeIdentifyInfo;
    }

}
