package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.mapper.DataOcrDetailsMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 * 非税详情信息表
 *
 */
@Component
public class RegenaichangeMedicalTreatmentDetails {
    private final DataOcrDetailsMapper detailsMapper;

    public RegenaichangeMedicalTreatmentDetails(DataOcrDetailsMapper detailsMapper) {
        this.detailsMapper = detailsMapper;
    }

    public List<DataOcrDetails> changeMedicalTreatmentDetails(JSONObject jsonObject, String fileId) {
        //根据fileId条件获取非税详情列表
        List<DataOcrDetails>detailList=detailsMapper.selectList(new LambdaQueryWrapper<DataOcrDetails>().eq(DataOcrDetails::getFileId, fileId));
        JSONArray jsonArray = jsonObject.getJSONArray("items1");
        List<DataOcrDetails> medicalTreatmentDetails = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject  entries = jsonArray.getJSONObject(i);
            //创建详情对象
            DataOcrDetails medicalTreatment = new DataOcrDetails();
            //详情id
            medicalTreatment.setId(detailList.get(i).getId());
            //fileid
            medicalTreatment.setFileId(fileId);
            //项目名称
            medicalTreatment.setProjectName(entries.getStr("name"));
            //数量
            medicalTreatment.setDetailsCount(entries.getStr("quantity"));
            //金额
            medicalTreatment.setDetailAmount(entries.getStr("total"));
            //备注
            medicalTreatment.setRemark(entries.getStr("remarks"));
            medicalTreatmentDetails.add(medicalTreatment);
        }
        return medicalTreatmentDetails;
    }
}
