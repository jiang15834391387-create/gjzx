package org.smartlink.business.invoice.autoinvcheck.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.springframework.stereotype.Component;

/**
 * 二手车发票信息转换
 *
 */
@Component
public class AutoinvChangeUsedCarSales {

    public void changeUsedCarSales(JSONObject jsonObject, DataUsedCarSales usedCarSales) {
        usedCarSales.setInvoiceCode(jsonObject.getStr("Code"));
        usedCarSales.setInvoiceNumber(jsonObject.getStr("No"));
        // 开票日期
        usedCarSales.setInvoiceDate(jsonObject.getStr("Date"));
        //总金额
        usedCarSales.setInvoiceTotal(jsonObject.getStr("CarSumAmount"));
        // 卖方单位/个人
        usedCarSales.setSellerName(jsonObject.getStr("SellerCompany"));
        // 卖方单位代码/身份证号
        usedCarSales.setSellerId(jsonObject.getStr("SellerCompanyCode"));
        // 卖方单位/个人住址
        if (StringUtils.isNotBlank(jsonObject.getStr("SellerCompanyAddress"))) {
            usedCarSales.setSellerAddress(jsonObject.getStr("SellerCompanyAddress"));
        }
        // 卖方电话
        if (StringUtils.isNotBlank(jsonObject.getStr("SellerPhone"))) {
            usedCarSales.setSellerPhone(jsonObject.getStr("SellerPhone"));
        }
        // 买方单位/个人
        usedCarSales.setBuyerName(jsonObject.getStr("BuyerCompany"));
        // 买方单位/个人代码/身份证号码
        usedCarSales.setBuyerId(jsonObject.getStr("BuyerCompanyCode"));
        // 买方单位/个人住址
        if (StringUtils.isNotBlank(jsonObject.getStr("BuyerCompanyAddress"))) {
            usedCarSales.setBuyerAddress(jsonObject.getStr("BuyerCompanyAddress"));
        }
        // 买方电话
        if (StringUtils.isNotBlank(jsonObject.getStr("BuyerPhone"))) {
            usedCarSales.setBuyerPhone(jsonObject.getStr("BuyerPhone"));
        }
        // 二手车市场
        usedCarSales.setCompanyName(jsonObject.getStr("LemonMarket"));
        // 二手车市场纳税人识别号
        usedCarSales.setCompanyTaxId(jsonObject.getStr("LemonMarketCode"));
        // 二手车市场地址
        if (StringUtils.isNotBlank(jsonObject.getStr("LemonMarketAddress"))) {
            usedCarSales.setLemonMarketAddress(jsonObject.getStr("LemonMarketAddress"));
        }
        // 二手车市场开户行及账号
        if (StringUtils.isNotBlank(jsonObject.getStr("LemonMarketBank"))) {
            usedCarSales.setLemonMarketBankAndCcount(jsonObject.getStr("LemonMarketBank"));
        }
        // 二手车市场电话
        if (StringUtils.isNotBlank(jsonObject.getStr("LemonMarketPhone"))) {
            usedCarSales.setLemonMarketPhone(jsonObject.getStr("LemonMarketPhone"));
        }
        // 车牌照号
        usedCarSales.setLicensePlate(jsonObject.getStr("LicensePlate"));
        // 登记证号
        usedCarSales.setRegisTrationNumber(jsonObject.getStr("RegistrationNo"));
        // 车辆识别代号/车架号码
        usedCarSales.setCarCode(jsonObject.getStr("VechicleFrameNO"));
        // 厂牌型号
        usedCarSales.setCarModel(jsonObject.getStr("BrandModel"));
        //机器编号
        usedCarSales.setMachineId(jsonObject.getStr("MachineNo"));
        //备注
        usedCarSales.setRemark(jsonObject.getStr("Remark"));
        //转入地车辆管理所
        usedCarSales.setDestinationDepartmentOfMotorVehicles(jsonObject.getStr("VehicleOfficeName"));
        //经营拍卖单位
        if (StringUtils.isNotBlank(jsonObject.getStr("OperationCompany"))) {
            usedCarSales.setBusinessUnit(jsonObject.getStr("OperationCompany"));
        }
        //经营拍卖地址
        if (StringUtils.isNotBlank(jsonObject.getStr("OperationCompanyAddress"))) {
            usedCarSales.setBusinessUnitAddress(jsonObject.getStr("OperationCompanyAddress"));
        }
        //经营拍卖单位纳税人识别号
        if (StringUtils.isNotBlank(jsonObject.getStr("OperationCompanyCode"))) {
            usedCarSales.setBusinessUnitTaxNo(jsonObject.getStr("OperationCompanyCode"));
        }
        //经营拍卖单位开户行及账号
        if (StringUtils.isNotBlank(jsonObject.getStr("OperationCompanyBank"))) {
            usedCarSales.setSellerAccount(jsonObject.getStr("OperationCompanyBank"));
        }
        //经营拍卖单位电话
        if (StringUtils.isNotBlank(jsonObject.getStr("OperationCompanyPhone"))) {
            usedCarSales.setBusinessUnitPhone(jsonObject.getStr("OperationCompanyPhone"));
        }
        //车辆类型
        if (StringUtils.isNotBlank(jsonObject.getStr("VehicleType"))) {
            usedCarSales.setVehicleType(jsonObject.getStr("VehicleType"));
        }
        //数电票标记
//        if (StringUtils.isNotBlank(jsonObject.getStr("EinvoiceMark"))) {
////            usedCarSales.setEinvoiceMark(jsonObject.getStr("EinvoiceMark"));
//        }
    }
}
