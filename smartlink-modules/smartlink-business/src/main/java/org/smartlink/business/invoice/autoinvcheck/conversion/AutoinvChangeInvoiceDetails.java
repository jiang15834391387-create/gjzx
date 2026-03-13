package org.smartlink.business.invoice.autoinvcheck.conversion;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.mapper.DataOcrDetailsMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * OCR 详情转换
 *
 */
@Component
public class AutoinvChangeInvoiceDetails {
    private final DataOcrDetailsMapper ocrDetailsMapper;

    public AutoinvChangeInvoiceDetails(DataOcrDetailsMapper ocrDetailsMapper) {
        this.ocrDetailsMapper = ocrDetailsMapper;
    }

    public List<DataOcrDetails> changeInvoiceDetails(String fileId, JSONObject jsonObject) {
        //根据fileId获取ocr详情列表
        List<DataOcrDetails> details = ocrDetailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
        JSONArray jsonArray = jsonObject.getJSONArray("Items");
        List<DataOcrDetails> ocrDetailsList = new ArrayList<>();
        if (jsonArray == null || jsonArray.isEmpty()) {
            ocrDetailsList.addAll(details);
            return ocrDetailsList;
        }
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject detail = jsonArray.getJSONObject(i);
            DataOcrDetails ocrDetails = new DataOcrDetails();
            //详情表id 检查details列表的索引是否越界
            if (i < details.size()) {
                ocrDetails.setId(details.get(i).getId());
            } else {
                // 如果索引越界，使用details.get(0)的id
                if (!details.isEmpty()) {
                    ocrDetails.setId(details.get(0).getId());
                } else {
                    ocrDetails.setId(IdUtil.simpleUUID());
                }
            }
            //fileId
            ocrDetails.setFileId(fileId);
            //明细编号
            ocrDetails.setDetailNo(detail.getStr("InvoiceDetailNumber"));
            //详细名称
            ocrDetails.setName(detail.getStr("Name"));
            //规格型号
            ocrDetails.setStandard(detail.getStr("Specification"));
            //单位
            ocrDetails.setUnit(detail.getStr("Unit"));
            //数量
            ocrDetails.setDetailsCount(detail.getStr("Quantity"));
            //单价
            ocrDetails.setPrice(detail.getStr("Price"));
            //金额
            ocrDetails.setDetailAmount(detail.getStr("Amount"));
            //税额
            ocrDetails.setTax(detail.getStr("TaxAmount"));
            //税率
            ocrDetails.setTaxRate(detail.getStr("TaxRate"));
            //商品编码
            ocrDetails.setCommodityCode(detail.getStr("TaxClassificationCode"));

            // 卷票字段：含税金额
            if (StringUtils.isNotBlank(detail.getStr("TotalAmount"))) {
                ocrDetails.setDetailAmount(detail.getStr("TotalAmount"));
            }

            // 通行费发票字段
            //车牌号
            if (StringUtils.isNotBlank(detail.getStr("CarNo"))) {
                ocrDetails.setLicensePlateNum(detail.getStr("CarNo"));
            }
            //车辆类型
            if (StringUtils.isNotBlank(detail.getStr("Type"))) {
                ocrDetails.setVehicleType(detail.getStr("Type"));
            }
            //通行日期起
            if (StringUtils.isNotBlank(detail.getStr("PassDateStart"))) {
                ocrDetails.setCurrentDateStart(detail.getStr("PassDateStart"));
            }
            //通行日期止
            if (StringUtils.isNotBlank(detail.getStr("PassDateEnd"))) {
                ocrDetails.setCurrentDateEnd(detail.getStr("PassDateEnd"));
            }

            ocrDetailsList.add(ocrDetails);
        }
        return ocrDetailsList;
    }
}
