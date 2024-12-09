package org.smartlink.server.nc.ocr.service.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataSteamerTicket;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;

import java.util.Date;

/**
 * 船票
 *
 * @author L
 **/
public class SteamerTicketConversion implements ChangeIdentifyInfo<IdentifyResults> {


    private static class LazyHolder {
        private static final SteamerTicketConversion INSTANCE = new SteamerTicketConversion();
    }

    private SteamerTicketConversion() {
    }

    public static SteamerTicketConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        JSONObject jsonObject = identifyResults.getDetails();
        DataSteamerTicket steamerTicket = JSONObject.parseObject(jsonObject.toJSONString(), DataSteamerTicket.class);
        if (null == steamerTicket) {
            return null;
        }
        steamerTicket.setId(IdUtil.simpleUUID());
        steamerTicket.setFileId(dataImageFilesInfo.getFileId());
        dataImageFilesInfo.setFileType(InvoiceConstants.STEAMER_TICKET);
        steamerTicket.setInvoiceCode(jsonObject.getString("code"));
        steamerTicket.setInvoiceNumber(jsonObject.getString("number"));
        steamerTicket.setInvoiceDate(Convert.toDate(jsonObject.getString("date"), new Date()));
        steamerTicket.setInvoiceTime(jsonObject.getString("time"));
        steamerTicket.setStationGetOn(jsonObject.getString("station_geton"));
        steamerTicket.setStationGetOff(jsonObject.getString("station_getoff"));
        steamerTicket.setCurrencyCode(jsonObject.getString("currency_code"));
        steamerTicket.setInvoiceTotal(jsonObject.getBigDecimal("total"));
        steamerTicket.setName(jsonObject.getString("name"));
        steamerTicket.setCity(jsonObject.getString("city"));
        steamerTicket.setProvince(jsonObject.getString("province"));
        steamerTicket.setCoordinate(identifyResults.getRegion());
        steamerTicket.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), steamerTicket);

    }
}
