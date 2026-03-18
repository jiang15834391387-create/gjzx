package org.smartlink.common.ocr.autoinv.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.ocr.constant.InvoiceConstants;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.ocr.core.ChangeIdentifyInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.exception.OcrException;
import org.smartlink.common.ocr.autoinv.response.AutoinvIdentifyResult;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * autoinv 机动车销售统一发票转换类
 */
public class AutoinvMotorVehicleSaleConversion implements ChangeIdentifyInfo<List<AutoinvIdentifyResult>> {

    private static class LazyHolder {
        private static final AutoinvMotorVehicleSaleConversion INSTANCE = new AutoinvMotorVehicleSaleConversion();
    }

    private AutoinvMotorVehicleSaleConversion() {}

    public static AutoinvMotorVehicleSaleConversion getInstance() {
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

            DataMotorVehicleSale motorVehicleSale = new DataMotorVehicleSale();
            motorVehicleSale.setId(IdUtil.simpleUUID());
            motorVehicleSale.setFileId(dataImageFilesInfo.getFileId());

            // 设置机动车发票基本信息
            motorVehicleSale.setInvoiceCode(jsonObject.getStr("invoice_code"));
            motorVehicleSale.setInvoiceNumber(jsonObject.getStr("invoice_no"));
            motorVehicleSale.setInvoiceDate(jsonObject.getStr("date"));
            motorVehicleSale.setInvoiceTotal(jsonObject.getStr("amount_little"));
            motorVehicleSale.setTotalCn(jsonObject.getStr("amount_big"));
            motorVehicleSale.setTax(jsonObject.getStr("tax_amount"));
            motorVehicleSale.setTaxRate(jsonObject.getStr("tax_rate"));

            // 设置销售方信息
            motorVehicleSale.setSeller(jsonObject.getStr("seller_name"));
            motorVehicleSale.setSellerTaxid(jsonObject.getStr("seller_tax_id"));
            motorVehicleSale.setSellerAddress(jsonObject.getStr("seller_address"));
            motorVehicleSale.setSellerPhone(jsonObject.getStr("seller_phone"));
            motorVehicleSale.setSellerBankName(jsonObject.getStr("seller_bank"));
            motorVehicleSale.setSellerBankAccount(jsonObject.getStr("seller_bank_account"));

            // 设置购买方信息
            motorVehicleSale.setBuyerName(jsonObject.getStr("buyer_name"));
            motorVehicleSale.setBuyerId(jsonObject.getStr("buyer_tax_id"));

            // 设置车辆信息
            motorVehicleSale.setCarModel(jsonObject.getStr("vehicle_model"));
            motorVehicleSale.setProduceArea(jsonObject.getStr("production_place"));
            motorVehicleSale.setCertificateNumber(jsonObject.getStr("certificate_no"));
            motorVehicleSale.setCertificateOfImport(jsonObject.getStr("import_no"));
            motorVehicleSale.setCommodityInspectionNo(jsonObject.getStr("inspection_no"));
            motorVehicleSale.setCarEngineCode(jsonObject.getStr("vehicle_engine_no"));
            motorVehicleSale.setCarCode(jsonObject.getStr("vehicle_vin"));
            motorVehicleSale.setVehicleType(jsonObject.getStr("vehicle_type"));
            motorVehicleSale.setTonnage(jsonObject.getStr("tonnage"));
            motorVehicleSale.setLimitedPeopleCount(jsonObject.getStr("limit_passengers"));

            // 设置其他信息
            motorVehicleSale.setMachineCode(jsonObject.getStr("machine_code"));
            motorVehicleSale.setMachineCode(jsonObject.getStr("print_code"));
            motorVehicleSale.setMachineNumber(jsonObject.getStr("print_no"));
            motorVehicleSale.setDrawer(jsonObject.getStr("issuer"));
            motorVehicleSale.setTaxCode(jsonObject.getStr("cipher_text"));
            motorVehicleSale.setTaxPaymentCertificateNo(jsonObject.getStr("tax_payment_receipt"));
            motorVehicleSale.setTaxAuthorities(jsonObject.getStr("tax_gov_name"));
            motorVehicleSale.setTaxAuthoritiesCode(jsonObject.getStr("tax_gov_no"));
            motorVehicleSale.setInvoiceSheet(jsonObject.getStr("sheet_type"));
            motorVehicleSale.setCompanySeal(jsonObject.getStr("seal"));

            // 设置数电票相关信息
            motorVehicleSale.setElectronicNumber(jsonObject.getStr("einvoice_no"));
            motorVehicleSale.setElectronicMark(jsonObject.getStr("einvoice_mark"));

            dataImageFilesInfo.setInvoice(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE);
            dataImageFilesInfo.setMessage(identifyResult.getStr("msg"));
            //发票待查验
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            // 添加到结果列表
            resultsList.add(new IdentificationData<>(InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE, motorVehicleSale, null, identifyResult.getStr("msg")));

        }
        return resultsList;
    }
}
