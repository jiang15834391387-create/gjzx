package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataDidiItineraryDetailsBo;
import org.smartlink.business.domain.vo.DataDidiItineraryDetailsVo;
import org.smartlink.business.domain.DataDidiItineraryDetails;
import org.smartlink.business.mapper.DataDidiItineraryDetailsMapper;
import org.smartlink.business.service.IDataDidiItineraryDetailsService;
import org.smartlink.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 滴滴行程单明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataDidiItineraryDetailsServiceImpl implements IDataDidiItineraryDetailsService {

    private final DataDidiItineraryDetailsMapper baseMapper;

    /**
     * 查询滴滴行程单明细
     *
     * @param id 主键
     * @return 滴滴行程单明细
     */
    @Override
    public DataDidiItineraryDetailsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询滴滴行程单明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 滴滴行程单明细分页列表
     */
    @Override
    public TableDataInfo<DataDidiItineraryDetailsVo> queryPageList(DataDidiItineraryDetailsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataDidiItineraryDetails> lqw = buildQueryWrapper(bo);
        Page<DataDidiItineraryDetailsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的滴滴行程单明细列表
     *
     * @param bo 查询条件
     * @return 滴滴行程单明细列表
     */
    @Override
    public List<DataDidiItineraryDetailsVo> queryList(DataDidiItineraryDetailsBo bo) {
        LambdaQueryWrapper<DataDidiItineraryDetails> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataDidiItineraryDetails> buildQueryWrapper(DataDidiItineraryDetailsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataDidiItineraryDetails> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataDidiItineraryDetails::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataDidiItineraryDetails::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCarType()), DataDidiItineraryDetails::getCarType, bo.getCarType());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataDidiItineraryDetails::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getMileage()), DataDidiItineraryDetails::getMileage, bo.getMileage());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOff()), DataDidiItineraryDetails::getStationGetOff, bo.getStationGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOn()), DataDidiItineraryDetails::getStationGetOn, bo.getStationGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeOrder()), DataDidiItineraryDetails::getTimeOrder, bo.getTimeOrder());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeGetOn()), DataDidiItineraryDetails::getTimeGetOn, bo.getTimeGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeGetOff()), DataDidiItineraryDetails::getTimeGetOff, bo.getTimeGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getProducer()), DataDidiItineraryDetails::getProducer, bo.getProducer());
        lqw.eq(bo.getInvoiceTotal() != null, DataDidiItineraryDetails::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataDidiItineraryDetails::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataDidiItineraryDetails::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增滴滴行程单明细
     *
     * @param bo 滴滴行程单明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataDidiItineraryDetailsBo bo) {
        DataDidiItineraryDetails add = MapstructUtils.convert(bo, DataDidiItineraryDetails.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改滴滴行程单明细
     *
     * @param bo 滴滴行程单明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataDidiItineraryDetailsBo bo) {
        DataDidiItineraryDetails update = MapstructUtils.convert(bo, DataDidiItineraryDetails.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataDidiItineraryDetails entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除滴滴行程单明细信息
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
