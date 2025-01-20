package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatmentDetail;
import org.smartlink.common.entity.domain.business.mapper.DataMedicalTreatmentDetailMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * 非税详情信息表
 *
 */
@Component
public class RegenaichangeMedicalTreatmentDetails {
    private final DataMedicalTreatmentDetailMapper flightsItineraryDetailMapper;

    public RegenaichangeMedicalTreatmentDetails(DataMedicalTreatmentDetailMapper flightsItineraryDetailMapper) {
        this.flightsItineraryDetailMapper = flightsItineraryDetailMapper;
    }

    public List<DataMedicalTreatmentDetail> changeMedicalTreatmentDetails(JSONObject jsonObject, String fileId) {
        //根据fileId条件获取非税详情列表
        List<DataMedicalTreatmentDetail>detailList=flightsItineraryDetailMapper.selectList(new LambdaQueryWrapper<DataMedicalTreatmentDetail>().eq(DataMedicalTreatmentDetail::getFileId, fileId));
        JSONArray jsonArray = jsonObject.getJSONArray("items1");
        List<DataMedicalTreatmentDetail> medicalTreatmentDetails = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject  entries = jsonArray.getJSONObject(i);
            //创建详情对象
            DataMedicalTreatmentDetail medicalTreatment = new DataMedicalTreatmentDetail();
            //详情id
            medicalTreatment.setId(detailList.get(i).getId());
            //fileid
            medicalTreatment.setFileId(fileId);
            //项目名称
            medicalTreatment.setProjectName(entries.getStr("name"));
            //数量
            medicalTreatment.setQuantity(entries.getStr("quantity"));
            //金额
            medicalTreatment.setAmount(entries.getStr("total"));
            //备注
            medicalTreatment.setComment(entries.getStr("remarks"));
            medicalTreatmentDetails.add(medicalTreatment);
        }
        return medicalTreatmentDetails;
    }
}
