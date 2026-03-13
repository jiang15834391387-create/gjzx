package org.smartlink.business.invoice.autoinvcheck.conversion;

import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.springframework.stereotype.Component;

/**
 * OCR基本信息的转换
 *
 */
@Component
public class AutoinvBasicOcrInfo {

    public void setBasicOcrInfo(JSONObject jsonObject, DataOcrInfo ocrInfo) {
        ocrInfo.setInvoiceCode(jsonObject.getStr("Code"));
        ocrInfo.setInvoiceNumber(jsonObject.getStr("No"));
        ocrInfo.setInvoiceDate(jsonObject.getStr("Date"));
        //税前金额
        ocrInfo.setPretaxAmount(jsonObject.getStr("Amount"));
        //税额合计
        ocrInfo.setSumTax(jsonObject.getStr("TaxAmount"));
        //小写 价税合计
        ocrInfo.setTotalLowercase(jsonObject.getStr("SummaryAmount"));
        // 校验码
        ocrInfo.setCheckCode(jsonObject.getStr("VCode"));
        //机器编码
        ocrInfo.setMachineCode(jsonObject.getStr("MachineNo"));
        //备注
        ocrInfo.setRemark(jsonObject.getStr("Remark"));
        //代开标志
        ocrInfo.setReplaceOpen(jsonObject.getStr("AgentMark"));
//        //特殊票种标志
//        ocrInfo.setSpecialTicketMark(jsonObject.getStr("SpecialTicketMark"));

        //销售方信息
        JSONObject seller = jsonObject.getJSONObject("Saler");
        if (seller != null) {
            //销售方名称
            ocrInfo.setSellerName(seller.getStr("Name"));
            //销售方纳税人识别号
            ocrInfo.setSellerNo(seller.getStr("TaxCode"));
            //销售方地址、电话
            ocrInfo.setSellerAddress(seller.getStr("AddressPhone"));
            //销售方开户行及账号
            ocrInfo.setSellerAccount(seller.getStr("AccountBank"));
        }

        //购买方信息
        JSONObject buyer = jsonObject.getJSONObject("Buyer");
        if (buyer != null) {
            // 购方名称
            ocrInfo.setBuyerName(buyer.getStr("Name"));
            // 购方纳税人识别号
            ocrInfo.setBuyerNo(buyer.getStr("TaxCode"));
            //购买方开户行及账号
            ocrInfo.setBuyerAccount(buyer.getStr("AccountBank"));
            // 购方地址、电话
            ocrInfo.setBuyerAddress(buyer.getStr("AddressPhone"));
        }

        //收款人（卷票字段）
        if (StringUtils.isNotBlank(jsonObject.getStr("Payee"))) {
            ocrInfo.setPayee(jsonObject.getStr("Payee"));
        }

        //通行费标识
        if (StringUtils.isNotBlank(jsonObject.getStr("TollMark"))) {
            ocrInfo.setTransitMark(jsonObject.getStr("TollMark"));
        }
    }
}
