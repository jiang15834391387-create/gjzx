package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


//货物运输电子收款凭证发票
public class DataElectronicTransportationGoodsConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final DataElectronicTransportationGoodsConversion INSTANCE = new DataElectronicTransportationGoodsConversion();
    }

    private DataElectronicTransportationGoodsConversion() {
    }

    public static DataElectronicTransportationGoodsConversion getInstance() {
        return DataElectronicTransportationGoodsConversion.LazyHolder.INSTANCE;
    }


    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataElectronicTransportationGoods invoice = new DataElectronicTransportationGoods();
            if (null == jsonObject) {
                return null;
            }
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());
            invoice.setDate(jsonObject.getStr("date"));
            invoice.setBusinessLicenseNumber(jsonObject.getStr("business_license_number"));
            invoice.setElectronicReceiptNumber(jsonObject.getStr("electronic_receipt_number"));
            invoice.setProducer(jsonObject.getStr("producer"));
            invoice.setShipper(jsonObject.getStr("shipper"));
            invoice.setTotalPrice(jsonObject.getStr("total"));
            invoice.setTotalCn(jsonObject.getStr("total_cn"));
            invoice.setTransporter(jsonObject.getStr("transporter"));
            invoice.setTransporterIdNumber(jsonObject.getStr("transporter_id_number"));


            JSONArray list = jsonObject.getJSONArray("items");
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            if (ObjectUtil.isNotNull(list)) {
                for (Object invoiceDetails : list) {
                    DataOcrDetails e = new DataOcrDetails();
                    LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                    e.setId(IdUtil.fastSimpleUUID());
                    e.setFileId(dataImageFilesInfo.getFileId());
                    e.setPrice(fJson.containsKey("dutiable_price") ? fJson.get("dutiable_price") : null);
                    e.setName(fJson.containsKey("name_of_goods") ? fJson.get("name_of_goods") : null);
                    e.setDetailsCount(fJson.containsKey("quantity") ? fJson.get("quantity") : null);
                    e.setTax(fJson.containsKey("tax") ? fJson.get("tax") : null);
                    e.setTaxNumber(fJson.containsKey("tax_number") ? fJson.get("tax_number") : null);
                    e.setTaxRate(fJson.containsKey("tax_rate") ? fJson.get("tax_rate") : null);
                    e.setUnit(fJson.containsKey("unit") ? fJson.get("unit") : null);

                    ocrDetailsList.add(e);
                }
            }

            invoice.setDetails(ocrDetailsList);

            invoice.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                invoice.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                invoice.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE.getCode(), invoice, identifyResults.getExtra()));
        }
        return resultsList;
    }

}
