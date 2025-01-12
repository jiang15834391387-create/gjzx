package org.smartlink.common.check.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.smartlink.common.check.invoice.DataOcrDetails;
import org.smartlink.common.core.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * OCR 详情转换
 *
 */
@Component
public class RuiZhenChangeInvoiceDetails {
    public List<DataOcrDetails> changeInvoiceDetails(String fileId, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("items");
        List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject detail = jsonArray.getJSONObject(i);
            DataOcrDetails ocrDetails = new DataOcrDetails();
            ocrDetails.setId(IdUtil.simpleUUID());
           //详细名称
            ocrDetails.setName(detail.getStr("name"));
            //规格型号
            ocrDetails.setStandard(detail.getStr("standard"));
            //单位
            ocrDetails.setUnit(detail.getStr("unit"));
            //数量
            ocrDetails.setDetailsCount(detail.getStr("count"));
            //单价
            ocrDetails.setPrice(detail.getStr("price"));
            //金额
            ocrDetails.setDetailAmount(detail.getStr("total"));
            //税额
            ocrDetails.setTax(detail.getStr("tax"));
            //税率
            ocrDetails.setTaxRate(detail.getStr("tax_rate"));
            //车牌号
            if (StringUtils.isNotBlank(detail.getStr("license_plate"))){
                ocrDetails.setLicensePlateNum(detail.getStr("license_plate_num"));
            }
            //车辆类型
            if (StringUtils.isNotBlank(detail.getStr("vehicle_type"))){
                ocrDetails.setVehicleType(detail.getStr("vehicle_type"));
            }
            //通行日期起
            if (StringUtils.isNotBlank(detail.getStr("start_date"))){
                ocrDetails.setCurrentDateStart(detail.getStr("start_date"));
            }
            //通行日期止
            if (StringUtils.isNotBlank(detail.getStr("end_date"))){
                ocrDetails.setCurrentDateEnd(detail.getStr("end_date"));
            }
            ocrDetailsList.add(ocrDetails);

        }
        return ocrDetailsList;
    }
}
