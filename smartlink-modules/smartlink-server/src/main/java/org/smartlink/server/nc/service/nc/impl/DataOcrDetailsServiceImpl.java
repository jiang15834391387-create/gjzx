package org.smartlink.server.nc.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.nc.domain.invoice.DataOcrDetails;
import org.smartlink.server.nc.domain.invoice.bo.DataOcrDetailsBo;
import org.smartlink.server.nc.domain.invoice.vo.DataOcrDetailsVo;
import org.smartlink.server.nc.mapper.DataOcrDetailsMapper;
import org.smartlink.server.nc.service.nc.IDataOcrDetailsService;
import org.smartlink.server.nc.utils.StringUtils;
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
public class DataOcrDetailsServiceImpl implements IDataOcrDetailsService {

    private final DataOcrDetailsMapper baseMapper;

    /**
     * 查询ocr明细列表
     *
     * @param bo ocr明细
     * @return ocr明细
     */
    @Override
    public List<DataOcrDetailsVo> queryList(DataOcrDetailsBo bo) {
        LambdaQueryWrapper<DataOcrDetails> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }
    private LambdaQueryWrapper<DataOcrDetails> buildQueryWrapper(DataOcrDetailsBo bo) {
        LambdaQueryWrapper<DataOcrDetails> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDetailAmount() != null, DataOcrDetails::getDetailAmount, bo.getDetailAmount());
        lqw.eq(bo.getDetailsCount() != null, DataOcrDetails::getDetailsCount, bo.getDetailsCount());
        lqw.eq(StringUtils.isNotBlank(bo.getDetailNo()), DataOcrDetails::getDetailNo, bo.getDetailNo());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataOcrDetails::getFileId, bo.getFileId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DataOcrDetails::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCommodityCode()), DataOcrDetails::getCommodityCode, bo.getCommodityCode());
        lqw.like(StringUtils.isNotBlank(bo.getCommodityName()), DataOcrDetails::getCommodityName, bo.getCommodityName());
        lqw.eq(bo.getPrice() != null, DataOcrDetails::getPrice, bo.getPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxRate()), DataOcrDetails::getTaxRate, bo.getTaxRate());
        lqw.eq(StringUtils.isNotBlank(bo.getStandard()), DataOcrDetails::getStandard, bo.getStandard());
        lqw.eq(bo.getTax() != null, DataOcrDetails::getTax, bo.getTax());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), DataOcrDetails::getUnit, bo.getUnit());
        lqw.eq(bo.getCurrentDateEnd() != null, DataOcrDetails::getCurrentDateEnd, bo.getCurrentDateEnd());
        lqw.eq(bo.getCurrentDateStart() != null, DataOcrDetails::getCurrentDateStart, bo.getCurrentDateStart());
        lqw.eq(StringUtils.isNotBlank(bo.getLicensePlateNum()), DataOcrDetails::getLicensePlateNum, bo.getLicensePlateNum());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecialMark()), DataOcrDetails::getSpecialMark, bo.getSpecialMark());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataOcrDetails::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getConfidence()), DataOcrDetails::getConfidence, bo.getConfidence());
        return lqw;
    }

}
