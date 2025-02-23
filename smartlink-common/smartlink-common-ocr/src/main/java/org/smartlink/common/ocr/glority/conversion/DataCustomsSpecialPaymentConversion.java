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


//海关专用缴款书发票
public class DataCustomsSpecialPaymentConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final DataCustomsSpecialPaymentConversion INSTANCE = new DataCustomsSpecialPaymentConversion();
    }

    private DataCustomsSpecialPaymentConversion() {
    }

    public static DataCustomsSpecialPaymentConversion getInstance() {
        return DataCustomsSpecialPaymentConversion.LazyHolder.INSTANCE;
    }


    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataCustomsSpecialPayment invoice = new DataCustomsSpecialPayment();
            if (null == jsonObject) {
                return null;
            }
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());
            invoice.setAccount(jsonObject.getStr("account"));
            invoice.setAccountBank(jsonObject.getStr("account_bank"));
            invoice.setCompanyName(jsonObject.getStr("company_name"));
            invoice.setContractNumber(jsonObject.getStr("contract_number"));
            invoice.setCurrencyComment(jsonObject.getStr("currency_comment"));
            invoice.setCustomsName(jsonObject.getStr("customs_name"));
            invoice.setCustomsNumber(jsonObject.getStr("customs_number"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setDeliveryNumber(jsonObject.getStr("delivery_number"));
            invoice.setFillingCompany(jsonObject.getStr("filling_company"));
            invoice.setKind(jsonObject.getStr("kind"));
            invoice.setPaymentType(jsonObject.getStr("payment_type"));
            invoice.setRemarks(jsonObject.getStr("remarks"));
            invoice.setSeal(jsonObject.getStr("seal"));
            invoice.setNumber(jsonObject.getStr("number"));
            invoice.setPortCode(jsonObject.getStr("port_code"));
            invoice.setRevenueAgency(jsonObject.getStr("revenue_agency"));
            invoice.setSubject(jsonObject.getStr("subject"));
            invoice.setTaxExchangeRateComment(jsonObject.getStr("tax_exchange_rate_comment"));
            invoice.setInvoiceTotal(jsonObject.getStr("total"));
            invoice.setTotalWords(jsonObject.getStr("total_words"));
            invoice.setTransportationTools(jsonObject.getStr("transportation_tools"));
            invoice.setIncomeSystem(jsonObject.getStr("income_system"));
            invoice.setReceiptTreasury(jsonObject.getStr("receipt_treasury"));
            invoice.setBudgetLevel(jsonObject.getStr("budget_level"));
            invoice.setApplicationUnitNumber(jsonObject.getStr("application_unit_number"));
            invoice.setPaymentDeadline(jsonObject.getStr("payment_deadline"));
            invoice.setTitle(jsonObject.getStr("title"));





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
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE.getCode(), invoice, identifyResults.getExtra()));
        }
        return resultsList;
    }

}
