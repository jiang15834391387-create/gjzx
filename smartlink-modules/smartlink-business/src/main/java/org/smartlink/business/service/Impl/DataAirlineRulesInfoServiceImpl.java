package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataAirlineRulesInfoBo;
import org.smartlink.business.domain.vo.DataAirlineRulesInfoVo;
import org.smartlink.business.domain.DataAirlineRulesInfo;
import org.smartlink.business.mapper.DataAirlineRulesInfoMapper;
import org.smartlink.business.service.IDataAirlineRulesInfoService;
import org.smartlink.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 飞机票仓位信息Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataAirlineRulesInfoServiceImpl implements IDataAirlineRulesInfoService {

    private final DataAirlineRulesInfoMapper baseMapper;

    /**
     * 查询飞机票仓位信息
     *
     * @param airlineId 主键
     * @return 飞机票仓位信息
     */
    @Override
    public DataAirlineRulesInfoVo queryById(Long airlineId){
        return baseMapper.selectVoById(airlineId);
    }

    /**
     * 分页查询飞机票仓位信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 飞机票仓位信息分页列表
     */
    @Override
    public TableDataInfo<DataAirlineRulesInfoVo> queryPageList(DataAirlineRulesInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataAirlineRulesInfo> lqw = buildQueryWrapper(bo);
        Page<DataAirlineRulesInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的飞机票仓位信息列表
     *
     * @param bo 查询条件
     * @return 飞机票仓位信息列表
     */
    @Override
    public List<DataAirlineRulesInfoVo> queryList(DataAirlineRulesInfoBo bo) {
        LambdaQueryWrapper<DataAirlineRulesInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataAirlineRulesInfo> buildQueryWrapper(DataAirlineRulesInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataAirlineRulesInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getAirlineName()), DataAirlineRulesInfo::getAirlineName, bo.getAirlineName());
        lqw.eq(StringUtils.isNotBlank(bo.getAirlineCode()), DataAirlineRulesInfo::getAirlineCode, bo.getAirlineCode());
        lqw.eq(StringUtils.isNotBlank(bo.getAirlineInfoGrade()), DataAirlineRulesInfo::getAirlineInfoGrade, bo.getAirlineInfoGrade());
        lqw.eq(StringUtils.isNotBlank(bo.getAirlineInfoCode()), DataAirlineRulesInfo::getAirlineInfoCode, bo.getAirlineInfoCode());
        lqw.eq(StringUtils.isNotBlank(bo.getAirlineInfoRemarks()), DataAirlineRulesInfo::getAirlineInfoRemarks, bo.getAirlineInfoRemarks());
        lqw.eq(bo.getVersionCode() != null, DataAirlineRulesInfo::getVersionCode, bo.getVersionCode());
        lqw.like(StringUtils.isNotBlank(bo.getVersionName()), DataAirlineRulesInfo::getVersionName, bo.getVersionName());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataAirlineRulesInfo::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增飞机票仓位信息
     *
     * @param bo 飞机票仓位信息
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataAirlineRulesInfoBo bo) {
        DataAirlineRulesInfo add = MapstructUtils.convert(bo, DataAirlineRulesInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setAirlineId(add.getAirlineId());
        }
        return flag;
    }

    /**
     * 修改飞机票仓位信息
     *
     * @param bo 飞机票仓位信息
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataAirlineRulesInfoBo bo) {
        DataAirlineRulesInfo update = MapstructUtils.convert(bo, DataAirlineRulesInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataAirlineRulesInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除飞机票仓位信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteByIds(ids) > 0;
    }
}
