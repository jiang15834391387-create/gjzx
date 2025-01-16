package org.smartlink.business.invoice.conversion;


import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.springframework.stereotype.Component;

/**
 * OCR基本信息的转换
 *
 */
@Component
public class RegenaiBasicOcrInfo {

    public void setBasicOcrInfo(JSONObject jsonObject, DataOcrInfo ocrInfo) {
        ocrInfo.setInvoiceCode(jsonObject.getStr("code"));
        ocrInfo.setInvoiceNumber(jsonObject.getStr("number"));
        ocrInfo.setInvoiceDate(jsonObject.getStr("date"));
        //税前金额
        ocrInfo.setPretaxAmount(jsonObject.getStr("pretax_amount"));
       //税额合计
        ocrInfo.setSumTax(jsonObject.getStr("tax"));
        //小写 价税合计
        ocrInfo.setTotalLowercase(jsonObject.getStr("total"));
        // 校验码
        ocrInfo.setCheckCode(jsonObject.getStr("check_code"));
        //销售方名称
        ocrInfo.setSellerName(jsonObject.getStr("seller"));
        //销售方纳税人识别号
        ocrInfo.setSellerNo(jsonObject.getStr("seller_tax_id"));
        //销售方地址、电话
        ocrInfo.setSellerAddress(jsonObject.getStr("seller_addr_tel"));
        //销售方开户行及账号
        ocrInfo.setSellerAccount(jsonObject.getStr("seller_bank_account"));
        // 购方名称
        ocrInfo.setBuyerName(jsonObject.getStr("buyer"));
        // 购方纳税人识别号
        ocrInfo.setBuyerNo(jsonObject.getStr("buyer_tax_id"));
        //购买方开户行及账号
        ocrInfo.setBuyerAccount(jsonObject.getStr("buyer_bank_account"));
        // 购方地址、电话
        ocrInfo.setBuyerAddress(jsonObject.getStr("buyer_addr_tel"));
        //备注
        ocrInfo.setRemark(jsonObject.getStr("remark"));
        //机器编码
        ocrInfo.setMachineCode(jsonObject.getStr("machine_code"));
        //作废标志
        ocrInfo.setCancellationMark(jsonObject.getStr("invalid_mark"));

        //成品油标志
        if (StringUtils.isNotBlank(jsonObject.getStr("oil_mark"))){
            ocrInfo.setOilMark(jsonObject.getStr("oil_mark"));
        }
        //收款人
        if (StringUtils.isNotBlank(jsonObject.getStr("receiptor"))){
            ocrInfo.setPayee(jsonObject.getStr("receiptor"));
        }
        //通行费标识
        if (StringUtils.isNotBlank(jsonObject.getStr("transit_mark"))){
            ocrInfo.setTransitMark(jsonObject.getStr("transit_mark"));
        }

    }
}
