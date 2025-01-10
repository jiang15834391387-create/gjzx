package org.smartlink.common.ocr.abstractd;


import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.core.IdentificationFactory;
import org.smartlink.common.ocr.factory.OcrFactory;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

/**
 * <p>Title: ConversionFactory</p>
 * <p>将不同厂商识别转换工厂</p>
 * <p>Description: 获取ocr厂商</p>
 **/
public class ConversionFactory {

    public static ChangeIdentifyInfo getConversionFactory(DataImageFilesInfo dataImageFilesInfo, Object o) {
        ChangeIdentifyInfo changeIdentifyInfo = null;
        try {
            Class<?> c = OcrFactory.instanceObj();
            Object obj = c.newInstance();
            changeIdentifyInfo = ((IdentificationFactory) obj).conversionInfo(dataImageFilesInfo,o);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return changeIdentifyInfo;
    }

}
