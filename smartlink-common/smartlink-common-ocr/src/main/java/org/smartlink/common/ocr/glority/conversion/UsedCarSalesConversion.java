package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 二手车销售
 *
 **/
public class UsedCarSalesConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final UsedCarSalesConversion INSTANCE = new UsedCarSalesConversion();
    }

    private UsedCarSalesConversion() {
    }

    public static UsedCarSalesConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataUsedCarSales dataUsedCarSales = new DataUsedCarSales();
            if (null == jsonObject) {
                return null;
            }
            dataUsedCarSales.setId(IdUtil.simpleUUID());
            dataUsedCarSales.setFileId(dataImageFilesInfo.getFileId());
            dataUsedCarSales.setInvoiceNumber(jsonObject.getStr("number"));
            dataUsedCarSales.setInvoiceCode(jsonObject.getStr("code"));
            dataUsedCarSales.setInvoiceDate(jsonObject.getStr("date"));
            dataUsedCarSales.setInvoiceTotal(jsonObject.getStr("total"));
            dataUsedCarSales.setSellerName(jsonObject.getStr("seller"));
            dataUsedCarSales.setRegisTrationNumber(jsonObject.getStr("seller"));
            dataUsedCarSales.setSellerName(jsonObject.getStr("seller"));
            dataUsedCarSales.setSellerName(jsonObject.getStr("registration_number"));
            dataUsedCarSales.setVehicleType(jsonObject.getStr("car_type"));
            dataUsedCarSales.setBuyerId(jsonObject.getStr("buyer_id"));
            dataUsedCarSales.setSellerId(jsonObject.getStr("seller_id"));
            dataUsedCarSales.setCarModel(jsonObject.getStr("car_model"));
            dataUsedCarSales.setBuyerAddress(jsonObject.getStr("buyer_address"));
            dataUsedCarSales.setSellerAddress(jsonObject.getStr("seller_address"));
            dataUsedCarSales.setBuyerName(jsonObject.getStr("buyer"));
            dataUsedCarSales.setSellerPhone(jsonObject.getStr("seller_tel"));
            dataUsedCarSales.setLicensePlate(jsonObject.getStr("license_plate"));
            dataUsedCarSales.setBuyerPhone(jsonObject.getStr("buyer_tel"));
            dataUsedCarSales.setCompanyName(jsonObject.getStr("company_name"));
            dataUsedCarSales.setCarCode(jsonObject.getStr("car_code"));
            dataUsedCarSales.setCarModel(jsonObject.getStr("car_model"));
            dataUsedCarSales.setCompanySeal(jsonObject.getStr("company_seal"));
            dataUsedCarSales.setMachineCode(jsonObject.getStr("machine_id"));
            dataUsedCarSales.setInvoiceSheet(jsonObject.getStr("form_type"));
            dataUsedCarSales.setPageNumber(jsonObject.getStr("form_name"));
            dataUsedCarSales.setPageNumber(jsonObject.getStr("kind"));
            dataUsedCarSales.setTitle(jsonObject.getStr("title"));
            dataUsedCarSales.setMachineNumber(jsonObject.getStr("machine_number"));
            dataUsedCarSales.setMachineCode(jsonObject.getStr("machine_code"));
            dataUsedCarSales.setIssuer(jsonObject.getStr("issuer"));
            dataUsedCarSales.setDestinationDepartmentOfMotorVehicles(jsonObject.getStr("destination_department_of_motor_vehicles"));
            dataUsedCarSales.setElectronicNumber(jsonObject.getStr("electronic_number"));
            dataUsedCarSales.setElectronicMark(jsonObject.getStr("electronic_mark"));
            dataUsedCarSales.setProvince(jsonObject.getStr("province"));
            dataUsedCarSales.setCity(jsonObject.getStr("city"));
            dataUsedCarSales.setBusinessUnitAddress(jsonObject.getStr("auction_address"));
            dataUsedCarSales.setBusinessUnit(jsonObject.getStr("auction_company"));
            dataUsedCarSales.setBusinessUnit(jsonObject.getStr("auction_company"));
            dataUsedCarSales.setSellerAccount(jsonObject.getStr("auction_bank_account"));
            dataUsedCarSales.setBusinessUnitPhone(jsonObject.getStr("auction_phone"));

            dataUsedCarSales.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                dataUsedCarSales.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                dataUsedCarSales.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_USED_CAR_SALES_CODE.getCode());
            dataImageFilesInfo.setMessage(identifyResults.getMessage());
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_USED_CAR_SALES_CODE.getCode(), dataUsedCarSales, identifyResults.getExtra(), identifyResults.getMessage()));

        }
        return resultsList;
    }
}
