package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 火车票转换类
 */
public class AutoinvRailwayTicketConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvRailwayTicketConversion INSTANCE = new AutoinvRailwayTicketConversion();
    }

    private AutoinvRailwayTicketConversion() {}

    public static AutoinvRailwayTicketConversion getInstance() {
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

            DataRailwayTicket railwayTicket = new DataRailwayTicket();
            railwayTicket.setId(IdUtil.simpleUUID());
            railwayTicket.setFileId(dataImageFilesInfo.getFileId());

            // 设置火车票基本信息
            railwayTicket.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            railwayTicket.setInvoiceDate(jsonObject.getStr("date"));
            railwayTicket.setInvoiceTotal(jsonObject.getStr("amount_little"));
            railwayTicket.setName(jsonObject.getStr("id_name"));
            railwayTicket.setTrainNumber(jsonObject.getStr("train_no"));
            railwayTicket.setSeatNum(jsonObject.getStr("seat"));
            railwayTicket.setSeat(jsonObject.getStr("seat_class"));
            railwayTicket.setStationGetOn(jsonObject.getStr("station_from"));
            railwayTicket.setStationGetOff(jsonObject.getStr("station_to"));
            railwayTicket.setInvoiceDate(jsonObject.getStr("date_time"));
            railwayTicket.setTicketContent(jsonObject.getStr("issuing_no"));
            railwayTicket.setElectronicMark(jsonObject.getStr("einvoice_mark"));
//            railwayTicket.setPurchaseType(jsonObject.getStr("purchase_type"));
//            railwayTicket.setIssuingStation(jsonObject.getStr("issuing_station"));
//            railwayTicket.setTicketType(jsonObject.getStr("ticket_type"));

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE, railwayTicket, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
