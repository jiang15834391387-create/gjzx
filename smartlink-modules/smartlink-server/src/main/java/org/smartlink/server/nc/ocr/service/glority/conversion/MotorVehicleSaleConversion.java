package org.smartlink.server.nc.ocr.service.glority.conversion;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataMotorVehicleSale;
import org.smartlink.server.nc.ocr.exception.OcrException;
import org.smartlink.server.nc.ocr.service.ChangeIdentifyInfo;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.glority.response.IdentifyResults;

import java.util.Date;

/**
 * 机动车销售发票
 *
 * @author L
 **/
public class MotorVehicleSaleConversion implements ChangeIdentifyInfo<IdentifyResults> {

    private static class LazyHolder {

        private static final MotorVehicleSaleConversion INSTANCE = new MotorVehicleSaleConversion();
    }

    private MotorVehicleSaleConversion() {
    }

    public static MotorVehicleSaleConversion getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public IdentificationData changeInfo(DataImageFilesInfo dataImageFilesInfo, IdentifyResults identifyResults) throws OcrException {
        JSONObject jsonObject = identifyResults.getDetails();
        jsonObject.toString();
        DataMotorVehicleSale motorVehicleSale = JSONObject.parseObject(jsonObject.toJSONString(), DataMotorVehicleSale.class);
        if (null == motorVehicleSale) {
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
        dataImageFilesInfo.setFileType(InvoiceConstants.MOTOR_VEHICLE_SALE);
        motorVehicleSale.setInvoiceDate(Convert.toDate(jsonObject.getString("date"), new Date()));
        motorVehicleSale.setSeller(jsonObject.getString("seller"));
        motorVehicleSale.setInvoiceCode(jsonObject.getString("code"));
        motorVehicleSale.setBuyerId(jsonObject.getString("buyer_id"));
        motorVehicleSale.setCertificateNumber(jsonObject.getString("certificate_number"));
        motorVehicleSale.setTaxRate(jsonObject.getString("tax_rate"));
        motorVehicleSale.setSellerTaxid(jsonObject.getString("seller_tax_id"));
        motorVehicleSale.setInvoiceNumber(jsonObject.getString("number"));
        motorVehicleSale.setTaxAuthorities(jsonObject.getString("tax_authorities"));
        motorVehicleSale.setInvoiceTotal(jsonObject.getBigDecimal("total"));
        motorVehicleSale.setProvince(jsonObject.getString("province"));
        motorVehicleSale.setMachineNumber(jsonObject.getString("machine_number"));
        motorVehicleSale.setCarEngineCode(jsonObject.getString("car_engine_code"));
        motorVehicleSale.setPreTaxAmount(jsonObject.getBigDecimal("pretax_amount"));
        motorVehicleSale.setTonnage(jsonObject.getString("tonnage"));
        motorVehicleSale.setSellerBankAccount(jsonObject.getString("seller_bank_account"));
        motorVehicleSale.setSellerAddress(jsonObject.getString("address"));
        motorVehicleSale.setTax(jsonObject.getBigDecimal("tax"));
        motorVehicleSale.setMachineCode(jsonObject.getString("machine_code"));
        motorVehicleSale.setBuyerName(jsonObject.getString("buyer"));
        motorVehicleSale.setTaxAuthoritiesCode(jsonObject.getString("tax_authorities_code"));
        motorVehicleSale.setSellerPhone(jsonObject.getString("phone"));
        motorVehicleSale.setCarCode(jsonObject.getString("car_code"));
        motorVehicleSale.setCompanySeal(jsonObject.getString("company_seal"));
        motorVehicleSale.setInvoiceSheet(jsonObject.getString("form_type"));
        motorVehicleSale.setPageNumber(jsonObject.getString("form_name"));
        motorVehicleSale.setPreTaxAmount(jsonObject.getBigDecimal("pretax_amount"));
        motorVehicleSale.setCoordinate(identifyResults.getRegion());
        motorVehicleSale.setOrientation(Integer.parseInt("-"+identifyResults.getOrientation()));
        return new IdentificationData<>(dataImageFilesInfo.getFileType(), motorVehicleSale);
    }
}
