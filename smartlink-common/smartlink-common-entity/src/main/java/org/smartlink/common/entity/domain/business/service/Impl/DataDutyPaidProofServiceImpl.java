package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataDutyPaidProof;
import org.smartlink.common.entity.domain.business.domain.bo.DataDutyPaidProofBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDutyPaidProofVo;
import org.smartlink.common.entity.domain.business.mapper.DataDutyPaidProofMapper;
import org.smartlink.common.entity.domain.business.service.IDataDutyPaidProofService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 完税证明Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataDutyPaidProofServiceImpl implements IDataDutyPaidProofService {

    private final DataDutyPaidProofMapper baseMapper;

    /**
     * 查询完税证明
     *
     * @param id 主键
     * @return 完税证明
     */
    @Override
    public DataDutyPaidProofVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询完税证明列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 完税证明分页列表
     */
    @Override
    public TableDataInfo<DataDutyPaidProofVo> queryPageList(DataDutyPaidProofBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataDutyPaidProof> lqw = buildQueryWrapper(bo);
        Page<DataDutyPaidProofVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的完税证明列表
     *
     * @param bo 查询条件
     * @return 完税证明列表
     */
    @Override
    public List<DataDutyPaidProofVo> queryList(DataDutyPaidProofBo bo) {
        LambdaQueryWrapper<DataDutyPaidProof> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataDutyPaidProof> buildQueryWrapper(DataDutyPaidProofBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataDutyPaidProof> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataDutyPaidProof::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataDutyPaidProof::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getSerialNumber()), DataDutyPaidProof::getSerialNumber, bo.getSerialNumber());
        lqw.like(StringUtils.isNotBlank(bo.getBuyerName()), DataDutyPaidProof::getBuyerName, bo.getBuyerName());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerTaxId()), DataDutyPaidProof::getBuyerTaxId, bo.getBuyerTaxId());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataDutyPaidProof::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataDutyPaidProof::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataDutyPaidProof::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalUppercase()), DataDutyPaidProof::getTotalUppercase, bo.getTotalUppercase());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxAuthority()), DataDutyPaidProof::getTaxAuthority, bo.getTaxAuthority());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxAgencyCode()), DataDutyPaidProof::getTaxAgencyCode, bo.getTaxAgencyCode());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerDepositBank()), DataDutyPaidProof::getBuyerDepositBank, bo.getBuyerDepositBank());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerAccount()), DataDutyPaidProof::getBuyerAccount, bo.getBuyerAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxPaymentLimitedTime()), DataDutyPaidProof::getTaxPaymentLimitedTime, bo.getTaxPaymentLimitedTime());
        lqw.eq(StringUtils.isNotBlank(bo.getReceivingTreasury()), DataDutyPaidProof::getReceivingTreasury, bo.getReceivingTreasury());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataDutyPaidProof::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataDutyPaidProof::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增完税证明
     *
     * @param bo 完税证明
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataDutyPaidProofBo bo) {
        DataDutyPaidProof add = MapstructUtils.convert(bo, DataDutyPaidProof.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改完税证明
     *
     * @param bo 完税证明
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataDutyPaidProofBo bo) {
        DataDutyPaidProof update = MapstructUtils.convert(bo, DataDutyPaidProof.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataDutyPaidProof entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除完税证明信息
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
