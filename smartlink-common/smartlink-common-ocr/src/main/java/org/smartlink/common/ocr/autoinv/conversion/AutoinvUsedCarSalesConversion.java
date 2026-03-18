package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 二手车销售统一发票转换类
 */
public class AutoinvUsedCarSalesConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvUsedCarSalesConversion INSTANCE = new AutoinvUsedCarSalesConversion();
    }

    private AutoinvUsedCarSalesConversion() {}

    public static AutoinvUsedCarSalesConversion getInstance() {
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

            DataUsedCarSales usedCarSales = new DataUsedCarSales();
            usedCarSales.setId(IdUtil.simpleUUID());
            usedCarSales.setFileId(dataImageFilesInfo.getFileId());

            // 设置发票基本信息
            usedCarSales.setInvoiceCode(jsonObject.getStr("invoice_code"));
            usedCarSales.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            usedCarSales.setInvoiceDate(jsonObject.getStr("date"));
            usedCarSales.setInvoiceTotal(jsonObject.getStr("amount_little"));
            usedCarSales.setTotalUppercase(jsonObject.getStr("amount_big"));
            usedCarSales.setMachineCode(jsonObject.getStr("machine_code"));
            usedCarSales.setMachineNumber(jsonObject.getStr("print_no"));
            usedCarSales.setInvoiceSheet(jsonObject.getStr("sheet_type"));
            usedCarSales.setIssuer(jsonObject.getStr("issuer"));
            usedCarSales.setRemark(jsonObject.getStr("notes"));

            // 设置买卖双方信息
            usedCarSales.setBuyerName(jsonObject.getStr("buyer_name"));
            usedCarSales.setBuyerId(jsonObject.getStr("buyer_id"));
            usedCarSales.setBuyerAddress(jsonObject.getStr("buyer_address"));
            usedCarSales.setBuyerPhone(jsonObject.getStr("buyer_phone"));
            usedCarSales.setSellerName(jsonObject.getStr("seller_name"));
            usedCarSales.setSellerId(jsonObject.getStr("seller_id"));
            usedCarSales.setSellerAddress(jsonObject.getStr("seller_address"));
            usedCarSales.setSellerPhone(jsonObject.getStr("seller_phone"));

            // 设置车辆信息
            usedCarSales.setCarModel(jsonObject.getStr("vehicle_model"));
            usedCarSales.setVehicleType(jsonObject.getStr("vehicle_type"));
            usedCarSales.setCarCode(jsonObject.getStr("vehicle_vin"));
            usedCarSales.setLicensePlate(jsonObject.getStr("vehicle_plate_no"));
            usedCarSales.setRegisTrationNumber(jsonObject.getStr("register_no"));
            usedCarSales.setElectronicMark(jsonObject.getStr("einvoice_mark"));
            usedCarSales.setElectronicNumber(jsonObject.getStr("einvoice_no"));
            usedCarSales.setCompanyName(jsonObject.getStr("market_name"));
            usedCarSales.setCompanyTaxId(jsonObject.getStr("market_tax_id"));
            usedCarSales.setLemonMarketAddress(jsonObject.getStr("market_address"));
            usedCarSales.setLemonMarketBankAndCcount(jsonObject.getStr("market_bank_account"));
            usedCarSales.setLemonMarketPhone(jsonObject.getStr("market_phone"));
            usedCarSales.setBusinessUnit(jsonObject.getStr("auction_company"));
            usedCarSales.setBusinessUnitAddress(jsonObject.getStr("auction_company_address"));
            usedCarSales.setBusinessUnitTaxNo(jsonObject.getStr("auction_company_tax_id"));
            usedCarSales.setSellerAccount(jsonObject.getStr("auction_company_bank_account"));
            usedCarSales.setBusinessUnitPhone(jsonObject.getStr("auction_company_phone"));


            dataImageFilesInfo.setInvoice(InvoiceConstants.GLORITY_USED_CAR_SALES_CODE);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_USED_CAR_SALES_CODE, usedCarSales, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
