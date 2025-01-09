package org.dromara.common.ocr.abstractd;


import org.dromara.common.ocr.core.ChangeIdentifyInfo;
import org.dromara.common.ocr.core.IdentificationFactory;
import org.dromara.common.ocr.factory.OcrFactory;
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
