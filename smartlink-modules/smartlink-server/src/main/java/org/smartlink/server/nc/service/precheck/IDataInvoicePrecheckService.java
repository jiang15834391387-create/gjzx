package org.smartlink.server.nc.service.precheck;

import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheckBo;
import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheckVo;

import java.util.List;

/**
 * 发票预校验Service接口
 *
 * @author L
 * @date
 */
public interface IDataInvoicePrecheckService {

    /**
     * 查询发票预校验列表
     *
     * @param bo 发票预校验
     * @return 发票预校验集合
     */
    List<DataInvoicePrecheckVo> queryList(DataInvoicePrecheckBo bo);
}
