package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataDidiItineraryBo;
import org.smartlink.business.domain.vo.DataDidiItineraryVo;
import org.smartlink.business.domain.DataDidiItinerary;
import org.smartlink.business.mapper.DataDidiItineraryMapper;
import org.smartlink.business.service.IDataDidiItineraryService;
import org.smartlink.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 滴滴行程单Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataDidiItineraryServiceImpl implements IDataDidiItineraryService {

    private final DataDidiItineraryMapper baseMapper;

    /**
     * 查询滴滴行程单
     *
     * @param id 主键
     * @return 滴滴行程单
     */
    @Override
    public DataDidiItineraryVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询滴滴行程单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 滴滴行程单分页列表
     */
    @Override
    public TableDataInfo<DataDidiItineraryVo> queryPageList(DataDidiItineraryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataDidiItinerary> lqw = buildQueryWrapper(bo);
        Page<DataDidiItineraryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的滴滴行程单列表
     *
     * @param bo 查询条件
     * @return 滴滴行程单列表
     */
    @Override
    public List<DataDidiItineraryVo> queryList(DataDidiItineraryBo bo) {
        LambdaQueryWrapper<DataDidiItinerary> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataDidiItinerary> buildQueryWrapper(DataDidiItineraryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataDidiItinerary> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataDidiItinerary::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataDidiItinerary::getTitle, bo.getTitle());
        lqw.eq(bo.getInvoiceDate() != null, DataDidiItinerary::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeGetOff()), DataDidiItinerary::getTimeGetOff, bo.getTimeGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeGetOn()), DataDidiItinerary::getTimeGetOn, bo.getTimeGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), DataDidiItinerary::getPhone, bo.getPhone());
        lqw.eq(bo.getInvoiceTotal() != null, DataDidiItinerary::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataDidiItinerary::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataDidiItinerary::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataDidiItinerary::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataDidiItinerary::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckResult()), DataDidiItinerary::getCheckResult, bo.getCheckResult());
        lqw.eq(StringUtils.isNotBlank(bo.getPushBusinessInfoFlag()), DataDidiItinerary::getPushBusinessInfoFlag, bo.getPushBusinessInfoFlag());
        return lqw;
    }

    /**
     * 新增滴滴行程单
     *
     * @param bo 滴滴行程单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataDidiItineraryBo bo) {
        DataDidiItinerary add = MapstructUtils.convert(bo, DataDidiItinerary.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改滴滴行程单
     *
     * @param bo 滴滴行程单
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataDidiItineraryBo bo) {
        DataDidiItinerary update = MapstructUtils.convert(bo, DataDidiItinerary.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataDidiItinerary entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除滴滴行程单信息
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
