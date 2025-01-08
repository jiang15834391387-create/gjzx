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
import org.smartlink.business.domain.bo.DataTaxiTicketsBo;
import org.smartlink.business.domain.vo.DataTaxiTicketsVo;
import org.smartlink.business.domain.DataTaxiTickets;
import org.smartlink.business.mapper.DataTaxiTicketsMapper;
import org.smartlink.business.service.IDataTaxiTicketsService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 出租车发票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataTaxiTicketsServiceImpl implements IDataTaxiTicketsService {

    private final DataTaxiTicketsMapper baseMapper;

    /**
     * 查询出租车发票
     *
     * @param id 主键
     * @return 出租车发票
     */
    @Override
    public DataTaxiTicketsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询出租车发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出租车发票分页列表
     */
    @Override
    public TableDataInfo<DataTaxiTicketsVo> queryPageList(DataTaxiTicketsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataTaxiTickets> lqw = buildQueryWrapper(bo);
        Page<DataTaxiTicketsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的出租车发票列表
     *
     * @param bo 查询条件
     * @return 出租车发票列表
     */
    @Override
    public List<DataTaxiTicketsVo> queryList(DataTaxiTicketsBo bo) {
        LambdaQueryWrapper<DataTaxiTickets> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataTaxiTickets> buildQueryWrapper(DataTaxiTicketsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataTaxiTickets> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataTaxiTickets::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataTaxiTickets::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataTaxiTickets::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataTaxiTickets::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataTaxiTickets::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getLicensePlate()), DataTaxiTickets::getLicensePlate, bo.getLicensePlate());
        lqw.eq(StringUtils.isNotBlank(bo.getMileage()), DataTaxiTickets::getMileage, bo.getMileage());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataTaxiTickets::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPlace()), DataTaxiTickets::getPlace, bo.getPlace());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataTaxiTickets::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeGetOff()), DataTaxiTickets::getTimeGetOff, bo.getTimeGetOff());
        lqw.eq(StringUtils.isNotBlank(bo.getTimeGetOn()), DataTaxiTickets::getTimeGetOn, bo.getTimeGetOn());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataTaxiTickets::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataTaxiTickets::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getFuelSurcharge()), DataTaxiTickets::getFuelSurcharge, bo.getFuelSurcharge());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataTaxiTickets::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataTaxiTickets::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增出租车发票
     *
     * @param bo 出租车发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataTaxiTicketsBo bo) {
        DataTaxiTickets add = MapstructUtils.convert(bo, DataTaxiTickets.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改出租车发票
     *
     * @param bo 出租车发票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataTaxiTicketsBo bo) {
        DataTaxiTickets update = MapstructUtils.convert(bo, DataTaxiTickets.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataTaxiTickets entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除出租车发票信息
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
