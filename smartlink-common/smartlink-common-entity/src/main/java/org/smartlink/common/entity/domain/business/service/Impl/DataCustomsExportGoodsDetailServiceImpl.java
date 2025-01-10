package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataCustomsExportGoodsDetail;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsExportGoodsDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsExportGoodsDetailVo;
import org.smartlink.common.entity.domain.business.mapper.DataCustomsExportGoodsDetailMapper;
import org.smartlink.common.entity.domain.business.service.IDataCustomsExportGoodsDetailService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 海关出口货物明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataCustomsExportGoodsDetailServiceImpl implements IDataCustomsExportGoodsDetailService {

    private final DataCustomsExportGoodsDetailMapper baseMapper;

    /**
     * 查询海关出口货物明细
     *
     * @param id 主键
     * @return 海关出口货物明细
     */
    @Override
    public DataCustomsExportGoodsDetailVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询海关出口货物明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关出口货物明细分页列表
     */
    @Override
    public TableDataInfo<DataCustomsExportGoodsDetailVo> queryPageList(DataCustomsExportGoodsDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataCustomsExportGoodsDetail> lqw = buildQueryWrapper(bo);
        Page<DataCustomsExportGoodsDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的海关出口货物明细列表
     *
     * @param bo 查询条件
     * @return 海关出口货物明细列表
     */
    @Override
    public List<DataCustomsExportGoodsDetailVo> queryList(DataCustomsExportGoodsDetailBo bo) {
        LambdaQueryWrapper<DataCustomsExportGoodsDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataCustomsExportGoodsDetail> buildQueryWrapper(DataCustomsExportGoodsDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataCustomsExportGoodsDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataCustomsExportGoodsDetail::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getCommodityNumber()), DataCustomsExportGoodsDetail::getCommodityNumber, bo.getCommodityNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCurrency()), DataCustomsExportGoodsDetail::getCurrency, bo.getCurrency());
        lqw.eq(StringUtils.isNotBlank(bo.getDescriptionOfCommodity()), DataCustomsExportGoodsDetail::getDescriptionOfCommodity, bo.getDescriptionOfCommodity());
        lqw.eq(StringUtils.isNotBlank(bo.getFinalDestinationCountry()), DataCustomsExportGoodsDetail::getFinalDestinationCountry, bo.getFinalDestinationCountry());
        lqw.eq(StringUtils.isNotBlank(bo.getItemNumber()), DataCustomsExportGoodsDetail::getItemNumber, bo.getItemNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getKindOfTax()), DataCustomsExportGoodsDetail::getKindOfTax, bo.getKindOfTax());
        lqw.eq(StringUtils.isNotBlank(bo.getOriginalCountry()), DataCustomsExportGoodsDetail::getOriginalCountry, bo.getOriginalCountry());
        lqw.eq(StringUtils.isNotBlank(bo.getOriginalPlaceOfDeliveredGoods()), DataCustomsExportGoodsDetail::getOriginalPlaceOfDeliveredGoods, bo.getOriginalPlaceOfDeliveredGoods());
        lqw.eq(StringUtils.isNotBlank(bo.getQuantityOf2Uom()), DataCustomsExportGoodsDetail::getQuantityOf2Uom, bo.getQuantityOf2Uom());
        lqw.eq(StringUtils.isNotBlank(bo.getQuantityOfUom()), DataCustomsExportGoodsDetail::getQuantityOfUom, bo.getQuantityOfUom());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecification()), DataCustomsExportGoodsDetail::getSpecification, bo.getSpecification());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalPrice()), DataCustomsExportGoodsDetail::getTotalPrice, bo.getTotalPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getTransactionUomAndQuantity()), DataCustomsExportGoodsDetail::getTransactionUomAndQuantity, bo.getTransactionUomAndQuantity());
        lqw.eq(StringUtils.isNotBlank(bo.getUnitPrice()), DataCustomsExportGoodsDetail::getUnitPrice, bo.getUnitPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataCustomsExportGoodsDetail::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增海关出口货物明细
     *
     * @param bo 海关出口货物明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataCustomsExportGoodsDetailBo bo) {
        DataCustomsExportGoodsDetail add = MapstructUtils.convert(bo, DataCustomsExportGoodsDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改海关出口货物明细
     *
     * @param bo 海关出口货物明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataCustomsExportGoodsDetailBo bo) {
        DataCustomsExportGoodsDetail update = MapstructUtils.convert(bo, DataCustomsExportGoodsDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataCustomsExportGoodsDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除海关出口货物明细信息
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
