package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.smartlink.common.entity.domain.business.domain.bo.DataUsedCarSalesBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataUsedCarSalesVo;
import org.smartlink.common.entity.domain.business.mapper.DataUsedCarSalesMapper;
import org.smartlink.common.entity.domain.business.service.IDataUsedCarSalesService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 二手车销售统一发票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DataUsedCarSalesServiceImpl implements IDataUsedCarSalesService {

    private final DataUsedCarSalesMapper baseMapper;

    /**
     * 查询二手车销售统一发票
     *
     * @param  id 主键
     * @return 二手车销售统一发票
     */
    @Override
    public DataUsedCarSalesVo queryById(String  id){
        return baseMapper.selectVoById( id);
    }

    /**
     * 分页查询二手车销售统一发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 二手车销售统一发票分页列表
     */
    @Override
    public TableDataInfo<DataUsedCarSalesVo> queryPageList(DataUsedCarSalesBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataUsedCarSales> lqw = buildQueryWrapper(bo);
        Page<DataUsedCarSalesVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的二手车销售统一发票列表
     *
     * @param bo 查询条件
     * @return 二手车销售统一发票列表
     */
    @Override
    public List<DataUsedCarSalesVo> queryList(DataUsedCarSalesBo bo) {
        LambdaQueryWrapper<DataUsedCarSales> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataUsedCarSales> buildQueryWrapper(DataUsedCarSalesBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataUsedCarSales> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataUsedCarSales::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataUsedCarSales::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessUnit()), DataUsedCarSales::getBusinessUnit, bo.getBusinessUnit());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessUnitTaxNo()), DataUsedCarSales::getBusinessUnitTaxNo, bo.getBusinessUnitTaxNo());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessUnitAddress()), DataUsedCarSales::getBusinessUnitAddress, bo.getBusinessUnitAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerAccount()), DataUsedCarSales::getSellerAccount, bo.getSellerAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessUnitPhone()), DataUsedCarSales::getBusinessUnitPhone, bo.getBusinessUnitPhone());
        lqw.like(StringUtils.isNotBlank(bo.getBuyerName()), DataUsedCarSales::getBuyerName, bo.getBuyerName());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerId()), DataUsedCarSales::getBuyerId, bo.getBuyerId());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerPhone()), DataUsedCarSales::getBuyerPhone, bo.getBuyerPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerAddress()), DataUsedCarSales::getBuyerAddress, bo.getBuyerAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getCarCode()), DataUsedCarSales::getCarCode, bo.getCarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCarEngineCode()), DataUsedCarSales::getCarEngineCode, bo.getCarEngineCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCarModel()), DataUsedCarSales::getCarModel, bo.getCarModel());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckInvoice()), DataUsedCarSales::getCheckInvoice, bo.getCheckInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataUsedCarSales::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataUsedCarSales::getInvoiceCode, bo.getInvoiceCode());
        lqw.like(StringUtils.isNotBlank(bo.getCompanyName()), DataUsedCarSales::getCompanyName, bo.getCompanyName());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanyTaxId()), DataUsedCarSales::getCompanyTaxId, bo.getCompanyTaxId());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataUsedCarSales::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getLemonMarketBankAndCcount()), DataUsedCarSales::getLemonMarketBankAndCcount, bo.getLemonMarketBankAndCcount());
        lqw.eq(StringUtils.isNotBlank(bo.getLemonMarketPhone()), DataUsedCarSales::getLemonMarketPhone, bo.getLemonMarketPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getLemonMarketAddress()), DataUsedCarSales::getLemonMarketAddress, bo.getLemonMarketAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getLicensePlate()), DataUsedCarSales::getLicensePlate, bo.getLicensePlate());
        lqw.eq(StringUtils.isNotBlank(bo.getCarType()), DataUsedCarSales::getCarType, bo.getCarType());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataUsedCarSales::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataUsedCarSales::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getRegisTrationNumber()), DataUsedCarSales::getRegisTrationNumber, bo.getRegisTrationNumber());
        lqw.like(StringUtils.isNotBlank(bo.getSellerName()), DataUsedCarSales::getSellerName, bo.getSellerName());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerId()), DataUsedCarSales::getSellerId, bo.getSellerId());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerPhone()), DataUsedCarSales::getSellerPhone, bo.getSellerPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerAddress()), DataUsedCarSales::getSellerAddress, bo.getSellerAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataUsedCarSales::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getPageNumber()), DataUsedCarSales::getPageNumber, bo.getPageNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceSheet()), DataUsedCarSales::getInvoiceSheet, bo.getInvoiceSheet());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataUsedCarSales::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanySeal()), DataUsedCarSales::getCompanySeal, bo.getCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalUppercase()), DataUsedCarSales::getTotalUppercase, bo.getTotalUppercase());
        lqw.eq(StringUtils.isNotBlank(bo.getMachineCode()), DataUsedCarSales::getMachineCode, bo.getMachineCode());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxCode()), DataUsedCarSales::getTaxCode, bo.getTaxCode());
        lqw.like(StringUtils.isNotBlank(bo.getVehicleManageName()), DataUsedCarSales::getVehicleManageName, bo.getVehicleManageName());
        lqw.eq(StringUtils.isNotBlank(bo.getVehicleType()), DataUsedCarSales::getVehicleType, bo.getVehicleType());
        lqw.eq(StringUtils.isNotBlank(bo.getMachineId()), DataUsedCarSales::getMachineId, bo.getMachineId());
        lqw.eq(StringUtils.isNotBlank(bo.getMachineNumber()), DataUsedCarSales::getMachineNumber, bo.getMachineNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicNumber()), DataUsedCarSales::getElectronicNumber, bo.getElectronicNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataUsedCarSales::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getIssuer()), DataUsedCarSales::getIssuer, bo.getIssuer());
        lqw.eq(StringUtils.isNotBlank(bo.getDestinationDepartmentOfMotorVehicles()), DataUsedCarSales::getDestinationDepartmentOfMotorVehicles, bo.getDestinationDepartmentOfMotorVehicles());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataUsedCarSales::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataUsedCarSales::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增二手车销售统一发票
     *
     * @param bo 二手车销售统一发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataUsedCarSalesBo bo) {
        DataUsedCarSales add = MapstructUtils.convert(bo, DataUsedCarSales.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改二手车销售统一发票
     *
     * @param bo 二手车销售统一发票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataUsedCarSalesBo bo) {
        DataUsedCarSales update = MapstructUtils.convert(bo, DataUsedCarSales.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataUsedCarSales entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除二手车销售统一发票信息
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

    @Override
    public DataUsedCarSales getByFileId(String fileId) {
        if (StringUtils.isBlank(fileId)){
            log.error("行程单文件fileId为空", fileId);
            return null;
        }
        return baseMapper.selectOne(new LambdaQueryWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, fileId));
    }
}
