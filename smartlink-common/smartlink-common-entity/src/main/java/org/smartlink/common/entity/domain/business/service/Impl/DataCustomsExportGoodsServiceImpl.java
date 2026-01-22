package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataCustomsExportGoods;
import org.smartlink.common.entity.domain.business.domain.DataCustomsImportGoodsDetail;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsExportGoodsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsExportGoodsVo;
import org.smartlink.common.entity.domain.business.mapper.DataCustomsExportGoodsMapper;
import org.smartlink.common.entity.domain.business.service.IDataCustomsExportGoodsService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 海关出口货物Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataCustomsExportGoodsServiceImpl implements IDataCustomsExportGoodsService {

    private final DataCustomsExportGoodsMapper baseMapper;

    /**
     * 查询海关出口货物
     *
     * @param id 主键
     * @return 海关出口货物
     */
    @Override
    public DataCustomsExportGoodsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insert(DataCustomsExportGoods dataOcrInfo) {
        boolean flag = baseMapper.insert(dataOcrInfo) > 0;
        return flag;
    }

    /**
     * 分页查询海关出口货物列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关出口货物分页列表
     */
    @Override
    public TableDataInfo<DataCustomsExportGoodsVo> queryPageList(DataCustomsExportGoodsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataCustomsExportGoods> lqw = buildQueryWrapper(bo);
        Page<DataCustomsExportGoodsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的海关出口货物列表
     *
     * @param bo 查询条件
     * @return 海关出口货物列表
     */
    @Override
    public List<DataCustomsExportGoodsVo> queryList(DataCustomsExportGoodsBo bo) {
        LambdaQueryWrapper<DataCustomsExportGoods> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DataCustomsExportGoods selectOneByFileId(String fileId) {
        DataCustomsExportGoods result = buildQueryWrapperByFileId(fileId);
        return result;
    }

    private DataCustomsExportGoods buildQueryWrapperByFileId(String fileId) {
        LambdaQueryWrapper<DataCustomsExportGoods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DataCustomsExportGoods::getFileId, fileId); // 使用 Lambda 表达式
        return baseMapper.selectOne(lambdaQueryWrapper); // 返回查询结果
    }

    private LambdaQueryWrapper<DataCustomsExportGoods> buildQueryWrapper(DataCustomsExportGoodsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataCustomsExportGoods> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataCustomsExportGoods::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getAdditionalExpress()), DataCustomsExportGoods::getAdditionalExpress, bo.getAdditionalExpress());
        lqw.eq(StringUtils.isNotBlank(bo.getConfirmOfPayRoyalties()), DataCustomsExportGoods::getConfirmOfPayRoyalties, bo.getConfirmOfPayRoyalties());
        lqw.eq(StringUtils.isNotBlank(bo.getConfirmOfSpecialRelationship()), DataCustomsExportGoods::getConfirmOfSpecialRelationship, bo.getConfirmOfSpecialRelationship());
        lqw.eq(StringUtils.isNotBlank(bo.getConsumptionCompanyCode()), DataCustomsExportGoods::getConsumptionCompanyCode, bo.getConsumptionCompanyCode());
        lqw.like(StringUtils.isNotBlank(bo.getConsumptionCompanyName()), DataCustomsExportGoods::getConsumptionCompanyName, bo.getConsumptionCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getContractNumber()), DataCustomsExportGoods::getContractNumber, bo.getContractNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCustomsNumber()), DataCustomsExportGoods::getCustomsNumber, bo.getCustomsNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getDateOfApplication()), DataCustomsExportGoods::getDateOfApplication, bo.getDateOfApplication());
        lqw.eq(StringUtils.isNotBlank(bo.getDateOfExport()), DataCustomsExportGoods::getDateOfExport, bo.getDateOfExport());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryNumber()), DataCustomsExportGoods::getDeliveryNumber, bo.getDeliveryNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getDepartureCountryCode()), DataCustomsExportGoods::getDepartureCountryCode, bo.getDepartureCountryCode());
        lqw.like(StringUtils.isNotBlank(bo.getDepartureCountryName()), DataCustomsExportGoods::getDepartureCountryName, bo.getDepartureCountryName());
        lqw.eq(StringUtils.isNotBlank(bo.getExecutiveCompanyCode()), DataCustomsExportGoods::getExecutiveCompanyCode, bo.getExecutiveCompanyCode());
        lqw.like(StringUtils.isNotBlank(bo.getExecutiveCompanyName()), DataCustomsExportGoods::getExecutiveCompanyName, bo.getExecutiveCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getFreight()), DataCustomsExportGoods::getFreight, bo.getFreight());
        lqw.eq(StringUtils.isNotBlank(bo.getGrossWeight()), DataCustomsExportGoods::getGrossWeight, bo.getGrossWeight());
        lqw.eq(StringUtils.isNotBlank(bo.getInsurancePremium()), DataCustomsExportGoods::getInsurancePremium, bo.getInsurancePremium());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataCustomsExportGoods::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getKindOfTaxCode()), DataCustomsExportGoods::getKindOfTaxCode, bo.getKindOfTaxCode());
        lqw.like(StringUtils.isNotBlank(bo.getKindOfTaxName()), DataCustomsExportGoods::getKindOfTaxName, bo.getKindOfTaxName());
        lqw.eq(StringUtils.isNotBlank(bo.getMarksAndRemarks()), DataCustomsExportGoods::getMarksAndRemarks, bo.getMarksAndRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getModeOfTransportationCode()), DataCustomsExportGoods::getModeOfTransportationCode, bo.getModeOfTransportationCode());
        lqw.like(StringUtils.isNotBlank(bo.getModeOfTransportationName()), DataCustomsExportGoods::getModeOfTransportationName, bo.getModeOfTransportationName());
        lqw.eq(StringUtils.isNotBlank(bo.getNetWeight()), DataCustomsExportGoods::getNetWeight, bo.getNetWeight());
        lqw.eq(StringUtils.isNotBlank(bo.getNumberOfPackages()), DataCustomsExportGoods::getNumberOfPackages, bo.getNumberOfPackages());
        lqw.eq(StringUtils.isNotBlank(bo.getOverseasConsigneeCode()), DataCustomsExportGoods::getOverseasConsigneeCode, bo.getOverseasConsigneeCode());
        lqw.like(StringUtils.isNotBlank(bo.getOverseasConsigneeName()), DataCustomsExportGoods::getOverseasConsigneeName, bo.getOverseasConsigneeName());
        lqw.eq(StringUtils.isNotBlank(bo.getPortOfExportCode()), DataCustomsExportGoods::getPortOfExportCode, bo.getPortOfExportCode());
        lqw.like(StringUtils.isNotBlank(bo.getPortOfExportName()), DataCustomsExportGoods::getPortOfExportName, bo.getPortOfExportName());
        lqw.eq(StringUtils.isNotBlank(bo.getPreRecordNumber()), DataCustomsExportGoods::getPreRecordNumber, bo.getPreRecordNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getTradeTermsCode()), DataCustomsExportGoods::getTradeTermsCode, bo.getTradeTermsCode());
        lqw.like(StringUtils.isNotBlank(bo.getTradeTermsName()), DataCustomsExportGoods::getTradeTermsName, bo.getTradeTermsName());
        lqw.eq(StringUtils.isNotBlank(bo.getTradingCountryCode()), DataCustomsExportGoods::getTradingCountryCode, bo.getTradingCountryCode());
        lqw.like(StringUtils.isNotBlank(bo.getTradingCountryName()), DataCustomsExportGoods::getTradingCountryName, bo.getTradingCountryName());
        lqw.eq(StringUtils.isNotBlank(bo.getTransportationTools()), DataCustomsExportGoods::getTransportationTools, bo.getTransportationTools());
        lqw.eq(StringUtils.isNotBlank(bo.getModeOfTradeCode()), DataCustomsExportGoods::getModeOfTradeCode, bo.getModeOfTradeCode());
        lqw.like(StringUtils.isNotBlank(bo.getModeOfTradeName()), DataCustomsExportGoods::getModeOfTradeName, bo.getModeOfTradeName());
        lqw.eq(StringUtils.isNotBlank(bo.getAttachmentsAndNumbers()), DataCustomsExportGoods::getAttachmentsAndNumbers, bo.getAttachmentsAndNumbers());
        lqw.eq(StringUtils.isNotBlank(bo.getFilingEntity()), DataCustomsExportGoods::getFilingEntity, bo.getFilingEntity());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataCustomsExportGoods::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataCustomsExportGoods::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataCustomsExportGoods::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增海关出口货物
     *
     * @param bo 海关出口货物
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataCustomsExportGoodsBo bo) {
        DataCustomsExportGoods add = MapstructUtils.convert(bo, DataCustomsExportGoods.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改海关出口货物
     *
     * @param bo 海关出口货物
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataCustomsExportGoodsBo bo) {
        DataCustomsExportGoods update = MapstructUtils.convert(bo, DataCustomsExportGoods.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataCustomsExportGoods entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除海关出口货物信息
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
