package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;

import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataTaxiTickets;
import org.smartlink.common.entity.domain.business.domain.DataTollRoads;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 过路费
 *
 * @author maxuhui
 **/
public class TollRoadsConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final TollRoadsConversion INSTANCE = new TollRoadsConversion();
    }

    private TollRoadsConversion() {
    }

    public static TollRoadsConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataTollRoads tollRoads = new DataTollRoads();
            if (null == jsonObject) {
                return null;
            }
            tollRoads.setId(IdUtil.simpleUUID());
            tollRoads.setFileId(dataImageFilesInfo.getFileId());
            tollRoads.setInvoiceNumber(jsonObject.getStr("number"));
            tollRoads.setInvoiceCode(jsonObject.getStr("code"));
            tollRoads.setInvoiceTotal(jsonObject.getStr("total"));
            tollRoads.setInvoiceTime(jsonObject.getStr("time"));
            tollRoads.setHighwayFlag(jsonObject.getStr("highway_flag"));
            tollRoads.setInvoiceDate(jsonObject.getStr("date"));
            tollRoads.setEntrance(jsonObject.getStr("entrance"));
            tollRoads.setExit(jsonObject.getStr("exit"));
            tollRoads.setKind(jsonObject.getStr("kind"));
            tollRoads.setHighwayFlag(jsonObject.getStr("highway_flag"));
            tollRoads.setTitle(jsonObject.getStr("title"));

            tollRoads.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                tollRoads.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                tollRoads.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_TOLL_ROADS_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_TOLL_ROADS_CODE.getCode(), tollRoads, identifyResults.getExtra(), identifyResults.getMessage()));

        }
        return resultsList;
    }
}
