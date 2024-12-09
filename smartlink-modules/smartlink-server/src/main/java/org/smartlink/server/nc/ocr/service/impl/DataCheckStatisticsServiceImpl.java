package org.smartlink.server.nc.ocr.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.nc.domain.invoice.DataCheckStatistics;
import org.smartlink.server.nc.mapper.DataCheckStatisticsMapper;
import org.smartlink.server.nc.ocr.service.IDataCheckStatisticsService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 查验记录统计Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataCheckStatisticsServiceImpl implements IDataCheckStatisticsService {

    private final DataCheckStatisticsMapper baseMapper;
    @Override
    public boolean insertCheckInfo(Integer checkSupplierEnum, String checkStatus) {
        if (checkSupplierEnum == null) {
            return false;
        }
        DataCheckStatistics checkStatistics = new DataCheckStatistics();
        checkStatistics.setID(IdUtil.simpleUUID());
        checkStatistics.setCheckSupplier(Convert.toStr(checkSupplierEnum));
        checkStatistics.setCheckDate(new Date());
        checkStatistics.setTYPE(checkStatus);
        checkStatistics.setREMARK(""); //必填 null 后续改动
        return baseMapper.insert(checkStatistics) > 0;
    }
}
