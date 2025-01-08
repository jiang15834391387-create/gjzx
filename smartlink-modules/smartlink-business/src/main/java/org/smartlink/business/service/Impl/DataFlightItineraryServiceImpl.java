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
import org.smartlink.business.domain.bo.DataFlightItineraryBo;
import org.smartlink.business.domain.vo.DataFlightItineraryVo;
import org.smartlink.business.domain.DataFlightItinerary;
import org.smartlink.business.mapper.DataFlightItineraryMapper;
import org.smartlink.business.service.IDataFlightItineraryService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 航空电子行程单Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataFlightItineraryServiceImpl implements IDataFlightItineraryService {

    private final DataFlightItineraryMapper baseMapper;

    /**
     * 查询航空电子行程单
     *
     * @param id 主键
     * @return 航空电子行程单
     */
    @Override
    public DataFlightItineraryVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询航空电子行程单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 航空电子行程单分页列表
     */
    @Override
    public TableDataInfo<DataFlightItineraryVo> queryPageList(DataFlightItineraryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataFlightItinerary> lqw = buildQueryWrapper(bo);
        Page<DataFlightItineraryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的航空电子行程单列表
     *
     * @param bo 查询条件
     * @return 航空电子行程单列表
     */
    @Override
    public List<DataFlightItineraryVo> queryList(DataFlightItineraryBo bo) {
        LambdaQueryWrapper<DataFlightItinerary> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataFlightItinerary> buildQueryWrapper(DataFlightItineraryBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataFlightItinerary> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataFlightItinerary::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataFlightItinerary::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getUserName()), DataFlightItinerary::getUserName, bo.getUserName());
        lqw.eq(StringUtils.isNotBlank(bo.getAgentCode()), DataFlightItinerary::getAgentCode, bo.getAgentCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCaacDevelopmentFund()), DataFlightItinerary::getCaacDevelopmentFund, bo.getCaacDevelopmentFund());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckCode()), DataFlightItinerary::getCheckCode, bo.getCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataFlightItinerary::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getFare()), DataFlightItinerary::getFare, bo.getFare());
        lqw.eq(StringUtils.isNotBlank(bo.getFuelSurcharge()), DataFlightItinerary::getFuelSurcharge, bo.getFuelSurcharge());
        lqw.eq(StringUtils.isNotBlank(bo.getInsurance()), DataFlightItinerary::getInsurance, bo.getInsurance());
        lqw.eq(StringUtils.isNotBlank(bo.getInternationalFlag()), DataFlightItinerary::getInternationalFlag, bo.getInternationalFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getIssueBy()), DataFlightItinerary::getIssueBy, bo.getIssueBy());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataFlightItinerary::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getTax()), DataFlightItinerary::getTax, bo.getTax());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataFlightItinerary::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataFlightItinerary::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintNumber()), DataFlightItinerary::getPrintNumber, bo.getPrintNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getEndorsement()), DataFlightItinerary::getEndorsement, bo.getEndorsement());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataFlightItinerary::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getIssuingStatus()), DataFlightItinerary::getIssuingStatus, bo.getIssuingStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getQrcode()), DataFlightItinerary::getQrcode, bo.getQrcode());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiptNumber()), DataFlightItinerary::getReceiptNumber, bo.getReceiptNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getNumberOfGpOrder()), DataFlightItinerary::getNumberOfGpOrder, bo.getNumberOfGpOrder());
        lqw.eq(StringUtils.isNotBlank(bo.getPromptInformation()), DataFlightItinerary::getPromptInformation, bo.getPromptInformation());
        lqw.eq(StringUtils.isNotBlank(bo.getOtherTaxes()), DataFlightItinerary::getOtherTaxes, bo.getOtherTaxes());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyer()), DataFlightItinerary::getBuyer, bo.getBuyer());
        lqw.eq(StringUtils.isNotBlank(bo.getSeller()), DataFlightItinerary::getSeller, bo.getSeller());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerTaxId()), DataFlightItinerary::getBuyerTaxId, bo.getBuyerTaxId());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxRate()), DataFlightItinerary::getTaxRate, bo.getTaxRate());
        lqw.eq(StringUtils.isNotBlank(bo.getTypeOfBusiness()), DataFlightItinerary::getTypeOfBusiness, bo.getTypeOfBusiness());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataFlightItinerary::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataFlightItinerary::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataFlightItinerary::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增航空电子行程单
     *
     * @param bo 航空电子行程单
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataFlightItineraryBo bo) {
        DataFlightItinerary add = MapstructUtils.convert(bo, DataFlightItinerary.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改航空电子行程单
     *
     * @param bo 航空电子行程单
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataFlightItineraryBo bo) {
        DataFlightItinerary update = MapstructUtils.convert(bo, DataFlightItinerary.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataFlightItinerary entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除航空电子行程单信息
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
