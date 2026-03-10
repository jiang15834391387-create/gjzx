package org.smartlink.business.invoice.check.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.springframework.stereotype.Component;


/**
 * 二手车发票信息转换
 *
 */
@Component
public class RegenaiChangeUsedCarSales {

    public void changeUsedCarSales(JSONObject jsonObject, DataUsedCarSales usedCarSales) {
        usedCarSales.setInvoiceCode(jsonObject.getStr("code"));
        usedCarSales.setInvoiceNumber(jsonObject.getStr("number"));
        // 开票日期
        usedCarSales.setInvoiceDate(jsonObject.getStr("date"));
        //总金额
        usedCarSales.setInvoiceTotal(jsonObject.getStr("total"));
        // 卖方单位/个人
        usedCarSales.setSellerName(jsonObject.getStr("seller"));
        // 卖方单位代码/身份 证号
        usedCarSales.setSellerId(jsonObject.getStr("seller_id"));
        // 卖方单位/个人住址
        if (StringUtils.isNotBlank(jsonObject.getStr("seller_address"))){
            usedCarSales.setSellerAddress(jsonObject.getStr("seller_address"));
        }
        // 卖方电话
        if (StringUtils.isNotBlank(jsonObject.getStr("seller_tel"))){
            usedCarSales.setSellerPhone(jsonObject.getStr("seller_tel"));
        }
        // 买方单位/个人
        usedCarSales.setBuyerName(jsonObject.getStr("buyer"));
        // 买方单位/个人代码/身份证号码
        usedCarSales.setBuyerId(jsonObject.getStr("buyer_id"));
        // 买方单位/个人住址
        if (StringUtils.isNotBlank(jsonObject.getStr("buyer_address"))){
            usedCarSales.setBuyerAddress(jsonObject.getStr("buyer_address"));
        }
        // 买方电话
        if (StringUtils.isNotBlank(jsonObject.getStr("buyer_tel"))){
            usedCarSales.setBuyerPhone(jsonObject.getStr("buyer_tel"));
        }
        // 二手车市场
        usedCarSales.setCompanyName(jsonObject.getStr("company_name"));
        // 二手车市场纳税人 识别号
        usedCarSales.setCompanyTaxId(jsonObject.getStr("company_tax_id"));
        // 二手车市场地址
        if (StringUtils.isNotBlank(jsonObject.getStr("company_address"))){
            usedCarSales.setLemonMarketAddress(jsonObject.getStr("company_address"));
        }
        // 二手车市场开户行及账号
        if (StringUtils.isNotBlank(jsonObject.getStr("company_bank_account"))){
            usedCarSales.setLemonMarketBankAndCcount(jsonObject.getStr("company_bank_account"));
        }
        // 二手车市场电话
        if (StringUtils.isNotBlank(jsonObject.getStr("company_tel"))){
            usedCarSales.setLemonMarketPhone(jsonObject.getStr("company_tel"));
        }
        // 车牌照号
        usedCarSales.setLicensePlate(jsonObject.getStr("license_plate"));
        // 登记证号
        usedCarSales.setRegisTrationNumber(jsonObject.getStr("registration_number"));
        // 车辆识别代号/车架号码
        usedCarSales.setCarCode(jsonObject.getStr("car_code"));
        // 厂牌型号
        usedCarSales.setCarModel(jsonObject.getStr("car_model"));
        //机器编号
        usedCarSales.setMachineId(jsonObject.getStr("machine_code"));
        //备注
        usedCarSales.setRemark(jsonObject.getStr("remark"));
        //转入地车辆管理所
        usedCarSales.setDestinationDepartmentOfMotorVehicles(jsonObject.getStr("transfer_vehicle_office"));
        //经营拍卖单位
        if (StringUtils.isNotBlank(jsonObject.getStr("auction_name"))){
            usedCarSales.setBusinessUnit(jsonObject.getStr("auction_name"));
        }
        //经营拍卖地址
        if (StringUtils.isNotBlank(jsonObject.getStr("auction_address"))){
            usedCarSales.setBusinessUnitAddress(jsonObject.getStr("auction_address"));
        }
        //经营拍卖单位纳税人识别号
        if (StringUtils.isNotBlank(jsonObject.getStr("auction_tax_id"))){
            usedCarSales.setBusinessUnitTaxNo(jsonObject.getStr("auction_tax_id"));
        }
        //经营拍卖单位开户行及账号
        if (StringUtils.isNotBlank(jsonObject.getStr("auction_bank_account"))){
            usedCarSales.setBusinessUnitPhone(jsonObject.getStr("auction_bank_account"));
        }
        //经营拍卖单位电话
        if (StringUtils.isNotBlank(jsonObject.getStr("auction_tel"))){
            usedCarSales.setBusinessUnitPhone(jsonObject.getStr("auction_tel"));
        }
    }
}
