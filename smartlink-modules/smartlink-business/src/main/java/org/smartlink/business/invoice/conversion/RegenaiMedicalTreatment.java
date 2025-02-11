package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONObject;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatment;
import org.springframework.stereotype.Component;
/**
 * 非税收入基本转换信息
 */
@Component
public class RegenaiMedicalTreatment {
    public void changeMedicalTreatment(JSONObject jsonObject, DataMedicalTreatment medicalTreatment) {
        medicalTreatment.setInvoiceCode(jsonObject.getStr("code"));
        medicalTreatment.setInvoiceNumber(jsonObject.getStr("number"));
        medicalTreatment.setInvoiceDate(jsonObject.getStr("date"));
        //金额
        medicalTreatment.setInvoiceTotal(jsonObject.getStr("total"));
        // 校验码
        medicalTreatment.setCheckCode(jsonObject.getStr("check_code"));
        //购买方名称
        medicalTreatment.setPayer(jsonObject.getStr("buyer"));
        //购买方纳税识别号
        medicalTreatment.setSocialCreditCode(jsonObject.getStr("buyer_credit_code"));
        //销售方
        medicalTreatment.setPayee(jsonObject.getStr("seller"));
        //发票种类
        medicalTreatment.setTitle(jsonObject.getStr("electronic_ticket_title"));


    }
}
