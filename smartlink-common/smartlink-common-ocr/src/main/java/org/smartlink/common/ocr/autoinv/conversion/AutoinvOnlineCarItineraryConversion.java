package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataDidiItinerary;
import org.smartlink.common.entity.domain.business.domain.DataDidiItineraryDetails;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 网约车行程单转换类
 */
public class AutoinvOnlineCarItineraryConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvOnlineCarItineraryConversion INSTANCE = new AutoinvOnlineCarItineraryConversion();
    }

    private AutoinvOnlineCarItineraryConversion() {}

    public static AutoinvOnlineCarItineraryConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<AutoinvIdentifyResult> identifyResults) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (AutoinvIdentifyResult identifyResult : identifyResults) {
            JSONObject jsonObject = identifyResult;
            if (jsonObject == null) {
                continue;
            }

            DataDidiItinerary didiItinerary = new DataDidiItinerary();
            didiItinerary.setId(IdUtil.simpleUUID());
            didiItinerary.setFileId(dataImageFilesInfo.getFileId());

            // 设置网约车行程单基本信息
            didiItinerary.setTitle(jsonObject.getStr("title"));
            didiItinerary.setInvoiceDate(jsonObject.getStr("date"));
            didiItinerary.setTimeGetOn(jsonObject.getStr("start_time"));
            didiItinerary.setTimeGetOff(jsonObject.getStr("end_time"));
            didiItinerary.setInvoiceTotal(jsonObject.getStr("amount_little"));
            didiItinerary.setPhone(jsonObject.getStr("passenger_phone"));

            // 创建行程明细
            List<DataDidiItineraryDetails> detailsList = new ArrayList<>();
            DataDidiItineraryDetails detail = new DataDidiItineraryDetails();
            detail.setId(IdUtil.simpleUUID());
            detail.setFileId(dataImageFilesInfo.getFileId());
            detail.setTitle(jsonObject.getStr("title"));
            detail.setStationGetOn(jsonObject.getStr("start_address"));
            detail.setStationGetOff(jsonObject.getStr("end_address"));
            detail.setMileage(jsonObject.getStr("mileage"));
            detail.setTimeOrder(jsonObject.getStr("order_time"));
            detail.setTimeGetOn(jsonObject.getStr("start_time"));
            detail.setTimeGetOff(jsonObject.getStr("end_time"));
            detail.setCarType(jsonObject.getStr("car_type"));
            detail.setProducer(jsonObject.getStr("service_provider"));
            detail.setInvoiceTotal(jsonObject.getStr("amount_little"));
            detail.setCity(jsonObject.getStr("city"));

            detailsList.add(detail);
            didiItinerary.setDetails(detailsList);

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE, didiItinerary, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
