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
import org.smartlink.business.domain.bo.DataMotorVehicleSaleBo;
import org.smartlink.business.domain.vo.DataMotorVehicleSaleVo;
import org.smartlink.business.domain.DataMotorVehicleSale;
import org.smartlink.business.mapper.DataMotorVehicleSaleMapper;
import org.smartlink.business.service.IDataMotorVehicleSaleService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 机动车销售发票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataMotorVehicleSaleServiceImpl implements IDataMotorVehicleSaleService {

    private final DataMotorVehicleSaleMapper baseMapper;

    /**
     * 查询机动车销售发票
     *
     * @param id 主键
     * @return 机动车销售发票
     */
    @Override
    public DataMotorVehicleSaleVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询机动车销售发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 机动车销售发票分页列表
     */
    @Override
    public TableDataInfo<DataMotorVehicleSaleVo> queryPageList(DataMotorVehicleSaleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataMotorVehicleSale> lqw = buildQueryWrapper(bo);
        Page<DataMotorVehicleSaleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的机动车销售发票列表
     *
     * @param bo 查询条件
     * @return 机动车销售发票列表
     */
    @Override
    public List<DataMotorVehicleSaleVo> queryList(DataMotorVehicleSaleBo bo) {
        LambdaQueryWrapper<DataMotorVehicleSale> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataMotorVehicleSale> buildQueryWrapper(DataMotorVehicleSaleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataMotorVehicleSale> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataMotorVehicleSale::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getBuyerName()), DataMotorVehicleSale::getBuyerName, bo.getBuyerName());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerId()), DataMotorVehicleSale::getBuyerId, bo.getBuyerId());
        lqw.eq(StringUtils.isNotBlank(bo.getCarCode()), DataMotorVehicleSale::getCarCode, bo.getCarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCarEngineCode()), DataMotorVehicleSale::getCarEngineCode, bo.getCarEngineCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCarModel()), DataMotorVehicleSale::getCarModel, bo.getCarModel());
        lqw.eq(StringUtils.isNotBlank(bo.getCertificateNumber()), DataMotorVehicleSale::getCertificateNumber, bo.getCertificateNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCertificateOfImport()), DataMotorVehicleSale::getCertificateOfImport, bo.getCertificateOfImport());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckInvoice()), DataMotorVehicleSale::getCheckInvoice, bo.getCheckInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataMotorVehicleSale::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataMotorVehicleSale::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCommodityInspectionNo()), DataMotorVehicleSale::getCommodityInspectionNo, bo.getCommodityInspectionNo());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataMotorVehicleSale::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getDrawer()), DataMotorVehicleSale::getDrawer, bo.getDrawer());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataMotorVehicleSale::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getLimitedPeopleCount()), DataMotorVehicleSale::getLimitedPeopleCount, bo.getLimitedPeopleCount());
        lqw.eq(StringUtils.isNotBlank(bo.getMachineCode()), DataMotorVehicleSale::getMachineCode, bo.getMachineCode());
        lqw.eq(StringUtils.isNotBlank(bo.getMachineNumber()), DataMotorVehicleSale::getMachineNumber, bo.getMachineNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataMotorVehicleSale::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPreTaxAmount()), DataMotorVehicleSale::getPreTaxAmount, bo.getPreTaxAmount());
        lqw.eq(StringUtils.isNotBlank(bo.getProduceArea()), DataMotorVehicleSale::getProduceArea, bo.getProduceArea());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataMotorVehicleSale::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataMotorVehicleSale::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getSeller()), DataMotorVehicleSale::getSeller, bo.getSeller());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerAddress()), DataMotorVehicleSale::getSellerAddress, bo.getSellerAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerBankAccount()), DataMotorVehicleSale::getSellerBankAccount, bo.getSellerBankAccount());
        lqw.like(StringUtils.isNotBlank(bo.getSellerBankName()), DataMotorVehicleSale::getSellerBankName, bo.getSellerBankName());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerPhone()), DataMotorVehicleSale::getSellerPhone, bo.getSellerPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getSellerTaxid()), DataMotorVehicleSale::getSellerTaxid, bo.getSellerTaxid());
        lqw.eq(StringUtils.isNotBlank(bo.getTax()), DataMotorVehicleSale::getTax, bo.getTax());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxAuthorities()), DataMotorVehicleSale::getTaxAuthorities, bo.getTaxAuthorities());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxAuthoritiesCode()), DataMotorVehicleSale::getTaxAuthoritiesCode, bo.getTaxAuthoritiesCode());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxPaymentCertificateNo()), DataMotorVehicleSale::getTaxPaymentCertificateNo, bo.getTaxPaymentCertificateNo());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxRate()), DataMotorVehicleSale::getTaxRate, bo.getTaxRate());
        lqw.eq(StringUtils.isNotBlank(bo.getTaxCode()), DataMotorVehicleSale::getTaxCode, bo.getTaxCode());
        lqw.eq(StringUtils.isNotBlank(bo.getQrCode()), DataMotorVehicleSale::getQrCode, bo.getQrCode());
        lqw.eq(StringUtils.isNotBlank(bo.getTonnage()), DataMotorVehicleSale::getTonnage, bo.getTonnage());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataMotorVehicleSale::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataMotorVehicleSale::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanySeal()), DataMotorVehicleSale::getCompanySeal, bo.getCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getPageNumber()), DataMotorVehicleSale::getPageNumber, bo.getPageNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceSheet()), DataMotorVehicleSale::getInvoiceSheet, bo.getInvoiceSheet());
        lqw.eq(StringUtils.isNotBlank(bo.getVehicleType()), DataMotorVehicleSale::getVehicleType, bo.getVehicleType());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicNumber()), DataMotorVehicleSale::getElectronicNumber, bo.getElectronicNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataMotorVehicleSale::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataMotorVehicleSale::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataMotorVehicleSale::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增机动车销售发票
     *
     * @param bo 机动车销售发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataMotorVehicleSaleBo bo) {
        DataMotorVehicleSale add = MapstructUtils.convert(bo, DataMotorVehicleSale.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改机动车销售发票
     *
     * @param bo 机动车销售发票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataMotorVehicleSaleBo bo) {
        DataMotorVehicleSale update = MapstructUtils.convert(bo, DataMotorVehicleSale.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataMotorVehicleSale entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除机动车销售发票信息
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
