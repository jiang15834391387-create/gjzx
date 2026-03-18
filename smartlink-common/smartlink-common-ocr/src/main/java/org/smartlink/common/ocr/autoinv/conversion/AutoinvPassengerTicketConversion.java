package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataPassengerCar;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 客运票转换类
 */
public class AutoinvPassengerTicketConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvPassengerTicketConversion INSTANCE = new AutoinvPassengerTicketConversion();
    }

    private AutoinvPassengerTicketConversion() {}

    public static AutoinvPassengerTicketConversion getInstance() {
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

            DataPassengerCar passengerCar = new DataPassengerCar();
            passengerCar.setId(IdUtil.simpleUUID());
            passengerCar.setFileId(dataImageFilesInfo.getFileId());

            // 设置发票基本信息
            passengerCar.setInvoiceCode(jsonObject.getStr("invoice_code"));
            passengerCar.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            passengerCar.setInvoiceDate(jsonObject.getStr("date"));
            passengerCar.setInvoiceTime(jsonObject.getStr("time"));
            passengerCar.setInvoiceTotal(jsonObject.getStr("amount_little"));

            // 设置乘客信息
            passengerCar.setName(jsonObject.getStr("name"));
            passengerCar.setUserId(jsonObject.getStr("id_card_no"));

            // 设置行程信息
            passengerCar.setStationGeton(jsonObject.getStr("station_from"));
            passengerCar.setStationGetoff(jsonObject.getStr("station_to"));

            // 设置其他信息
            passengerCar.setCompanySeal(jsonObject.getStr("company_seal"));

            dataImageFilesInfo.setInvoice(InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE, passengerCar, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
