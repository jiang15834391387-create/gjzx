package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.math.BigDecimal;
import java.util.*;

/**
 * 增值税
 *
 **/
public class InvoiceConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final InvoiceConversion INSTANCE = new InvoiceConversion();
    }

    private InvoiceConversion() {
    }

    public static InvoiceConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataOcrInfo invoice = new DataOcrInfo();
            if (null == jsonObject) {
                return null;
            }
            invoice.setId(IdUtil.simpleUUID());
            invoice.setFileId(dataImageFilesInfo.getFileId());
            invoice.setInvoiceCode(jsonObject.getStr("code"));
            invoice.setInvoiceNumber(jsonObject.getStr("number"));
            invoice.setInvoiceDate(jsonObject.getStr("date"));
            invoice.setSumTax(jsonObject.getStr("tax"));
            String pretaxAmount = jsonObject.getStr("pretax_amount");
            if (StrUtil.isNotEmpty(pretaxAmount)) {
                invoice.setSumAmount(pretaxAmount);
            } else {
                invoice.setSumAmount(jsonObject.getStr("total"));
            }
            invoice.setPretaxAmount(jsonObject.getStr("pretax_amount"));
            invoice.setTotalLowercase(jsonObject.getStr("total"));
            invoice.setRightInvoiceNumber(jsonObject.getStr("number_confirm"));
            invoice.setRightInvoiceCode(jsonObject.getStr("code_confirm"));
            // 大写 价税合计
            invoice.setTotalUppercase(jsonObject.getStr("total"));
            invoice.setCheckCode(jsonObject.getStr("check_code"));
            invoice.setSellerName(jsonObject.getStr("seller"));
            invoice.setSellerNo(jsonObject.getStr("seller_tax_id"));
            invoice.setSellerAddress(jsonObject.getStr("seller_addr_tel"));
            invoice.setSellerAccount(jsonObject.getStr("seller_bank_account"));
            invoice.setBuyerName(jsonObject.getStr("buyer"));
            invoice.setBuyerNo(jsonObject.getStr("buyer_tax_id"));
            invoice.setBuyerAccount(jsonObject.getStr("buyer_bank_account"));
            invoice.setBuyerAddress(jsonObject.getStr("buyer_addr_tel"));
            String password = jsonObject.getStr("ciphertext");
            if (StringUtils.isNotEmpty(password)) {
                List<String> split = Arrays.asList(password.split(",").clone());
                if (split.size() > 3) {
                    invoice.setPassword1(split.get(0));
                }
            }
            invoice.setMachineCode(jsonObject.getStr("machine_code"));
            invoice.setPayee(jsonObject.getStr("receiptor"));
            invoice.setChecker(jsonObject.getStr("reviewer"));
            invoice.setIssuer(jsonObject.getStr("issuer"));
            invoice.setMachineCode(jsonObject.getStr("machine_code"));
            invoice.setCompanySeal(jsonObject.getStr("company_seal"));
            invoice.setSellCompanySeal(jsonObject.getStr("company_seal_mark"));
            invoice.setProvince(jsonObject.getStr("province"));
            invoice.setCity(jsonObject.getStr("city"));
            invoice.setInvoiceSheet(jsonObject.getStr("form_type"));
            invoice.setPageNumber(jsonObject.getStr("form_name"));
            invoice.setRemark(jsonObject.getStr("remark"));
            invoice.setItemNames(jsonObject.getStr("item_names"));
            invoice.setKind(jsonObject.getStr("kind"));
            //发票类型
            String invoiceCode = identifyResults.getType();
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                invoice.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                invoice.setRegion(null);
            }
            invoice.setOrientation(identifyResults.getOrientation());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            //发票类型判断
            if (InvoiceGlorityEnumd.GLORITY_TAX_SPECIAL_CODE.getCode().equals(invoiceCode) && jsonObject.getStr("electronic_mark").equals("1")) {
                //增值税电子专用发票
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRON_TAX_SPECIAL_CODE.getCode());
            } else if (InvoiceGlorityEnumd.GLORITY_ELECTRONIC_CODE.getCode().equals(invoiceCode) && jsonObject.getStr("block_chain").equals("1")) {
                //区块链电子发票
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRONIC_QUKUAILIAN_CODE.getCode());
            } else if (InvoiceGlorityEnumd.GLORITY_ELECTRONIC_CODE.getCode().equals(invoiceCode) && jsonObject.getStr("transit_mark").equals("1")) {
                //收费公路通行费增值税电子普通发票
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE.getCode());
            } else if (InvoiceGlorityEnumd.GLORITY_ROLL_TICKET_CODE.getCode().equals(invoiceCode)) {
                //增值税普通发票(卷票)
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_ROLL_TICKET_CODE.getCode());
            } else if (InvoiceGlorityEnumd.DIGITAL_INVOICE_LIST.getCode().equals(invoiceCode)) {
                //增值税发票清单
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.DIGITAL_INVOICE_LIST.getCode());
            } else if (InvoiceGlorityEnumd.GLORITY_TAX_SPECIAL_CODE.getCode().equals(invoiceCode)) {
                //增值税专用发票
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_TAX_SPECIAL_CODE.getCode());
            } else if (InvoiceGlorityEnumd.GLORITY_TAX_CODE.getCode().equals(invoiceCode)) {
                //增值税普通发票
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_TAX_CODE.getCode());
            } else if (InvoiceGlorityEnumd.GLORITY_AIRCRAFT_INVOICE_CODE.getCode().equals(invoiceCode)) {
                //机打发票
                dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_AIRCRAFT_INVOICE_CODE.getCode());
            }
            JSONArray list = jsonObject.getJSONArray("items");
            JSONArray list_transports = jsonObject.getJSONArray("transports");
            List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
            if (list != null && !list.isEmpty()) {
                for (Object invoiceDetails : list) {
                    DataOcrDetails e = new DataOcrDetails();
                    LinkedHashMap<String, String> fJson = ((cn.hutool.json.JSONObject) invoiceDetails).toBean(LinkedHashMap.class);
                    e.setId(IdUtil.fastSimpleUUID());
                    e.setFileId(dataImageFilesInfo.getFileId());
                    e.setName(fJson.containsKey("name") ? fJson.get("name") : null);
                    if (fJson.containsKey("goods_services")) {
                        e.setTax(fJson.get("goods_services"));
                    }
                    e.setStandard(fJson.containsKey("specification") ? fJson.get("specification") : null);
                    e.setUnit(fJson.containsKey("unit") ? fJson.get("unit") : null);
                    if (fJson.containsKey("uom")) {
                        e.setTax(fJson.get("uom"));
                    }
                    e.setPrice(fJson.containsKey("price") ? fJson.get("price") : null);
                    if (fJson.containsKey("unit_price")) {
                        e.setTax(fJson.get("unit_price"));
                    }
                    e.setDetailAmount(fJson.containsKey("total") ? fJson.get("total") : null);
                    e.setTaxRate(fJson.containsKey("tax_rate") ? fJson.get("tax_rate") : null);
                    e.setTax(fJson.containsKey("tax") ? fJson.get("tax") : null);
                    e.setDetailsCount(fJson.containsKey("quantity") ? fJson.get("quantity") : null);
                    e.setPlaceOfBuildingService(fJson.containsKey("placeOfBuildingService") ? fJson.get("placeOfBuildingService") : null);
                    e.setBuildingName(fJson.containsKey("building_name") ? fJson.get("building_name") : null);
                    e.setTitleCertificateNumber(fJson.containsKey("title_certificate_number") ? fJson.get("title_certificate_number") : null);
                    e.setAreaUnit(fJson.containsKey("area_unit") ? fJson.get("area_unit") : null);

                    e.setTax(fJson.containsKey("tax") ? fJson.get("tax") : null);
                    if (fJson.containsKey("tax_amount")) {
                        e.setTax(fJson.get("tax_amount"));
                    }
                    ocrDetailsList.add(e);
                }
            }
            if (list_transports != null && !list_transports.isEmpty()) {
                for (Object listTransports : list_transports) {
                    DataOcrDetails e = new DataOcrDetails();
                    LinkedHashMap<String, String> fJson = (LinkedHashMap) listTransports;
                    e.setId(IdUtil.fastSimpleUUID());
                    e.setFileId(dataImageFilesInfo.getFileId());
                    e.setTransportType(fJson.containsKey("transport_type") ? fJson.get("transport_type") : null);
                    e.setTransportNumber(fJson.containsKey("transport_number") ? fJson.get("transport_number") : null);
                    e.setFrom(fJson.containsKey("from") ? fJson.get("from") : null);
                    e.setTo(fJson.containsKey("to") ? fJson.get("to") : null);
                    e.setGoodsName(fJson.containsKey("goods_name") ? fJson.get("goods_name") : null);
                    e.setPassenger(fJson.containsKey("passenger") ? fJson.get("passenger") : null);
                    e.setTax(fJson.containsKey("user_id") ? fJson.get("user_id") : null);
                    e.setTravelDate(fJson.containsKey("date") ? fJson.get("date") : null);
                    e.setSeat(fJson.containsKey("seat") ? fJson.get("seat") : null);
                    ocrDetailsList.add(e);
                }
            }
            invoice.setDetails(ocrDetailsList);
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            resultsList.add(new IdentificationData<>(invoiceCode, invoice, identifyResults.getExtra()));
        }
        return resultsList;
    }

}
