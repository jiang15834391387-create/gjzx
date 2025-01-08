package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataAircraftInvoiceBo;
import org.smartlink.business.domain.vo.DataAircraftInvoiceVo;
import org.smartlink.business.domain.DataAircraftInvoice;
import org.smartlink.business.mapper.DataAircraftInvoiceMapper;
import org.smartlink.business.service.IDataAircraftInvoiceService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 机打发票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataAircraftInvoiceServiceImpl implements IDataAircraftInvoiceService {

    private final DataAircraftInvoiceMapper baseMapper;

    /**
     * 查询机打发票
     *
     * @param id 主键
     * @return 机打发票
     */
    @Override
    public DataAircraftInvoiceVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询机打发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 机打发票分页列表
     */
    @Override
    public TableDataInfo<DataAircraftInvoiceVo> queryPageList(DataAircraftInvoiceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataAircraftInvoice> lqw = buildQueryWrapper(bo);
        Page<DataAircraftInvoiceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的机打发票列表
     *
     * @param bo 查询条件
     * @return 机打发票列表
     */
    @Override
    public List<DataAircraftInvoiceVo> queryList(DataAircraftInvoiceBo bo) {
        LambdaQueryWrapper<DataAircraftInvoice> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataAircraftInvoice> buildQueryWrapper(DataAircraftInvoiceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataAircraftInvoice> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataAircraftInvoice::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataAircraftInvoice::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getBuyerName()), DataAircraftInvoice::getBuyerName, bo.getBuyerName());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerTaxid()), DataAircraftInvoice::getBuyerTaxid, bo.getBuyerTaxid());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), DataAircraftInvoice::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckCode()), DataAircraftInvoice::getCheckCode, bo.getCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataAircraftInvoice::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataAircraftInvoice::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataAircraftInvoice::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(bo.getInvoiceDate() != null, DataAircraftInvoice::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataAircraftInvoice::getProvince, bo.getProvince());
        lqw.like(StringUtils.isNotBlank(bo.getSellerName()), DataAircraftInvoice::getSellerName, bo.getSellerName());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerTaxid()), DataAircraftInvoice::getSellerTaxid, bo.getSellerTaxid());
        lqw.eq(bo.getInvoiceTotal() != null, DataAircraftInvoice::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataAircraftInvoice::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanySeal()), DataAircraftInvoice::getCompanySeal, bo.getCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getMoneyUppercase()), DataAircraftInvoice::getMoneyUppercase, bo.getMoneyUppercase());
        lqw.eq(bo.getPretaxAmount() != null, DataAircraftInvoice::getPretaxAmount, bo.getPretaxAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataAircraftInvoice::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataAircraftInvoice::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataAircraftInvoice::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getConfidence()), DataAircraftInvoice::getConfidence, bo.getConfidence());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckResult()), DataAircraftInvoice::getCheckResult, bo.getCheckResult());
        lqw.eq(StringUtils.isNotBlank(bo.getPushBusinessInfoFlag()), DataAircraftInvoice::getPushBusinessInfoFlag, bo.getPushBusinessInfoFlag());
        return lqw;
    }

    /**
     * 新增机打发票
     *
     * @param bo 机打发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataAircraftInvoiceBo bo) {
        DataAircraftInvoice add = MapstructUtils.convert(bo, DataAircraftInvoice.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改机打发票
     *
     * @param bo 机打发票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataAircraftInvoiceBo bo) {
        DataAircraftInvoice update = MapstructUtils.convert(bo, DataAircraftInvoice.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataAircraftInvoice entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除机打发票信息
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
