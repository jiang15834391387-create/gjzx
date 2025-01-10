package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataMedicalTreatmentDetail;
import org.smartlink.common.entity.domain.business.domain.bo.DataMedicalTreatmentDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataMedicalTreatmentDetailVo;
import org.smartlink.common.entity.domain.business.mapper.DataMedicalTreatmentDetailMapper;
import org.smartlink.common.entity.domain.business.service.IDataMedicalTreatmentDetailService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 医疗票明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataMedicalTreatmentDetailServiceImpl implements IDataMedicalTreatmentDetailService {

    private final DataMedicalTreatmentDetailMapper baseMapper;

    /**
     * 查询医疗票明细
     *
     * @param id 主键
     * @return 医疗票明细
     */
    @Override
    public DataMedicalTreatmentDetailVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询医疗票明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 医疗票明细分页列表
     */
    @Override
    public TableDataInfo<DataMedicalTreatmentDetailVo> queryPageList(DataMedicalTreatmentDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataMedicalTreatmentDetail> lqw = buildQueryWrapper(bo);
        Page<DataMedicalTreatmentDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的医疗票明细列表
     *
     * @param bo 查询条件
     * @return 医疗票明细列表
     */
    @Override
    public List<DataMedicalTreatmentDetailVo> queryList(DataMedicalTreatmentDetailBo bo) {
        LambdaQueryWrapper<DataMedicalTreatmentDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataMedicalTreatmentDetail> buildQueryWrapper(DataMedicalTreatmentDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataMedicalTreatmentDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataMedicalTreatmentDetail::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataMedicalTreatmentDetail::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataMedicalTreatmentDetail::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getDate()), DataMedicalTreatmentDetail::getDate, bo.getDate());
        lqw.eq(StringUtils.isNotBlank(bo.getPayer()), DataMedicalTreatmentDetail::getPayer, bo.getPayer());
        lqw.eq(StringUtils.isNotBlank(bo.getSubtotal()), DataMedicalTreatmentDetail::getSubtotal, bo.getSubtotal());
        lqw.eq(StringUtils.isNotBlank(bo.getTotal()), DataMedicalTreatmentDetail::getTotal, bo.getTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getPayee()), DataMedicalTreatmentDetail::getPayee, bo.getPayee());
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), DataMedicalTreatmentDetail::getProjectName, bo.getProjectName());
        lqw.eq(StringUtils.isNotBlank(bo.getQuantity()), DataMedicalTreatmentDetail::getQuantity, bo.getQuantity());
        lqw.eq(StringUtils.isNotBlank(bo.getAmount()), DataMedicalTreatmentDetail::getAmount, bo.getAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getComment()), DataMedicalTreatmentDetail::getComment, bo.getComment());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataMedicalTreatmentDetail::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataMedicalTreatmentDetail::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增医疗票明细
     *
     * @param bo 医疗票明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataMedicalTreatmentDetailBo bo) {
        DataMedicalTreatmentDetail add = MapstructUtils.convert(bo, DataMedicalTreatmentDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改医疗票明细
     *
     * @param bo 医疗票明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataMedicalTreatmentDetailBo bo) {
        DataMedicalTreatmentDetail update = MapstructUtils.convert(bo, DataMedicalTreatmentDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataMedicalTreatmentDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除医疗票明细信息
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
