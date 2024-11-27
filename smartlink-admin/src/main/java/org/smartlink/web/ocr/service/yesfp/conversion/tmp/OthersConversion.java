package org.smartlink.web.ocr.service.yesfp.conversion.tmp;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.ocr.service.ChangeIdentifyInfo;
import org.smartlink.web.ocr.service.bean.IdentificationData;
import org.springframework.core.convert.ConversionException;

/**
 * <p>Title: AircraftInvoiceConversion</p>
 * <p>
 * <p>Description:税务云其他类型识别信息转换为实体类 </p>
 *
 * @author L
 * @date
 **/
public class OthersConversion implements ChangeIdentifyInfo<JSONObject> {

    private static class LazyHolder {
        private static final OthersConversion INSTANCE = new OthersConversion();
    }
    public static OthersConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    private OthersConversion() {}


    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, JSONObject o) throws ConversionException {
        DataOcrInfo others=new DataOcrInfo();
        others.setId(IdUtil.simpleUUID());
        others.setFileId(dataImageFilesInfo.getFileId());
        return new IdentificationData<>(InvoiceConstants.IMAGE_OTHERS,others);
        }
    }
