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


//海关进口货物报关单发票
public class DataCustomsImxportGoodsConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final DataCustomsImxportGoodsConversion INSTANCE = new DataCustomsImxportGoodsConversion();
    }

    private DataCustomsImxportGoodsConversion() {
    }

    public static DataCustomsImxportGoodsConversion getInstance() {
        return DataCustomsImxportGoodsConversion.LazyHolder.INSTANCE;
    }


    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataCustomsImxportGoods invoice = new DataCustomsImxportGoods();
            if (null == jsonObject) {
                return null;
            }
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());
            invoice.setAdditionalExpress(jsonObject.getStr("additional_express"));
            invoice.setConfirmOfPayRoyalties(jsonObject.getStr("confirm_of_pay_royalties"));
            invoice.setConfirmOfSpecialRelationship(jsonObject.getStr("confirm_of_special_relationship"));
            invoice.setConsumptionCompanyCode(jsonObject.getStr("consumption_company_code"));
            invoice.setConsumptionCompanyName(jsonObject.getStr("consumption_company_name"));
            invoice.setContractNumber(jsonObject.getStr("contract_number"));
            invoice.setCustomsNumber(jsonObject.getStr("customs_number"));
            invoice.setDateOfApplication(jsonObject.getStr("date_of_application"));
            invoice.setDateOfImport(jsonObject.getStr("date_of_import"));
            invoice.setDeliveryNumber(jsonObject.getStr("delivery_number"));
            invoice.setDepartureCountryCode(jsonObject.getStr("departure_country_code"));
            invoice.setDepartureCountryName(jsonObject.getStr("departure_country_name"));
            invoice.setExecutiveCompanyCode(jsonObject.getStr("executive_company_code"));
            invoice.setExecutiveCompanyName(jsonObject.getStr("executive_company_name"));
            invoice.setFreight(jsonObject.getStr("freight"));
            invoice.setGrossWeight(jsonObject.getStr("gross_weight"));
            invoice.setInsurancePremium(jsonObject.getStr("insurance_premium"));
            invoice.setKind(jsonObject.getStr("kind"));
            invoice.setKindOfTaxCode(jsonObject.getStr("kind_of_tax_code"));
            invoice.setKindOfTaxName(jsonObject.getStr("kind_of_tax_name"));
            invoice.setMarksAndRemarks(jsonObject.getStr("marks_and_remarks"));
            invoice.setModeOfTradeCode(jsonObject.getStr("mode_of_trade_code"));
            invoice.setModeOfTradeName(jsonObject.getStr("mode_of_trade_name"));
            invoice.setModeOfTransportationCode(jsonObject.getStr("mode_of_transportation_code"));
            invoice.setModeOfTransportationName(jsonObject.getStr("mode_of_transportation_name"));
            invoice.setNetWeight(jsonObject.getStr("net_weight"));
            invoice.setNumberOfPackages(jsonObject.getStr("number_of_packages"));
            invoice.setOverseasConsigneeCode(jsonObject.getStr("overseas_consigner_code"));
            invoice.setOverseasConsigneeName(jsonObject.getStr("overseas_consigner_name"));
            invoice.setPortOfImportCode(jsonObject.getStr("port_of_import_code"));
            invoice.setPortOfImportName(jsonObject.getStr("port_of_import_name"));
            invoice.setPreRecordNumber(jsonObject.getStr("pre_record_number"));
            invoice.setTradeTermsCode(jsonObject.getStr("trade_terms_code"));
            invoice.setTradeTermsName(jsonObject.getStr("trade_terms_name"));
            invoice.setTradingCountryCode(jsonObject.getStr("trading_country_code"));
            invoice.setTradingCountryName(jsonObject.getStr("trading_country_name"));
            invoice.setTransportationTools(jsonObject.getStr("transportation_tools"));
            invoice.setAttachmentsAndNumbers(jsonObject.getStr("attachments_and_numbers"));
            invoice.setFilingEntity(jsonObject.getStr("filing_entity"));



            JSONArray list = jsonObject.getJSONArray("items");
            List<DataCustomsImportGoodsDetail> ocrDetailsList = new ArrayList<>();
            if (ObjectUtil.isNotNull(list)) {
                for (Object invoiceDetails : list) {
                    DataCustomsImportGoodsDetail e = new DataCustomsImportGoodsDetail();
                    LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                    e.setId(IdUtil.fastSimpleUUID());
                    e.setFileId(dataImageFilesInfo.getFileId());
                    e.setCommodityNumber(fJson.containsKey("commodity_number") ? fJson.get("commodity_number") : null);
                    e.setCurrency(fJson.containsKey("currency") ? fJson.get("currency") : null);
                    e.setDescriptionOfCommodity(fJson.containsKey("description_of_commodity") ? fJson.get("description_of_commodity") : null);
                    e.setDomesticDestinationPlace(fJson.containsKey("domestic_destination_place") ? fJson.get("domestic_destination_place") : null);
                    e.setFinalDestinationCountry(fJson.containsKey("final_destination_country") ? fJson.get("final_destination_country") : null);
                    e.setItemNumber(fJson.containsKey("item_number") ? fJson.get("item_number") : null);
                    e.setKindOfTax(fJson.containsKey("kind_of_tax") ? fJson.get("kind_of_tax") : null);
                    e.setOriginalCountry(fJson.containsKey("original_country") ? fJson.get("original_country") : null);
                    e.setQuantityOf2Uom(fJson.containsKey("quantity_of_2_uom") ? fJson.get("quantity_of_2_uom") : null);
                    e.setQuantityOfUom(fJson.containsKey("quantity_of_uom") ? fJson.get("quantity_of_uom") : null);
                    e.setSpecification(fJson.containsKey("specification") ? fJson.get("specification") : null);
                    e.setTotalPrice(fJson.containsKey("total_price") ? fJson.get("total_price") : null);
                    e.setTransactionUomAndQuantity(fJson.containsKey("transaction_uom_and_quantity") ? fJson.get("transaction_uom_and_quantity") : null);
                    e.setUnitPrice(fJson.containsKey("unit_price") ? fJson.get("unit_price") : null);
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
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.CUSTOMS_IMPORTED_GOODS_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.CUSTOMS_IMPORTED_GOODS_CODE.getCode(), invoice, identifyResults.getExtra(), identifyResults.getMessage()));
        }
        return resultsList;
    }

}
