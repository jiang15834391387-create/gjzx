package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.invoice.bo.DataOcrDetailsBo;
import org.smartlink.server.nc.domain.invoice.vo.DataOcrDetailsVo;

import java.util.List;

public interface IDataOcrDetailsService {
    /**
     * 查询ocr明细列表
     *
     * @param dataOcrDetails ocr明细
     * @return ocr明细集合
     */
    List<DataOcrDetailsVo> queryList(DataOcrDetailsBo bo);
}
