package org.smartlink.business.invoice.conversion;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.smartlink.common.entity.domain.business.domain.DataFlightsItineraryDetail;
import org.smartlink.common.entity.domain.business.mapper.DataFlightsItineraryDetailMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * 航空运输电子客票行程单详情转换
 */
@Component
public class RegenaiChangeFlightItineraryDetails {
    private final DataFlightsItineraryDetailMapper flightsItineraryDetailMapper;

    public RegenaiChangeFlightItineraryDetails(DataFlightsItineraryDetailMapper flightsItineraryDetailMapper) {
        this.flightsItineraryDetailMapper = flightsItineraryDetailMapper;
    }

    public List<DataFlightsItineraryDetail> changeFlightItineraryDetails(JSONObject jsonObject,String fileId) {
        //根据fileId条件获取航空详情列表
        List<DataFlightsItineraryDetail>detailList=flightsItineraryDetailMapper.selectList(new LambdaQueryWrapper<DataFlightsItineraryDetail>().eq(DataFlightsItineraryDetail::getFileId, fileId));
        JSONArray jsonArray = jsonObject.getJSONArray("flights");
        List<DataFlightsItineraryDetail> flightsItineraryDetails = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
           JSONObject  entries = jsonArray.getJSONObject(i);
            //创建详情对象
            DataFlightsItineraryDetail flightsDetail = new DataFlightsItineraryDetail();
            //详情id
            flightsDetail.setId(detailList.get(i).getId());
            //fileid
            flightsDetail.setFileId(fileId);
            //航班号
            flightsDetail.setFlightNumber(entries.getStr("flight_number"));
            //航段序号
            flightsDetail.setFlightSegment(entries.getStr("flight_segment"));
            //出发站
            flightsDetail.setStationGetOn(entries.getStr("from"));
            //到达站
            flightsDetail.setStationGetOff(entries.getStr("to"));
           //客运人
            flightsDetail.setCarrier(entries.getStr("carrier"));
            //客票级别
            flightsDetail.setFareBasis(entries.getStr("fare_basis"));
            //乘机日期
            flightsDetail.setInvoiceDate(entries.getStr("time"));
            //座位等级
            flightsDetail.setSpaceLevel(entries.getStr("seat"));
            flightsItineraryDetails.add(flightsDetail);
        }
        return flightsItineraryDetails;
    }
}
