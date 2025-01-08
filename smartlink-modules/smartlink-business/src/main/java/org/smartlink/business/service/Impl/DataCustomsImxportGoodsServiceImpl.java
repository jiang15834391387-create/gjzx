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
import org.smartlink.business.domain.bo.DataCustomsImxportGoodsBo;
import org.smartlink.business.domain.vo.DataCustomsImxportGoodsVo;
import org.smartlink.business.domain.DataCustomsImxportGoods;
import org.smartlink.business.mapper.DataCustomsImxportGoodsMapper;
import org.smartlink.business.service.IDataCustomsImxportGoodsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 海关进口货物报关单Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataCustomsImxportGoodsServiceImpl implements IDataCustomsImxportGoodsService {

    private final DataCustomsImxportGoodsMapper baseMapper;

    /**
     * 查询海关进口货物报关单
     *
     * @param id 主键
     * @return 海关进口货物报关单
     */
    @Override
    public DataCustomsImxportGoodsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询海关进口货物报关单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关进口货物报关单分页列表
     */
    @Override
    public TableDataInfo<DataCustomsImxportGoodsVo> queryPageList(DataCustomsImxportGoodsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataCustomsImxportGoods> lqw = buildQueryWrapper(bo);
        Page<DataCustomsImxportGoodsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的海关进口货物报关单列表
     *
     * @param bo 查询条件
     * @return 海关进口货物报关单列表
     */
    @Override
    public List<DataCustomsImxportGoodsVo> queryList(DataCustomsImxportGoodsBo bo) {
        LambdaQueryWrapper<DataCustomsImxportGoods> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataCustomsImxportGoods> buildQueryWrapper(DataCustomsImxportGoodsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataCustomsImxportGoods> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataCustomsImxportGoods::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getAdditionalExpress()), DataCustomsImxportGoods::getAdditionalExpress, bo.getAdditionalExpress());
        lqw.eq(StringUtils.isNotBlank(bo.getConfirmOfPayRoyalties()), DataCustomsImxportGoods::getConfirmOfPayRoyalties, bo.getConfirmOfPayRoyalties());
        lqw.eq(StringUtils.isNotBlank(bo.getConfirmOfSpecialRelationship()), DataCustomsImxportGoods::getConfirmOfSpecialRelationship, bo.getConfirmOfSpecialRelationship());
        lqw.eq(StringUtils.isNotBlank(bo.getConsumptionCompanyCode()), DataCustomsImxportGoods::getConsumptionCompanyCode, bo.getConsumptionCompanyCode());
        lqw.like(StringUtils.isNotBlank(bo.getConsumptionCompanyName()), DataCustomsImxportGoods::getConsumptionCompanyName, bo.getConsumptionCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getContractNumber()), DataCustomsImxportGoods::getContractNumber, bo.getContractNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCustomsNumber()), DataCustomsImxportGoods::getCustomsNumber, bo.getCustomsNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getDateOfApplication()), DataCustomsImxportGoods::getDateOfApplication, bo.getDateOfApplication());
        lqw.eq(StringUtils.isNotBlank(bo.getDateOfImport()), DataCustomsImxportGoods::getDateOfImport, bo.getDateOfImport());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryNumber()), DataCustomsImxportGoods::getDeliveryNumber, bo.getDeliveryNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getDepartureCountryCode()), DataCustomsImxportGoods::getDepartureCountryCode, bo.getDepartureCountryCode());
        lqw.like(StringUtils.isNotBlank(bo.getDepartureCountryName()), DataCustomsImxportGoods::getDepartureCountryName, bo.getDepartureCountryName());
        lqw.eq(StringUtils.isNotBlank(bo.getExecutiveCompanyCode()), DataCustomsImxportGoods::getExecutiveCompanyCode, bo.getExecutiveCompanyCode());
        lqw.like(StringUtils.isNotBlank(bo.getExecutiveCompanyName()), DataCustomsImxportGoods::getExecutiveCompanyName, bo.getExecutiveCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getFreight()), DataCustomsImxportGoods::getFreight, bo.getFreight());
        lqw.eq(StringUtils.isNotBlank(bo.getGrossWeight()), DataCustomsImxportGoods::getGrossWeight, bo.getGrossWeight());
        lqw.eq(StringUtils.isNotBlank(bo.getInsurancePremium()), DataCustomsImxportGoods::getInsurancePremium, bo.getInsurancePremium());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataCustomsImxportGoods::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getKindOfTaxCode()), DataCustomsImxportGoods::getKindOfTaxCode, bo.getKindOfTaxCode());
        lqw.like(StringUtils.isNotBlank(bo.getKindOfTaxName()), DataCustomsImxportGoods::getKindOfTaxName, bo.getKindOfTaxName());
        lqw.eq(StringUtils.isNotBlank(bo.getMarksAndRemarks()), DataCustomsImxportGoods::getMarksAndRemarks, bo.getMarksAndRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getModeOfTransportationCode()), DataCustomsImxportGoods::getModeOfTransportationCode, bo.getModeOfTransportationCode());
        lqw.like(StringUtils.isNotBlank(bo.getModeOfTransportationName()), DataCustomsImxportGoods::getModeOfTransportationName, bo.getModeOfTransportationName());
        lqw.eq(StringUtils.isNotBlank(bo.getNetWeight()), DataCustomsImxportGoods::getNetWeight, bo.getNetWeight());
        lqw.eq(StringUtils.isNotBlank(bo.getNumberOfPackages()), DataCustomsImxportGoods::getNumberOfPackages, bo.getNumberOfPackages());
        lqw.eq(StringUtils.isNotBlank(bo.getOverseasConsigneeCode()), DataCustomsImxportGoods::getOverseasConsigneeCode, bo.getOverseasConsigneeCode());
        lqw.like(StringUtils.isNotBlank(bo.getOverseasConsigneeName()), DataCustomsImxportGoods::getOverseasConsigneeName, bo.getOverseasConsigneeName());
        lqw.eq(StringUtils.isNotBlank(bo.getPortOfImportCode()), DataCustomsImxportGoods::getPortOfImportCode, bo.getPortOfImportCode());
        lqw.like(StringUtils.isNotBlank(bo.getPortOfImportName()), DataCustomsImxportGoods::getPortOfImportName, bo.getPortOfImportName());
        lqw.eq(StringUtils.isNotBlank(bo.getPreRecordNumber()), DataCustomsImxportGoods::getPreRecordNumber, bo.getPreRecordNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getTradeTermsCode()), DataCustomsImxportGoods::getTradeTermsCode, bo.getTradeTermsCode());
        lqw.like(StringUtils.isNotBlank(bo.getTradeTermsName()), DataCustomsImxportGoods::getTradeTermsName, bo.getTradeTermsName());
        lqw.eq(StringUtils.isNotBlank(bo.getTradingCountryCode()), DataCustomsImxportGoods::getTradingCountryCode, bo.getTradingCountryCode());
        lqw.like(StringUtils.isNotBlank(bo.getTradingCountryName()), DataCustomsImxportGoods::getTradingCountryName, bo.getTradingCountryName());
        lqw.eq(StringUtils.isNotBlank(bo.getTransportationTools()), DataCustomsImxportGoods::getTransportationTools, bo.getTransportationTools());
        lqw.eq(StringUtils.isNotBlank(bo.getModeOfTradeCode()), DataCustomsImxportGoods::getModeOfTradeCode, bo.getModeOfTradeCode());
        lqw.like(StringUtils.isNotBlank(bo.getModeOfTradeName()), DataCustomsImxportGoods::getModeOfTradeName, bo.getModeOfTradeName());
        lqw.eq(StringUtils.isNotBlank(bo.getAttachmentsAndNumbers()), DataCustomsImxportGoods::getAttachmentsAndNumbers, bo.getAttachmentsAndNumbers());
        lqw.eq(StringUtils.isNotBlank(bo.getFilingEntity()), DataCustomsImxportGoods::getFilingEntity, bo.getFilingEntity());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataCustomsImxportGoods::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataCustomsImxportGoods::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataCustomsImxportGoods::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增海关进口货物报关单
     *
     * @param bo 海关进口货物报关单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataCustomsImxportGoodsBo bo) {
        DataCustomsImxportGoods add = MapstructUtils.convert(bo, DataCustomsImxportGoods.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改海关进口货物报关单
     *
     * @param bo 海关进口货物报关单
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataCustomsImxportGoodsBo bo) {
        DataCustomsImxportGoods update = MapstructUtils.convert(bo, DataCustomsImxportGoods.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataCustomsImxportGoods entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除海关进口货物报关单信息
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
