package org.smartlink.server.nc.ocr.service.nccbip.conversion;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.nccbip.response.NccBipOcrResponse;

/**
 * <p>Title: OthersConversion</p>
 * <p>
 * <p>Description:非识别转换为实体类 </p>
 *
 * @author L
 **/
public class NccBipOthersConversion implements ChangeIdentifyInfo<NccBipOcrResponse.OneDataDTO.DatasDTO> {

    private static class LazyHolder {

        private static final NccBipOthersConversion INSTANCE = new NccBipOthersConversion();
    }
    public static NccBipOthersConversion getInstance() {

        return LazyHolder.INSTANCE;
    }

    private NccBipOthersConversion() {}


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, NccBipOcrResponse.OneDataDTO.DatasDTO datasDTO) throws OcrException {
        return null;
    }

    @Override
    public NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO reverseConversion(BaseEntity baseEntity) {
        return new NccBipOcrResponse.OneDataDTO.DatasDTO.DataDTO();
    }
}
