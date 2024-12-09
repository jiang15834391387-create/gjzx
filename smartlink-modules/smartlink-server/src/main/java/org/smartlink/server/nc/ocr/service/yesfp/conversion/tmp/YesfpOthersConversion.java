package org.smartlink.server.nc.ocr.service.yesfp.conversion.tmp;


import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.bean.Others;
import org.smartlink.server.nc.ocr.service.yesfp.reponse.YesfpResultData;

/**
 * <p>Title: OthersConversion</p>
 * <p>
 * <p>Description:非识别转换为实体类 </p>
 *
 * @author L
 **/
public class YesfpOthersConversion implements ChangeIdentifyInfo<YesfpResultData> {

    private static class LazyHolder {

        private static final YesfpOthersConversion INSTANCE = new YesfpOthersConversion();
    }
    public static YesfpOthersConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    private YesfpOthersConversion() {}


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, YesfpResultData o) throws OcrException {
        Others others=new Others();
        dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
        others.setFileId(dataImageFilesInfo.getFileId());
        return new IdentificationData<>(dataImageFilesInfo.getFileType(),others);
    }
}
