package org.smartlink.common.ocr.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.glority.response.IdentifyResults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 机动车销售发票
 *
 * @author maxuhui
 **/
public class MotorVehicleSaleConversion implements ChangeIdentifyInfo<List<IdentifyResults>> {

    private static class LazyHolder {

        private static final MotorVehicleSaleConversion INSTANCE = new MotorVehicleSaleConversion();
    }

    private MotorVehicleSaleConversion() {
    }

    public static MotorVehicleSaleConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public List<IdentificationData> changeInfo(DataImageFilesInfo dataImageFilesInfo, List<IdentifyResults> identifyResult) throws OcrException {
        List<IdentificationData> resultsList = new ArrayList<>();
        for (IdentifyResults identifyResults : identifyResult) {

            JSONObject jsonObject = identifyResults.getDetails();
            DataMotorVehicleSale motorVehicleSale = new DataMotorVehicleSale();
            if (null == jsonObject) {
                return null;
            }
            motorVehicleSale.setId(IdUtil.simpleUUID());
            motorVehicleSale.setFileId(dataImageFilesInfo.getFileId());
            motorVehicleSale.setInvoiceDate(jsonObject.getStr("date"));
            motorVehicleSale.setTitle(jsonObject.getStr("title"));
            motorVehicleSale.setVehicleType(jsonObject.getStr("car_type"));
            motorVehicleSale.setCommodityInspectionNo(jsonObject.getStr("commodity_number"));
            motorVehicleSale.setElectronicNumber(jsonObject.getStr("electronicNumber"));
            motorVehicleSale.setTaxPaymentCertificateNo(jsonObject.getStr("tax_num"));
            motorVehicleSale.setSeller(jsonObject.getStr("seller"));
            motorVehicleSale.setInvoiceCode(jsonObject.getStr("code"));
            motorVehicleSale.setBuyerId(jsonObject.getStr("buyer_id"));
            motorVehicleSale.setCertificateNumber(jsonObject.getStr("certificate_number"));
            motorVehicleSale.setTaxRate(jsonObject.getStr("tax_rate"));
            motorVehicleSale.setSellerTaxid(jsonObject.getStr("seller_tax_id"));
            motorVehicleSale.setInvoiceNumber(jsonObject.getStr("number"));
            motorVehicleSale.setTaxAuthorities(jsonObject.getStr("tax_authorities"));
            motorVehicleSale.setInvoiceTotal(jsonObject.getStr("total"));
            motorVehicleSale.setProvince(jsonObject.getStr("province"));
            motorVehicleSale.setMachineNumber(jsonObject.getStr("machine_number"));
            motorVehicleSale.setCarEngineCode(jsonObject.getStr("car_engine_code"));
            motorVehicleSale.setPreTaxAmount(jsonObject.getStr("pretax_amount"));
            motorVehicleSale.setTonnage(jsonObject.getStr("tonnage"));
            motorVehicleSale.setSellerBankAccount(jsonObject.getStr("seller_bank_account"));
            motorVehicleSale.setSellerAddress(jsonObject.getStr("address"));
            motorVehicleSale.setTax(jsonObject.getStr("tax"));
            motorVehicleSale.setMachineCode(jsonObject.getStr("machine_code"));
            motorVehicleSale.setBuyerName(jsonObject.getStr("buyer"));
            motorVehicleSale.setTaxAuthoritiesCode(jsonObject.getStr("tax_authorities_code"));
            motorVehicleSale.setSellerPhone(jsonObject.getStr("phone"));
            motorVehicleSale.setCarCode(jsonObject.getStr("car_code"));
            motorVehicleSale.setCarModel(jsonObject.getStr("car_model"));
            motorVehicleSale.setCompanySeal(jsonObject.getStr("company_seal"));
            motorVehicleSale.setInvoiceSheet(jsonObject.getStr("form_type"));
            motorVehicleSale.setPageNumber(jsonObject.getStr("form_name"));
            motorVehicleSale.setPreTaxAmount(jsonObject.getStr("pretax_amount"));



            motorVehicleSale.setOrientation(identifyResults.getOrientation());
            if (identifyResults.getRegion() != null && identifyResults.getRegion().length > 0) {
                motorVehicleSale.setRegion(String.join(",", identifyResults.getRegion()));
            } else {
                motorVehicleSale.setRegion(null);
            }

            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            dataImageFilesInfo.setInvoice(InvoiceGlorityEnumd.GLORITY_MOTOR_VEHICLE_SALE_CODE.getCode());
            dataImageFilesInfo.setMessage(jsonObject.getStr("message"));
            resultsList.add(new IdentificationData<>(InvoiceGlorityEnumd.GLORITY_MOTOR_VEHICLE_SALE_CODE.getCode(), motorVehicleSale, identifyResults.getExtra()));

        }

        return resultsList;
    }

}
