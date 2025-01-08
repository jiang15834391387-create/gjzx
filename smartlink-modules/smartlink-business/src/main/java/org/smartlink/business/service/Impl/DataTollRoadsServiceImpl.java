package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.business.domain.bo.DataTollRoadsBo;
import org.smartlink.business.domain.vo.DataTollRoadsVo;
import org.smartlink.business.domain.DataTollRoads;
import org.smartlink.business.mapper.DataTollRoadsMapper;
import org.smartlink.business.service.IDataTollRoadsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 过路费Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataTollRoadsServiceImpl implements IDataTollRoadsService {

    private final DataTollRoadsMapper baseMapper;

    /**
     * 查询过路费
     *
     * @param id 主键
     * @return 过路费
     */
    @Override
    public DataTollRoadsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询过路费列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 过路费分页列表
     */
    @Override
    public TableDataInfo<DataTollRoadsVo> queryPageList(DataTollRoadsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataTollRoads> lqw = buildQueryWrapper(bo);
        Page<DataTollRoadsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的过路费列表
     *
     * @param bo 查询条件
     * @return 过路费列表
     */
    @Override
    public List<DataTollRoadsVo> queryList(DataTollRoadsBo bo) {
        LambdaQueryWrapper<DataTollRoads> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataTollRoads> buildQueryWrapper(DataTollRoadsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataTollRoads> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataTollRoads::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataTollRoads::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataTollRoads::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getEntrance()), DataTollRoads::getEntrance, bo.getEntrance());
        lqw.eq(StringUtils.isNotBlank(bo.getExit()), DataTollRoads::getExit, bo.getExit());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataTollRoads::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataTollRoads::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTime()), DataTollRoads::getInvoiceTime, bo.getInvoiceTime());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataTollRoads::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataTollRoads::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getHighwayFlag()), DataTollRoads::getHighwayFlag, bo.getHighwayFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataTollRoads::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataTollRoads::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增过路费
     *
     * @param bo 过路费
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataTollRoadsBo bo) {
        DataTollRoads add = MapstructUtils.convert(bo, DataTollRoads.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改过路费
     *
     * @param bo 过路费
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataTollRoadsBo bo) {
        DataTollRoads update = MapstructUtils.convert(bo, DataTollRoads.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataTollRoads entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除过路费信息
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
