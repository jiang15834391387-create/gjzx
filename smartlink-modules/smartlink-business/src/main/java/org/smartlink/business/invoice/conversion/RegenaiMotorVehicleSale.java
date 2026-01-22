package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.springframework.stereotype.Component;

/**
 * 机动车销售统一发票
 *
 */
@Component
public class RegenaiMotorVehicleSale {

    public void changeMotorVehicleSale(JSONObject jsonObject, DataMotorVehicleSale motorVehicleSale) {
        motorVehicleSale.setInvoiceCode(jsonObject.getStr("code"));
        motorVehicleSale.setInvoiceNumber(jsonObject.getStr("number"));
        //机打代码
        motorVehicleSale.setMachineCode(jsonObject.getStr("machine_code"));
        // 开票日期
        motorVehicleSale.setInvoiceDate("date");
        // 税前金额
        motorVehicleSale.setPreTaxAmount(jsonObject.getStr("pretax_amount"));
        // 税额
        motorVehicleSale.setTax(jsonObject.getStr("tax"));
        //税率
        motorVehicleSale.setTaxRate(jsonObject.getStr("tax_rate"));
        // 总金额
        motorVehicleSale.setInvoiceTotal(jsonObject.getStr("total"));
        // 销售方名称
        motorVehicleSale.setSeller(jsonObject.getStr("seller"));
        // 销售单位纳税人识别号
        motorVehicleSale.setSellerTaxid(jsonObject.getStr("seller_tax_id"));
        //买方单位
        motorVehicleSale.setBuyerName(jsonObject.getStr("buyer"));
        // 买方单位纳税人识别号
        motorVehicleSale.setBuyerId(jsonObject.getStr("buyer_tax_id"));
        // 主管税务机关名称
        motorVehicleSale.setTaxAuthorities(jsonObject.getStr("tax_authorities"));
        //主管税务机关代码
        motorVehicleSale.setTaxAuthoritiesCode(jsonObject.getStr("tax_authorities_code"));
        //完税凭证号码
        if (StringUtils.isNotBlank(jsonObject.getStr("tax_certificate_number"))){
            motorVehicleSale.setTaxPaymentCertificateNo(jsonObject.getStr("tax_certificate_number"));
        }
        //车架号
        motorVehicleSale.setCarCode(jsonObject.getStr("car_code"));
        // 发动机号
        motorVehicleSale.setCarEngineCode(jsonObject.getStr("car_engine_code"));
        //厂牌型号
        motorVehicleSale.setCarModel(jsonObject.getStr("car_model"));
        // 合格证号
        motorVehicleSale.setCertificateNumber(jsonObject.getStr("certificate_number"));
        //车辆类型
        motorVehicleSale.setVehicleType(jsonObject.getStr("car_type"));
        //产地
        motorVehicleSale.setProduceArea(jsonObject.getStr("original_place"));
        //商检单号
        if (StringUtils.isNotBlank(jsonObject.getStr("bill_number"))){
            motorVehicleSale.setCommodityInspectionNo(jsonObject.getStr("bill_number"));
        }
        // 进口证明书号
        motorVehicleSale.setCertificateOfImport(jsonObject.getStr("import_certificate_number"));
        //电话
        motorVehicleSale.setSellerPhone(jsonObject.getStr("tel"));
        //地址
        motorVehicleSale.setSellerAddress(jsonObject.getStr("address"));
        //开户行银行
        motorVehicleSale.setSellerBankName(jsonObject.getStr("bank"));
        //银行账号
        motorVehicleSale.setSellerBankAccount(jsonObject.getStr("account"));
        //吨位
        motorVehicleSale.setTonnage(jsonObject.getStr("tonnage"));
        // 限乘人数
        motorVehicleSale.setLimitedPeopleCount(jsonObject.getStr("limit_passengers_count"));
    }

}
