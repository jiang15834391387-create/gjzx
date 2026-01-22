package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;

import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataQuotaInvoice;
import org.smartlink.common.entity.domain.business.domain.DataTaxiTickets;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 出租车票
 *
 **/
public class TaxiTicketsConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final TaxiTicketsConversion INSTANCE = new TaxiTicketsConversion();
    }

    private TaxiTicketsConversion() {
    }

    public static TaxiTicketsConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataTaxiTickets taxiTickets = new DataTaxiTickets();
            if (null == jsonObject) {
                return null;
            }
            taxiTickets.setId(IdUtil.simpleUUID());
            taxiTickets.setFileId(dataImageFilesInfo.getFileId());
            taxiTickets.setInvoiceNumber(jsonObject.getStr("number"));
            taxiTickets.setInvoiceCode(jsonObject.getStr("code"));
            taxiTickets.setInvoiceTotal(jsonObject.getStr("total"));
            taxiTickets.setInvoiceDate(jsonObject.getStr("date"));
            taxiTickets.setLicensePlate(jsonObject.getStr("license_plate"));
            taxiTickets.setTimeGetOn(jsonObject.getStr("time_geton"));
            taxiTickets.setTimeGetOff(jsonObject.getStr("time_getoff"));
            taxiTickets.setMileage(jsonObject.getStr("mileage"));
            taxiTickets.setPlace(jsonObject.getStr("place"));
            taxiTickets.setProvince(jsonObject.getStr("province"));
            taxiTickets.setCity(jsonObject.getStr("city"));
            taxiTickets.setKind(jsonObject.getStr("kind"));
            taxiTickets.setFuelSurcharge(jsonObject.getStr("surcharge"));
            taxiTickets.setFuelSurcharge(jsonObject.getStr("fare"));

            taxiTickets.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                taxiTickets.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                taxiTickets.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_TAXI_TICKETS_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_TAXI_TICKETS_CODE.getCode(), taxiTickets, identifyResults.getExtra(), identifyResults.getMessage()));

        }
        return resultsList;
    }

}
