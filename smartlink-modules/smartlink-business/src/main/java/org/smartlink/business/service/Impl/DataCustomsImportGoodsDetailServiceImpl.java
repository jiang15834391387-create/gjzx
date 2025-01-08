package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataCustomsImportGoodsDetailBo;
import org.smartlink.business.domain.vo.DataCustomsImportGoodsDetailVo;
import org.smartlink.business.domain.DataCustomsImportGoodsDetail;
import org.smartlink.business.mapper.DataCustomsImportGoodsDetailMapper;
import org.smartlink.business.service.IDataCustomsImportGoodsDetailService;
import org.smartlink.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 海关进口货物明细Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataCustomsImportGoodsDetailServiceImpl implements IDataCustomsImportGoodsDetailService {

    private final DataCustomsImportGoodsDetailMapper baseMapper;

    /**
     * 查询海关进口货物明细
     *
     * @param id 主键
     * @return 海关进口货物明细
     */
    @Override
    public DataCustomsImportGoodsDetailVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询海关进口货物明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关进口货物明细分页列表
     */
    @Override
    public TableDataInfo<DataCustomsImportGoodsDetailVo> queryPageList(DataCustomsImportGoodsDetailBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataCustomsImportGoodsDetail> lqw = buildQueryWrapper(bo);
        Page<DataCustomsImportGoodsDetailVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的海关进口货物明细列表
     *
     * @param bo 查询条件
     * @return 海关进口货物明细列表
     */
    @Override
    public List<DataCustomsImportGoodsDetailVo> queryList(DataCustomsImportGoodsDetailBo bo) {
        LambdaQueryWrapper<DataCustomsImportGoodsDetail> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataCustomsImportGoodsDetail> buildQueryWrapper(DataCustomsImportGoodsDetailBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataCustomsImportGoodsDetail> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataCustomsImportGoodsDetail::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getCommodityNumber()), DataCustomsImportGoodsDetail::getCommodityNumber, bo.getCommodityNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCurrency()), DataCustomsImportGoodsDetail::getCurrency, bo.getCurrency());
        lqw.eq(StringUtils.isNotBlank(bo.getDescriptionOfCommodity()), DataCustomsImportGoodsDetail::getDescriptionOfCommodity, bo.getDescriptionOfCommodity());
        lqw.eq(StringUtils.isNotBlank(bo.getDomesticDestinationPlace()), DataCustomsImportGoodsDetail::getDomesticDestinationPlace, bo.getDomesticDestinationPlace());
        lqw.eq(StringUtils.isNotBlank(bo.getFinalDestinationCountry()), DataCustomsImportGoodsDetail::getFinalDestinationCountry, bo.getFinalDestinationCountry());
        lqw.eq(StringUtils.isNotBlank(bo.getItemNumber()), DataCustomsImportGoodsDetail::getItemNumber, bo.getItemNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getKindOfTax()), DataCustomsImportGoodsDetail::getKindOfTax, bo.getKindOfTax());
        lqw.eq(StringUtils.isNotBlank(bo.getOriginalCountry()), DataCustomsImportGoodsDetail::getOriginalCountry, bo.getOriginalCountry());
        lqw.eq(StringUtils.isNotBlank(bo.getOriginalPlaceOfDeliveredGoods()), DataCustomsImportGoodsDetail::getOriginalPlaceOfDeliveredGoods, bo.getOriginalPlaceOfDeliveredGoods());
        lqw.eq(StringUtils.isNotBlank(bo.getQuantityOf2Uom()), DataCustomsImportGoodsDetail::getQuantityOf2Uom, bo.getQuantityOf2Uom());
        lqw.eq(StringUtils.isNotBlank(bo.getQuantityOfUom()), DataCustomsImportGoodsDetail::getQuantityOfUom, bo.getQuantityOfUom());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecification()), DataCustomsImportGoodsDetail::getSpecification, bo.getSpecification());
        lqw.eq(bo.getTotalPrice() != null, DataCustomsImportGoodsDetail::getTotalPrice, bo.getTotalPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getTransactionUomAndQuantity()), DataCustomsImportGoodsDetail::getTransactionUomAndQuantity, bo.getTransactionUomAndQuantity());
        lqw.eq(bo.getUnitPrice() != null, DataCustomsImportGoodsDetail::getUnitPrice, bo.getUnitPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataCustomsImportGoodsDetail::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataCustomsImportGoodsDetail::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckResult()), DataCustomsImportGoodsDetail::getCheckResult, bo.getCheckResult());
        lqw.eq(StringUtils.isNotBlank(bo.getPushBusinessInfoFlag()), DataCustomsImportGoodsDetail::getPushBusinessInfoFlag, bo.getPushBusinessInfoFlag());
        return lqw;
    }

    /**
     * 新增海关进口货物明细
     *
     * @param bo 海关进口货物明细
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataCustomsImportGoodsDetailBo bo) {
        DataCustomsImportGoodsDetail add = MapstructUtils.convert(bo, DataCustomsImportGoodsDetail.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改海关进口货物明细
     *
     * @param bo 海关进口货物明细
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataCustomsImportGoodsDetailBo bo) {
        DataCustomsImportGoodsDetail update = MapstructUtils.convert(bo, DataCustomsImportGoodsDetail.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataCustomsImportGoodsDetail entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除海关进口货物明细信息
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
