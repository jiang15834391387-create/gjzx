package org.smartlink.web.service.nc;

import org.smartlink.web.domain.invoice.bo.DataOcrDetailsBo;
import org.smartlink.web.domain.invoice.vo.DataOcrDetailsVo;

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
