package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataCustomsSpecialPayment;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsSpecialPaymentBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsSpecialPaymentVo;
import org.smartlink.common.entity.domain.business.mapper.DataCustomsSpecialPaymentMapper;
import org.smartlink.common.entity.domain.business.service.IDataCustomsSpecialPaymentService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 海关专用缴款书Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataCustomsSpecialPaymentServiceImpl implements IDataCustomsSpecialPaymentService {

    private final DataCustomsSpecialPaymentMapper baseMapper;

    /**
     * 查询海关专用缴款书
     *
     * @param id 主键
     * @return 海关专用缴款书
     */
    @Override
    public DataCustomsSpecialPaymentVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询海关专用缴款书列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关专用缴款书分页列表
     */
    @Override
    public TableDataInfo<DataCustomsSpecialPaymentVo> queryPageList(DataCustomsSpecialPaymentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataCustomsSpecialPayment> lqw = buildQueryWrapper(bo);
        Page<DataCustomsSpecialPaymentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的海关专用缴款书列表
     *
     * @param bo 查询条件
     * @return 海关专用缴款书列表
     */
    @Override
    public List<DataCustomsSpecialPaymentVo> queryList(DataCustomsSpecialPaymentBo bo) {
        LambdaQueryWrapper<DataCustomsSpecialPayment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataCustomsSpecialPayment> buildQueryWrapper(DataCustomsSpecialPaymentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataCustomsSpecialPayment> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataCustomsSpecialPayment::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataCustomsSpecialPayment::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getAccount()), DataCustomsSpecialPayment::getAccount, bo.getAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getAccountBank()), DataCustomsSpecialPayment::getAccountBank, bo.getAccountBank());
        lqw.like(StringUtils.isNotBlank(bo.getCompanyName()), DataCustomsSpecialPayment::getCompanyName, bo.getCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getContractNumber()), DataCustomsSpecialPayment::getContractNumber, bo.getContractNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCurrencyComment()), DataCustomsSpecialPayment::getCurrencyComment, bo.getCurrencyComment());
        lqw.like(StringUtils.isNotBlank(bo.getCustomsName()), DataCustomsSpecialPayment::getCustomsName, bo.getCustomsName());
        lqw.eq(StringUtils.isNotBlank(bo.getCustomsNumber()), DataCustomsSpecialPayment::getCustomsNumber, bo.getCustomsNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryNumber()), DataCustomsSpecialPayment::getDeliveryNumber, bo.getDeliveryNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getFillingCompany()), DataCustomsSpecialPayment::getFillingCompany, bo.getFillingCompany());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataCustomsSpecialPayment::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getPaymentType()), DataCustomsSpecialPayment::getPaymentType, bo.getPaymentType());
        lqw.eq(StringUtils.isNotBlank(bo.getRemarks()), DataCustomsSpecialPayment::getRemarks, bo.getRemarks());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataCustomsSpecialPayment::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getSeal()), DataCustomsSpecialPayment::getSeal, bo.getSeal());
        lqw.eq(StringUtils.isNotBlank(bo.getNumber()), DataCustomsSpecialPayment::getNumber, bo.getNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPortCode()), DataCustomsSpecialPayment::getPortCode, bo.getPortCode());
        lqw.eq(StringUtils.isNotBlank(bo.getRevenueAgency()), DataCustomsSpecialPayment::getRevenueAgency, bo.getRevenueAgency());
        lqw.eq(StringUtils.isNotBlank(bo.getSubject()), DataCustomsSpecialPayment::getSubject, bo.getSubject());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxExchangeRateComment()), DataCustomsSpecialPayment::getTaxExchangeRateComment, bo.getTaxExchangeRateComment());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataCustomsSpecialPayment::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalWords()), DataCustomsSpecialPayment::getTotalWords, bo.getTotalWords());
        lqw.eq(StringUtils.isNotBlank(bo.getTransportationTools()), DataCustomsSpecialPayment::getTransportationTools, bo.getTransportationTools());
        lqw.eq(StringUtils.isNotBlank(bo.getIncomeSystem()), DataCustomsSpecialPayment::getIncomeSystem, bo.getIncomeSystem());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiptTreasury()), DataCustomsSpecialPayment::getReceiptTreasury, bo.getReceiptTreasury());
        lqw.eq(StringUtils.isNotBlank(bo.getBudgetLevel()), DataCustomsSpecialPayment::getBudgetLevel, bo.getBudgetLevel());
        lqw.eq(StringUtils.isNotBlank(bo.getApplicationUnitNumber()), DataCustomsSpecialPayment::getApplicationUnitNumber, bo.getApplicationUnitNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPaymentDeadline()), DataCustomsSpecialPayment::getPaymentDeadline, bo.getPaymentDeadline());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataCustomsSpecialPayment::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataCustomsSpecialPayment::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataCustomsSpecialPayment::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增海关专用缴款书
     *
     * @param bo 海关专用缴款书
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataCustomsSpecialPaymentBo bo) {
        DataCustomsSpecialPayment add = MapstructUtils.convert(bo, DataCustomsSpecialPayment.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改海关专用缴款书
     *
     * @param bo 海关专用缴款书
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataCustomsSpecialPaymentBo bo) {
        DataCustomsSpecialPayment update = MapstructUtils.convert(bo, DataCustomsSpecialPayment.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataCustomsSpecialPayment entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除海关专用缴款书信息
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
