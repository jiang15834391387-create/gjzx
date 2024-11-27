package org.smartlink.web.service.nc;

import org.smartlink.web.domain.invoice.bo.DataFlightsBo;
import org.smartlink.web.domain.invoice.vo.DataFlightsVo;

import java.util.List;

public interface IDataFlightsService {

    /**
     * 查询航空电子行程单明细列表
     *
     * @param bo 航空电子行程单明细
     * @return 航空电子行程单明细集合
     */
    List<DataFlightsVo> queryList(DataFlightsBo bo);



}
