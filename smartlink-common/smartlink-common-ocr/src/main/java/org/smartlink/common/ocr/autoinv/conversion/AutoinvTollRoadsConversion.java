package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataTollRoads;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 过路费发票转换类
 */
public class AutoinvTollRoadsConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvTollRoadsConversion INSTANCE = new AutoinvTollRoadsConversion();
    }

    private AutoinvTollRoadsConversion() {}

    public static AutoinvTollRoadsConversion getInstance() {
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

            DataTollRoads tollRoads = new DataTollRoads();
            tollRoads.setId(IdUtil.simpleUUID());
            tollRoads.setFileId(dataImageFilesInfo.getFileId());

            // 设置发票基本信息
            tollRoads.setInvoiceCode(jsonObject.getStr("invoice_code"));
            tollRoads.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            tollRoads.setInvoiceDate(jsonObject.getStr("date"));
            tollRoads.setInvoiceTotal(jsonObject.getStr("amount_little"));

            // 设置通行信息
            tollRoads.setEntrance(jsonObject.getStr("entrance"));
            tollRoads.setExit(jsonObject.getStr("exit"));
            tollRoads.setTitle(jsonObject.getStr("title"));

            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_TOLL_ROADS_CODE, tollRoads, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
