package org.smartlink.server.nc.ocr.service.abstractd;


import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.IdentificationFactory;
import org.smartlink.server.nc.ocr.service.factory.OcrFactory;
import org.smartlink.server.nc.utils.ExceptionUtil;

/**
 * <p>Title: ConversionFactory</p>
 * <p>将不同厂商识别转换工厂</p>
 * <p>Description: 获取ocr厂商</p>
 **/
@Slf4j
public class ConversionFactory {

    public static ChangeIdentifyInfo getConversionFactory(DataImageFilesInfo dataImageFilesInfo, Object o) {
        ChangeIdentifyInfo changeIdentifyInfo = null;
        try {
            Class<?> c = OcrFactory.instanceObj();
            Object obj = c.newInstance();
            IdentificationFactory obj1 = (IdentificationFactory) obj;
            changeIdentifyInfo = obj1.conversionInfo(dataImageFilesInfo,o);
        } catch (Exception e) {
            log.error("转换工厂出现异常："+ ExceptionUtil.getExceptionMessage(e));
        }
        return changeIdentifyInfo;
    }

}
