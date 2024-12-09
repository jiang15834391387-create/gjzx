package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.invoice.bo.DataFlightsBo;
import org.smartlink.server.nc.domain.invoice.vo.DataFlightsVo;

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
