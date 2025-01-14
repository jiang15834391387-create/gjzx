package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataFlightsItineraryDetail;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatmentDetail;
import org.smartlink.common.entity.domain.business.domain.bo.DataFlightsItineraryDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataFlightsItineraryDetailVo;
import org.smartlink.common.entity.domain.business.mapper.DataFlightsItineraryDetailMapper;
import org.smartlink.common.entity.domain.business.service.IDataFlightsItineraryDetailService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 航空电子行程单明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataFlightsItineraryDetailServiceImpl implements IDataFlightsItineraryDetailService {

    private final DataFlightsItineraryDetailMapper baseMapper;

    /**
     * 查询航空电子行程单明细
     *
     * @param id 主键
     * @return 航空电子行程单明细
     */
    @Override
    public DataFlightsItineraryDetailVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insertBatch(List<DataFlightsItineraryDetail> dataFlightsItineraryDetails) {
        return baseMapper.insertBatch(dataFlightsItineraryDetails);
    }

    /**
     * 分页查询航空电子行程单明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 航空电子行程单明细分页列表
     */
    @Override
    public TableDataInfo<DataFlightsItineraryDetailVo> queryPageList(DataFlightsItineraryDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataFlightsItineraryDetail> lqw = buildQueryWrapper(bo);
        Page<DataFlightsItineraryDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的航空电子行程单明细列表
     *
     * @param bo 查询条件
     * @return 航空电子行程单明细列表
     */
    @Override
    public List<DataFlightsItineraryDetailVo> queryList(DataFlightsItineraryDetailBo bo) {
        LambdaQueryWrapper<DataFlightsItineraryDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DataFlightsItineraryDetail selectOneByFileId(String fileId) {
        DataFlightsItineraryDetail result = buildQueryWrapperByFileId(fileId);
        return result;
    }

    private DataFlightsItineraryDetail buildQueryWrapperByFileId(String fileId) {
        LambdaQueryWrapper<DataFlightsItineraryDetail> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DataFlightsItineraryDetail::getFileId, fileId); // 使用 Lambda 表达式
        return baseMapper.selectOne(lambdaQueryWrapper); // 返回查询结果
    }

    private LambdaQueryWrapper<DataFlightsItineraryDetail> buildQueryWrapper(DataFlightsItineraryDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataFlightsItineraryDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCarrier()), DataFlightsItineraryDetail::getCarrier, bo.getCarrier());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataFlightsItineraryDetail::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataFlightsItineraryDetail::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getFlightNumber()), DataFlightsItineraryDetail::getFlightNumber, bo.getFlightNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOn()), DataFlightsItineraryDetail::getStationGetOn, bo.getStationGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getSeat()), DataFlightsItineraryDetail::getSeat, bo.getSeat());
        lqw.eq(StringUtils.isNotBlank(bo.getEffectiveDate()), DataFlightsItineraryDetail::getEffectiveDate, bo.getEffectiveDate());
        lqw.eq(StringUtils.isNotBlank(bo.getExpiryDate()), DataFlightsItineraryDetail::getExpiryDate, bo.getExpiryDate());
        lqw.eq(StringUtils.isNotBlank(bo.getOcrId()), DataFlightsItineraryDetail::getOcrId, bo.getOcrId());
        lqw.eq(StringUtils.isNotBlank(bo.getSpaceLevel()), DataFlightsItineraryDetail::getSpaceLevel, bo.getSpaceLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getAllow()), DataFlightsItineraryDetail::getAllow, bo.getAllow());
        lqw.eq(StringUtils.isNotBlank(bo.getFareBasis()), DataFlightsItineraryDetail::getFareBasis, bo.getFareBasis());
        lqw.eq(StringUtils.isNotBlank(bo.getUserId()), DataFlightsItineraryDetail::getUserId, bo.getUserId());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), DataFlightsItineraryDetail::getUserName, bo.getUserName());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTime()), DataFlightsItineraryDetail::getInvoiceTime, bo.getInvoiceTime());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOff()), DataFlightsItineraryDetail::getStationGetOff, bo.getStationGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getFlightSegment()), DataFlightsItineraryDetail::getFlightSegment, bo.getFlightSegment());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataFlightsItineraryDetail::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增航空电子行程单明细
     *
     * @param bo 航空电子行程单明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataFlightsItineraryDetailBo bo) {
        DataFlightsItineraryDetail add = MapstructUtils.convert(bo, DataFlightsItineraryDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改航空电子行程单明细
     *
     * @param bo 航空电子行程单明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataFlightsItineraryDetailBo bo) {
        DataFlightsItineraryDetail update = MapstructUtils.convert(bo, DataFlightsItineraryDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataFlightsItineraryDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除航空电子行程单明细信息
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
