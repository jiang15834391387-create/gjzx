package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataSteamerTicket;
import org.smartlink.common.entity.domain.business.domain.DataTaxiTickets;
import org.smartlink.common.entity.domain.business.domain.bo.DataSteamerTicketBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataSteamerTicketVo;
import org.smartlink.common.entity.domain.business.mapper.DataSteamerTicketMapper;
import org.smartlink.common.entity.domain.business.service.IDataSteamerTicketService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 船票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataSteamerTicketServiceImpl implements IDataSteamerTicketService {

    private final DataSteamerTicketMapper baseMapper;

    /**
     * 查询船票
     *
     * @param id 主键
     * @return 船票
     */
    @Override
    public DataSteamerTicketVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询船票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 船票分页列表
     */
    @Override
    public TableDataInfo<DataSteamerTicketVo> queryPageList(DataSteamerTicketBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataSteamerTicket> lqw = buildQueryWrapper(bo);
        Page<DataSteamerTicketVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public Boolean insert(DataSteamerTicket dataOcrInfo) {
        boolean flag = baseMapper.insert(dataOcrInfo) > 0;
        return flag;
    }

    @Override
    public DataSteamerTicket selectOneByFileId(String fileId) {
        DataSteamerTicket result = buildQueryWrapperByFileId(fileId);
        return result;
    }

    private DataSteamerTicket buildQueryWrapperByFileId(String fileId) {
        LambdaQueryWrapper<DataSteamerTicket> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DataSteamerTicket::getFileId, fileId); // 使用 Lambda 表达式
        return baseMapper.selectOne(lambdaQueryWrapper); // 返回查询结果
    }

    /**
     * 查询符合条件的船票列表
     *
     * @param bo 查询条件
     * @return 船票列表
     */
    @Override
    public List<DataSteamerTicketVo> queryList(DataSteamerTicketBo bo) {
        LambdaQueryWrapper<DataSteamerTicket> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataSteamerTicket> buildQueryWrapper(DataSteamerTicketBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataSteamerTicket> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataSteamerTicket::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataSteamerTicket::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataSteamerTicket::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataSteamerTicket::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCurrencyCode()), DataSteamerTicket::getCurrencyCode, bo.getCurrencyCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataSteamerTicket::getInvoiceDate, bo.getInvoiceDate());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DataSteamerTicket::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataSteamerTicket::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataSteamerTicket::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOff()), DataSteamerTicket::getStationGetOff, bo.getStationGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetOn()), DataSteamerTicket::getStationGetOn, bo.getStationGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTime()), DataSteamerTicket::getInvoiceTime, bo.getInvoiceTime());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataSteamerTicket::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataSteamerTicket::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getUserId()), DataSteamerTicket::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataSteamerTicket::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataSteamerTicket::getRegion, bo.getRegion());
        return lqw;
    }

    /**
     * 新增船票
     *
     * @param bo 船票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataSteamerTicketBo bo) {
        DataSteamerTicket add = MapstructUtils.convert(bo, DataSteamerTicket.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改船票
     *
     * @param bo 船票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataSteamerTicketBo bo) {
        DataSteamerTicket update = MapstructUtils.convert(bo, DataSteamerTicket.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataSteamerTicket entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除船票信息
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
