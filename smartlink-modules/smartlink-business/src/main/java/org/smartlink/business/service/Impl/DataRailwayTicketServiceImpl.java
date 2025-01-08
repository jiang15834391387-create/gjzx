package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataRailwayTicketBo;
import org.smartlink.business.domain.vo.DataRailwayTicketVo;
import org.smartlink.business.domain.DataRailwayTicket;
import org.smartlink.business.mapper.DataRailwayTicketMapper;
import org.smartlink.business.service.IDataRailwayTicketService;
import org.smartlink.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 火车票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataRailwayTicketServiceImpl implements IDataRailwayTicketService {

    private final DataRailwayTicketMapper baseMapper;

    /**
     * 查询火车票
     *
     * @param id 主键
     * @return 火车票
     */
    @Override
    public DataRailwayTicketVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询火车票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 火车票分页列表
     */
    @Override
    public TableDataInfo<DataRailwayTicketVo> queryPageList(DataRailwayTicketBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataRailwayTicket> lqw = buildQueryWrapper(bo);
        Page<DataRailwayTicketVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的火车票列表
     *
     * @param bo 查询条件
     * @return 火车票列表
     */
    @Override
    public List<DataRailwayTicketVo> queryList(DataRailwayTicketBo bo) {
        LambdaQueryWrapper<DataRailwayTicket> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataRailwayTicket> buildQueryWrapper(DataRailwayTicketBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataRailwayTicket> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataRailwayTicket::getTitle, bo.getTitle());
        lqw.eq(bo.getInvoiceDate() != null, DataRailwayTicket::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataRailwayTicket::getFileId, bo.getFileId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DataRailwayTicket::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataRailwayTicket::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getSeat()), DataRailwayTicket::getSeat, bo.getSeat());
        lqw.eq(StringUtils.isNotBlank(bo.getSeatNum()), DataRailwayTicket::getSeatNum, bo.getSeatNum());
        lqw.eq(StringUtils.isNotBlank(bo.getWicket()), DataRailwayTicket::getWicket, bo.getWicket());
        lqw.eq(StringUtils.isNotBlank(bo.getTicketAddress()), DataRailwayTicket::getTicketAddress, bo.getTicketAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getIdNumber()), DataRailwayTicket::getIdNumber, bo.getIdNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getSerialNumber()), DataRailwayTicket::getSerialNumber, bo.getSerialNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOff()), DataRailwayTicket::getStationGetOff, bo.getStationGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOn()), DataRailwayTicket::getStationGetOn, bo.getStationGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTime()), DataRailwayTicket::getInvoiceTime, bo.getInvoiceTime());
        lqw.eq(bo.getInvoiceTotal() != null, DataRailwayTicket::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataRailwayTicket::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getTrainNumber()), DataRailwayTicket::getTrainNumber, bo.getTrainNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataRailwayTicket::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataRailwayTicket::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getTypeOfBusiness()), DataRailwayTicket::getTypeOfBusiness, bo.getTypeOfBusiness());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundContent()), DataRailwayTicket::getRefundContent, bo.getRefundContent());
        lqw.eq(StringUtils.isNotBlank(bo.getTicketContent()), DataRailwayTicket::getTicketContent, bo.getTicketContent());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyer()), DataRailwayTicket::getBuyer, bo.getBuyer());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerTaxId()), DataRailwayTicket::getBuyerTaxId, bo.getBuyerTaxId());
        lqw.eq(StringUtils.isNotBlank(bo.getNumberOfOriginalInvoice()), DataRailwayTicket::getNumberOfOriginalInvoice, bo.getNumberOfOriginalInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getAirConditioning()), DataRailwayTicket::getAirConditioning, bo.getAirConditioning());
        lqw.eq(StringUtils.isNotBlank(bo.getTypeOfVoucher()), DataRailwayTicket::getTypeOfVoucher, bo.getTypeOfVoucher());
        lqw.eq(StringUtils.isNotBlank(bo.getTypeOfRailwayTicket()), DataRailwayTicket::getTypeOfRailwayTicket, bo.getTypeOfRailwayTicket());
        lqw.eq(StringUtils.isNotBlank(bo.getDiscountMark()), DataRailwayTicket::getDiscountMark, bo.getDiscountMark());
        lqw.eq(bo.getAmountRefunded() != null, DataRailwayTicket::getAmountRefunded, bo.getAmountRefunded());
        lqw.eq(bo.getFareOfOriginalRailwayTicket() != null, DataRailwayTicket::getFareOfOriginalRailwayTicket, bo.getFareOfOriginalRailwayTicket());
        lqw.eq(StringUtils.isNotBlank(bo.getDepartureStationOfOriginalRailwayTicket()), DataRailwayTicket::getDepartureStationOfOriginalRailwayTicket, bo.getDepartureStationOfOriginalRailwayTicket());
        lqw.eq(StringUtils.isNotBlank(bo.getDestinationStationOfOriginalRailwayTicket()), DataRailwayTicket::getDestinationStationOfOriginalRailwayTicket, bo.getDestinationStationOfOriginalRailwayTicket());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerAddrTel()), DataRailwayTicket::getBuyerAddrTel, bo.getBuyerAddrTel());
        lqw.eq(StringUtils.isNotBlank(bo.getBuyerBankAccount()), DataRailwayTicket::getBuyerBankAccount, bo.getBuyerBankAccount());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataRailwayTicket::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataRailwayTicket::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getConfidence()), DataRailwayTicket::getConfidence, bo.getConfidence());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckResult()), DataRailwayTicket::getCheckResult, bo.getCheckResult());
        lqw.eq(StringUtils.isNotBlank(bo.getPushBusinessInfoFlag()), DataRailwayTicket::getPushBusinessInfoFlag, bo.getPushBusinessInfoFlag());
        return lqw;
    }

    /**
     * 新增火车票
     *
     * @param bo 火车票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataRailwayTicketBo bo) {
        DataRailwayTicket add = MapstructUtils.convert(bo, DataRailwayTicket.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改火车票
     *
     * @param bo 火车票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataRailwayTicketBo bo) {
        DataRailwayTicket update = MapstructUtils.convert(bo, DataRailwayTicket.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataRailwayTicket entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除火车票信息
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
