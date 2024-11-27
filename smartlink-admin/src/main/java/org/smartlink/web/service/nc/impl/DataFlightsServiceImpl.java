package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.invoice.DataFlights;
import org.smartlink.web.domain.invoice.bo.DataFlightsBo;
import org.smartlink.web.domain.invoice.vo.DataFlightsVo;
import org.smartlink.web.mapper.DataFlightsMapper;
import org.smartlink.web.service.nc.IDataFlightsService;
import org.smartlink.web.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * ocr明细Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataFlightsServiceImpl implements IDataFlightsService {

    private final DataFlightsMapper baseMapper;
    /**
     * 查询航空电子行程单明细列表
     *
     * @param bo 航空电子行程单明细
     * @return 航空电子行程单明细
     */
    @Override
    public List<DataFlightsVo> queryList(DataFlightsBo bo) {
        LambdaQueryWrapper<DataFlights> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataFlights> buildQueryWrapper(DataFlightsBo bo) {
        LambdaQueryWrapper<DataFlights> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCarrier()), DataFlights::getCarrier, bo.getCarrier());
        lqw.eq(bo.getInvoiceDate() != null, DataFlights::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataFlights::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getFlightNumber()), DataFlights::getFlightNumber, bo.getFlightNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOn()), DataFlights::getStationGetOn, bo.getStationGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getSeat()), DataFlights::getSeat, bo.getSeat());
        lqw.eq(StringUtils.isNotBlank(bo.getUserId()), DataFlights::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), DataFlights::getUserName, bo.getUserName());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTime()), DataFlights::getInvoiceTime, bo.getInvoiceTime());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOff()), DataFlights::getStationGetOff, bo.getStationGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataFlights::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataFlights::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getConfidence()), DataFlights::getConfidence, bo.getConfidence());
        return lqw;
    }
}
