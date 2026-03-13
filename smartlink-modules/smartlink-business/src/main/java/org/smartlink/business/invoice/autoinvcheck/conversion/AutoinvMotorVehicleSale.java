package org.smartlink.business.invoice.autoinvcheck.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.springframework.stereotype.Component;

/**
 * 机动车销售统一发票转换
 *
 */
@Component
public class AutoinvMotorVehicleSale {

    public void changeMotorVehicleSale(JSONObject jsonObject, DataMotorVehicleSale motorVehicleSale) {
        motorVehicleSale.setInvoiceCode(jsonObject.getStr("Code"));
        motorVehicleSale.setInvoiceNumber(jsonObject.getStr("No"));
        //机打代码
        motorVehicleSale.setMachineCode(jsonObject.getStr("MachineNo"));
        // 开票日期
        motorVehicleSale.setInvoiceDate(jsonObject.getStr("Date"));
        // 税前金额
        motorVehicleSale.setPreTaxAmount(jsonObject.getStr("Amount"));
        // 税额
        motorVehicleSale.setTax(jsonObject.getStr("TaxAmount"));
        //税率
        motorVehicleSale.setTaxRate(jsonObject.getStr("TaxRate"));
        // 总金额
        motorVehicleSale.setInvoiceTotal(jsonObject.getStr("SummaryAmount"));
        // 销售方名称
        motorVehicleSale.setSeller(jsonObject.getStr("SalerName"));
        // 销售单位纳税人识别号
        motorVehicleSale.setSellerTaxid(jsonObject.getStr("SalerTaxCode"));
        //买方单位
        motorVehicleSale.setBuyerName(jsonObject.getStr("BuyerName"));
        // 买方单位纳税人识别号
        motorVehicleSale.setBuyerId(jsonObject.getStr("BuyerTaxCode"));
        // 主管税务机关名称
        motorVehicleSale.setTaxAuthorities(jsonObject.getStr("TaxAuthorityName"));
        //主管税务机关代码
        motorVehicleSale.setTaxAuthoritiesCode(jsonObject.getStr("TaxAuthorityCode"));
        //完税凭证号码
        if (StringUtils.isNotBlank(jsonObject.getStr("TaxPaymentCertificateNo"))) {
            motorVehicleSale.setTaxPaymentCertificateNo(jsonObject.getStr("TaxPaymentCertificateNo"));
        }
        //车架号
        motorVehicleSale.setCarCode(jsonObject.getStr("VechicleFrameNo"));
        // 发动机号
        motorVehicleSale.setCarEngineCode(jsonObject.getStr("EngineNo"));
        //厂牌型号
        motorVehicleSale.setCarModel(jsonObject.getStr("BrandModel"));
        // 合格证号
        motorVehicleSale.setCertificateNumber(jsonObject.getStr("QualificationNo"));
        //车辆类型
        motorVehicleSale.setVehicleType(jsonObject.getStr("VehicleType"));
        //产地
        motorVehicleSale.setProduceArea(jsonObject.getStr("ProduceArea"));
        //商检单号
        if (StringUtils.isNotBlank(jsonObject.getStr("CommodityInspectionNo"))) {
            motorVehicleSale.setCommodityInspectionNo(jsonObject.getStr("CommodityInspectionNo"));
        }
        // 进口证明书号
        motorVehicleSale.setCertificateOfImport(jsonObject.getStr("ImportCertificateNo"));
        //电话
        motorVehicleSale.setSellerPhone(jsonObject.getStr("SalerPhone"));
        //地址
        motorVehicleSale.setSellerAddress(jsonObject.getStr("SalerAddress"));
        //开户行银行
        motorVehicleSale.setSellerBankName(jsonObject.getStr("SalerBank"));
        //银行账号
        motorVehicleSale.setSellerBankAccount(jsonObject.getStr("SalerAccount"));
        //吨位
        motorVehicleSale.setTonnage(jsonObject.getStr("Tonnage"));
        // 限乘人数
        motorVehicleSale.setLimitedPeopleCount(jsonObject.getStr("LimitedPassenger"));
//        //身份证/组织机构代码
//        if (StringUtils.isNotBlank(jsonObject.getStr("IdNo"))) {
//            motorVehicleSale.setIdNo(jsonObject.getStr("IdNo"));
//        }
//        //数电票标记
//        if (StringUtils.isNotBlank(jsonObject.getStr("EinvoiceMark"))) {
//            motorVehicleSale.setEinvoiceMark(jsonObject.getStr("EinvoiceMark"));
//        }
    }
}
