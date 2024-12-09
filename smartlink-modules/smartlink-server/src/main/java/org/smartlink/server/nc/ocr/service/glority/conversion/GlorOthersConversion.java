package org.smartlink.server.nc.ocr.service.glority.conversion;


import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.bean.Others;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;

/**
 * <p>Title: OthersConversion</p>
 * <p>
 * <p>Description:非识别转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class GlorOthersConversion implements ChangeIdentifyInfo<IdentifyResults> {

    private static class LazyHolder {

        private static final GlorOthersConversion INSTANCE = new GlorOthersConversion();
    }
    public static GlorOthersConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    private GlorOthersConversion() {}


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        Others others=new Others();
        dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
        others.setFileId(dataImageFilesInfo.getFileId());
        others.setCoordinate(identifyResults.getRegion());
        others.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(),others);
    }
}
