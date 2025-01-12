package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrDetailsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrDetailsVo;
import org.smartlink.common.entity.domain.business.mapper.DataOcrDetailsMapper;
import org.smartlink.common.entity.domain.business.service.IDataOcrDetailsService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 增值税发票明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataOcrDetailsServiceImpl implements IDataOcrDetailsService {

    private final DataOcrDetailsMapper baseMapper;

    /**
     * 查询增值税发票明细
     *
     * @param id 主键
     * @return 增值税发票明细
     */
    @Override
    public DataOcrDetailsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询增值税发票明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 增值税发票明细分页列表
     */
    @Override
    public TableDataInfo<DataOcrDetailsVo> queryPageList(DataOcrDetailsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataOcrDetails> lqw = buildQueryWrapper(bo);
        Page<DataOcrDetailsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的增值税发票明细列表
     *
     * @param bo 查询条件
     * @return 增值税发票明细列表
     */
    @Override
    public List<DataOcrDetailsVo> queryList(DataOcrDetailsBo bo) {
        LambdaQueryWrapper<DataOcrDetails> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataOcrDetails> buildQueryWrapper(DataOcrDetailsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataOcrDetails> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOcrId()), DataOcrDetails::getOcrId, bo.getOcrId());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataOcrDetails::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getDetailAmount()), DataOcrDetails::getDetailAmount, bo.getDetailAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getDetailsCount()), DataOcrDetails::getDetailsCount, bo.getDetailsCount());
        lqw.eq(StringUtils.isNotBlank(bo.getDetailNo()), DataOcrDetails::getDetailNo, bo.getDetailNo());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DataOcrDetails::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getCommodityCode()), DataOcrDetails::getCommodityCode, bo.getCommodityCode());
        lqw.like(StringUtils.isNotBlank(bo.getCommodityName()), DataOcrDetails::getCommodityName, bo.getCommodityName());
        lqw.eq(StringUtils.isNotBlank(bo.getPrice()), DataOcrDetails::getPrice, bo.getPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxRate()), DataOcrDetails::getTaxRate, bo.getTaxRate());
        lqw.eq(StringUtils.isNotBlank(bo.getStandard()), DataOcrDetails::getStandard, bo.getStandard());
        lqw.eq(StringUtils.isNotBlank(bo.getTax()), DataOcrDetails::getTax, bo.getTax());
        lqw.eq(StringUtils.isNotBlank(bo.getUnit()), DataOcrDetails::getUnit, bo.getUnit());
        lqw.eq(StringUtils.isNotBlank(bo.getCurrentDateEnd()), DataOcrDetails::getCurrentDateEnd, bo.getCurrentDateEnd());
        lqw.eq(StringUtils.isNotBlank(bo.getCurrentDateStart()), DataOcrDetails::getCurrentDateStart, bo.getCurrentDateStart());
        lqw.eq(StringUtils.isNotBlank(bo.getLicensePlateNum()), DataOcrDetails::getLicensePlateNum, bo.getLicensePlateNum());
        lqw.eq(StringUtils.isNotBlank(bo.getVehicleType()), DataOcrDetails::getVehicleType, bo.getVehicleType());
        lqw.eq(StringUtils.isNotBlank(bo.getUsageTime()), DataOcrDetails::getUsageTime, bo.getUsageTime());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecialMark()), DataOcrDetails::getSpecialMark, bo.getSpecialMark());
        lqw.eq(StringUtils.isNotBlank(bo.getPlaceOfBuildingService()), DataOcrDetails::getPlaceOfBuildingService, bo.getPlaceOfBuildingService());
        lqw.like(StringUtils.isNotBlank(bo.getBuildingName()), DataOcrDetails::getBuildingName, bo.getBuildingName());
        lqw.eq(StringUtils.isNotBlank(bo.getTitleCertificateNumber()), DataOcrDetails::getTitleCertificateNumber, bo.getTitleCertificateNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getAreaUnit()), DataOcrDetails::getAreaUnit, bo.getAreaUnit());
        lqw.eq(StringUtils.isNotBlank(bo.getTransportType()), DataOcrDetails::getTransportType, bo.getTransportType());
        lqw.eq(StringUtils.isNotBlank(bo.getTransportNumber()), DataOcrDetails::getTransportNumber, bo.getTransportNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getFrom()), DataOcrDetails::getFrom, bo.getFrom());
        lqw.eq(StringUtils.isNotBlank(bo.getTo()), DataOcrDetails::getTo, bo.getTo());
        lqw.like(StringUtils.isNotBlank(bo.getGoodsName()), DataOcrDetails::getGoodsName, bo.getGoodsName());
        lqw.eq(StringUtils.isNotBlank(bo.getPassenger()), DataOcrDetails::getPassenger, bo.getPassenger());
        lqw.eq(StringUtils.isNotBlank(bo.getTravelDate()), DataOcrDetails::getTravelDate, bo.getTravelDate());
        lqw.eq(StringUtils.isNotBlank(bo.getSeat()), DataOcrDetails::getSeat, bo.getSeat());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataOcrDetails::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增增值税发票明细
     *
     * @param bo 增值税发票明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataOcrDetailsBo bo) {
        DataOcrDetails add = MapstructUtils.convert(bo, DataOcrDetails.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 新增增值税发票明细
     *
     * @param dataOcrDetails 增值税发票明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insert(DataOcrDetails dataOcrDetails) {
        validEntityBeforeSave(dataOcrDetails);
        boolean flag = baseMapper.insert(dataOcrDetails) > 0;
        return flag;
    }

    /**
     * 批量新增增值税发票明细
     *
     * @param dataOcrDetails 增值税发票明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertBatch(List<DataOcrDetails> dataOcrDetails) {
        return baseMapper.insertBatch(dataOcrDetails);
    }

    /**
     * 修改增值税发票明细
     *
     * @param bo 增值税发票明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataOcrDetailsBo bo) {
        DataOcrDetails update = MapstructUtils.convert(bo, DataOcrDetails.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataOcrDetails entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除增值税发票明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
