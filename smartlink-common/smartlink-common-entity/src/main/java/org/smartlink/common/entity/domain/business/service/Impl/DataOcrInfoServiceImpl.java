package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrInfoVo;
import org.smartlink.common.entity.domain.business.mapper.DataOcrInfoMapper;
import org.smartlink.common.entity.domain.business.service.IDataOcrInfoService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 增值税发票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataOcrInfoServiceImpl implements IDataOcrInfoService {

    private final DataOcrInfoMapper baseMapper;

    /**
     * 查询增值税发票
     *
     * @param id 主键
     * @return 增值税发票
     */
    @Override
    public DataOcrInfoVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询增值税发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 增值税发票分页列表
     */
    @Override
    public TableDataInfo<DataOcrInfoVo> queryPageList(DataOcrInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataOcrInfo> lqw = buildQueryWrapper(bo);
        Page<DataOcrInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的增值税发票列表
     *
     * @param bo 查询条件
     * @return 增值税发票列表
     */
    @Override
    public List<DataOcrInfoVo> queryList(DataOcrInfoBo bo) {
        LambdaQueryWrapper<DataOcrInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataOcrInfo> buildQueryWrapper(DataOcrInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataOcrInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessSerialNo()), DataOcrInfo::getBusinessSerialNo, bo.getBusinessSerialNo());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataOcrInfo::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerAccount()), DataOcrInfo::getBuyerAccount, bo.getBuyerAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerAddress()), DataOcrInfo::getBuyerAddress, bo.getBuyerAddress());
        lqw.like(StringUtils.isNotBlank(bo.getBuyerName()), DataOcrInfo::getBuyerName, bo.getBuyerName());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerNo()), DataOcrInfo::getBuyerNo, bo.getBuyerNo());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckInvoice()), DataOcrInfo::getCheckInvoice, bo.getCheckInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getCancellationMark()), DataOcrInfo::getCancellationMark, bo.getCancellationMark());
        lqw.eq(StringUtils.isNotBlank(bo.getChecker()), DataOcrInfo::getChecker, bo.getChecker());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataOcrInfo::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataOcrInfo::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataOcrInfo::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataOcrInfo::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataOcrInfo::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataOcrInfo::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataOcrInfo::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getIssuer()), DataOcrInfo::getIssuer, bo.getIssuer());
        lqw.eq(StringUtils.isNotBlank(bo.getItemNames()), DataOcrInfo::getItemNames, bo.getItemNames());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckCode()), DataOcrInfo::getCheckCode, bo.getCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalLowercase()), DataOcrInfo::getTotalLowercase, bo.getTotalLowercase());
        lqw.eq(StringUtils.isNotBlank(bo.getPretaxAmount()), DataOcrInfo::getPretaxAmount, bo.getPretaxAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanySeal()), DataOcrInfo::getCompanySeal, bo.getCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getSellCompanySeal()), DataOcrInfo::getSellCompanySeal, bo.getSellCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getPageNumber()), DataOcrInfo::getPageNumber, bo.getPageNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceSheet()), DataOcrInfo::getInvoiceSheet, bo.getInvoiceSheet());
        lqw.eq(StringUtils.isNotBlank(bo.getMachineCode()), DataOcrInfo::getMachineCode, bo.getMachineCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCategory()), DataOcrInfo::getCategory, bo.getCategory());
        lqw.eq(StringUtils.isNotBlank(bo.getPassword1()), DataOcrInfo::getPassword1, bo.getPassword1());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicNumber()), DataOcrInfo::getElectronicNumber, bo.getElectronicNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getTravelTax()), DataOcrInfo::getTravelTax, bo.getTravelTax());
        lqw.eq(StringUtils.isNotBlank(bo.getPayee()), DataOcrInfo::getPayee, bo.getPayee());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerAccount()), DataOcrInfo::getSellerAccount, bo.getSellerAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerAddress()), DataOcrInfo::getSellerAddress, bo.getSellerAddress());
        lqw.like(StringUtils.isNotBlank(bo.getSellerName()), DataOcrInfo::getSellerName, bo.getSellerName());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerNo()), DataOcrInfo::getSellerNo, bo.getSellerNo());
        lqw.eq(StringUtils.isNotBlank(bo.getSumAmount()), DataOcrInfo::getSumAmount, bo.getSumAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getSumTax()), DataOcrInfo::getSumTax, bo.getSumTax());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalUppercase()), DataOcrInfo::getTotalUppercase, bo.getTotalUppercase());
        lqw.eq(StringUtils.isNotBlank(bo.getPurchaseMark()), DataOcrInfo::getPurchaseMark, bo.getPurchaseMark());
        lqw.eq(StringUtils.isNotBlank(bo.getBlockChain()), DataOcrInfo::getBlockChain, bo.getBlockChain());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataOcrInfo::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getTransitMark()), DataOcrInfo::getTransitMark, bo.getTransitMark());
        lqw.eq(StringUtils.isNotBlank(bo.getOilMark()), DataOcrInfo::getOilMark, bo.getOilMark());
        lqw.eq(StringUtils.isNotBlank(bo.getVehicleMark()), DataOcrInfo::getVehicleMark, bo.getVehicleMark());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataOcrInfo::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getRedDashed()), DataOcrInfo::getRedDashed, bo.getRedDashed());
        lqw.eq(StringUtils.isNotBlank(bo.getQrCode()), DataOcrInfo::getQrCode, bo.getQrCode());
        lqw.eq(StringUtils.isNotBlank(bo.getRightInvoiceDate()), DataOcrInfo::getRightInvoiceDate, bo.getRightInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getElePayId()), DataOcrInfo::getElePayId, bo.getElePayId());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintInvoiceCode()), DataOcrInfo::getPrintInvoiceCode, bo.getPrintInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getNoteCheckCode()), DataOcrInfo::getNoteCheckCode, bo.getNoteCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintTotal()), DataOcrInfo::getPrintTotal, bo.getPrintTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getPrintCheckCode()), DataOcrInfo::getPrintCheckCode, bo.getPrintCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getRightInvoiceNumber()), DataOcrInfo::getRightInvoiceNumber, bo.getRightInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getRightInvoiceCode()), DataOcrInfo::getRightInvoiceCode, bo.getRightInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getReplaceOpen()), DataOcrInfo::getReplaceOpen, bo.getReplaceOpen());
        lqw.eq(StringUtils.isNotBlank(bo.getDeduction()), DataOcrInfo::getDeduction, bo.getDeduction());
        lqw.eq(StringUtils.isNotBlank(bo.getHandwrite()), DataOcrInfo::getHandwrite, bo.getHandwrite());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataOcrInfo::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增增值税发票
     *
     * @param bo 增值税发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataOcrInfoBo bo) {
        DataOcrInfo add = MapstructUtils.convert(bo, DataOcrInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 新增增值税发票
     *
     * @param dataOcrInfo 增值税发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insert(DataOcrInfo dataOcrInfo) {
        validEntityBeforeSave(dataOcrInfo);
        boolean flag = baseMapper.insert(dataOcrInfo) > 0;
        return flag;
    }

    /**
     * 修改增值税发票
     *
     * @param bo 增值税发票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataOcrInfoBo bo) {
        DataOcrInfo update = MapstructUtils.convert(bo, DataOcrInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataOcrInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除增值税发票信息
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
