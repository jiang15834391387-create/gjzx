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
            /**
             * {"date":"2022年01月27日","seller":"常山金淼供应链管理有限公司","code":"133002120269",
             * "company_seal":"1","city":"","origin":"山东省","form_type":"第二联","buyer_id":"91330822MA7D2PLX4T",
             * "certificate_number":"YG186ZNA2239516","tax_rate":"13%","issuer":"张家保","seller_tax_id":"91330822MA2DJ6TC5G",
             * "car_type":"牵引汽车","number":"01304916","tax_authorities":"国家税务总局常山县税务局经济技术开发区税务所",
             * "total":"343000.00","tax_num":"","province":"浙江省","machine_number":"01304916",
             * "car_engine_code":"220117005867","pretax_amount":"303539.82","tonnage":"-",
             * "seller_bank_account":"中国银行常山定阳支行","car_model":"汕德卡牌ZZ4186W361HF1B",
             * "commodity_number":"无","address":"浙江省衢州市常山县金川街道云耕大道1号1036","kind":"用车","tax":"39460.18",
             * "max_people_num":"2","machine_code":"133002120269","buyer":"常山速森供应链管理有限公司",
             * "tax_authorities_code":"13308223100","phone":"18930891567","car_code":"LZZ7CCXD7NC436818",
             * "form_name":"发票联","account":"377977991739"}
             */
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
