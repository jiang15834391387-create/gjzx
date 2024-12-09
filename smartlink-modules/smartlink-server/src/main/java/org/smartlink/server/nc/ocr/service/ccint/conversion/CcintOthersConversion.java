package org.smartlink.server.nc.ocr.service.ccint.conversion;


import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.bean.Others;
import org.smartlink.server.nc.ocr.service.ccint.response.ObjectListBean;

/**
 * <p>Title: OthersConversion</p>
 * <p>
 * <p>Description:非识别转换为实体类 </p>
 *
 * @author L
 **/
public class CcintOthersConversion implements ChangeIdentifyInfo<ObjectListBean> {

    private static class LazyHolder {

        private static final CcintOthersConversion INSTANCE = new CcintOthersConversion();
    }
    public static CcintOthersConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    private CcintOthersConversion() {}


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, ObjectListBean o) throws OcrException {
        Others others=new Others();
        dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
        others.setFileId(dataImageFilesInfo.getFileId());
        //base64
        others.setOption("base64");
        others.setBase64(o.getImage());
        return new IdentificationData<>(dataImageFilesInfo.getFileType(),others);
    }
}
