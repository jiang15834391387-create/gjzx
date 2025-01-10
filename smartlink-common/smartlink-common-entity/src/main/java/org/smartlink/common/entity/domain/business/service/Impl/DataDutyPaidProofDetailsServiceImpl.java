package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataDutyPaidProofDetails;
import org.smartlink.common.entity.domain.business.domain.bo.DataDutyPaidProofDetailsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDutyPaidProofDetailsVo;
import org.smartlink.common.entity.domain.business.mapper.DataDutyPaidProofDetailsMapper;
import org.smartlink.common.entity.domain.business.service.IDataDutyPaidProofDetailsService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 完税证明明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataDutyPaidProofDetailsServiceImpl implements IDataDutyPaidProofDetailsService {

    private final DataDutyPaidProofDetailsMapper baseMapper;

    /**
     * 查询完税证明明细
     *
     * @param id 主键
     * @return 完税证明明细
     */
    @Override
    public DataDutyPaidProofDetailsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询完税证明明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 完税证明明细分页列表
     */
    @Override
    public TableDataInfo<DataDutyPaidProofDetailsVo> queryPageList(DataDutyPaidProofDetailsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataDutyPaidProofDetails> lqw = buildQueryWrapper(bo);
        Page<DataDutyPaidProofDetailsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的完税证明明细列表
     *
     * @param bo 查询条件
     * @return 完税证明明细列表
     */
    @Override
    public List<DataDutyPaidProofDetailsVo> queryList(DataDutyPaidProofDetailsBo bo) {
        LambdaQueryWrapper<DataDutyPaidProofDetails> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataDutyPaidProofDetails> buildQueryWrapper(DataDutyPaidProofDetailsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataDutyPaidProofDetails> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataDutyPaidProofDetails::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getEntryDate()), DataDutyPaidProofDetails::getEntryDate, bo.getEntryDate());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxAgency()), DataDutyPaidProofDetails::getTaxAgency, bo.getTaxAgency());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxPeriod()), DataDutyPaidProofDetails::getTaxPeriod, bo.getTaxPeriod());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxType()), DataDutyPaidProofDetails::getTaxType, bo.getTaxType());
        lqw.eq(StringUtils.isNotBlank(bo.getBudgetAccountCode()), DataDutyPaidProofDetails::getBudgetAccountCode, bo.getBudgetAccountCode());
        lqw.like(StringUtils.isNotBlank(bo.getBudgetAccountName()), DataDutyPaidProofDetails::getBudgetAccountName, bo.getBudgetAccountName());
        lqw.eq(StringUtils.isNotBlank(bo.getBudgetAccountLevel()), DataDutyPaidProofDetails::getBudgetAccountLevel, bo.getBudgetAccountLevel());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DataDutyPaidProofDetails::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getOriginalNumber()), DataDutyPaidProofDetails::getOriginalNumber, bo.getOriginalNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getQuantity()), DataDutyPaidProofDetails::getQuantity, bo.getQuantity());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxRate()), DataDutyPaidProofDetails::getTaxRate, bo.getTaxRate());
        lqw.eq(StringUtils.isNotBlank(bo.getTotal()), DataDutyPaidProofDetails::getTotal, bo.getTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getAmountPaid()), DataDutyPaidProofDetails::getAmountPaid, bo.getAmountPaid());
        lqw.eq(StringUtils.isNotBlank(bo.getActualPaidAmount()), DataDutyPaidProofDetails::getActualPaidAmount, bo.getActualPaidAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataDutyPaidProofDetails::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增完税证明明细
     *
     * @param bo 完税证明明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataDutyPaidProofDetailsBo bo) {
        DataDutyPaidProofDetails add = MapstructUtils.convert(bo, DataDutyPaidProofDetails.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改完税证明明细
     *
     * @param bo 完税证明明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataDutyPaidProofDetailsBo bo) {
        DataDutyPaidProofDetails update = MapstructUtils.convert(bo, DataDutyPaidProofDetails.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataDutyPaidProofDetails entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除完税证明明细信息
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
