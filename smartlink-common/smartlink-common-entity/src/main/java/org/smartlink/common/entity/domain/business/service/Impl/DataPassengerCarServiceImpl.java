package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataPassengerCar;
import org.smartlink.common.entity.domain.business.domain.bo.DataPassengerCarBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataPassengerCarVo;
import org.smartlink.common.entity.domain.business.mapper.DataPassengerCarMapper;
import org.smartlink.common.entity.domain.business.service.IDataPassengerCarService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 客运汽车票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataPassengerCarServiceImpl implements IDataPassengerCarService {

    private final DataPassengerCarMapper baseMapper;

    /**
     * 查询客运汽车票
     *
     * @param id 主键
     * @return 客运汽车票
     */
    @Override
    public DataPassengerCarVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询客运汽车票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客运汽车票分页列表
     */
    @Override
    public TableDataInfo<DataPassengerCarVo> queryPageList(DataPassengerCarBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataPassengerCar> lqw = buildQueryWrapper(bo);
        Page<DataPassengerCarVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的客运汽车票列表
     *
     * @param bo 查询条件
     * @return 客运汽车票列表
     */
    @Override
    public List<DataPassengerCarVo> queryList(DataPassengerCarBo bo) {
        LambdaQueryWrapper<DataPassengerCar> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataPassengerCar> buildQueryWrapper(DataPassengerCarBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataPassengerCar> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataPassengerCar::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataPassengerCar::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataPassengerCar::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataPassengerCar::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGeton()), DataPassengerCar::getStationGeton, bo.getStationGeton());
        lqw.eq(StringUtils.isNotBlank(bo.getStationGetoff()), DataPassengerCar::getStationGetoff, bo.getStationGetoff());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataPassengerCar::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTime()), DataPassengerCar::getInvoiceTime, bo.getInvoiceTime());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataPassengerCar::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.like(StringUtils.isNotBlank(bo.getName()), DataPassengerCar::getName, bo.getName());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataPassengerCar::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getUserId()), DataPassengerCar::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanySeal()), DataPassengerCar::getCompanySeal, bo.getCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getBusNumber()), DataPassengerCar::getBusNumber, bo.getBusNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckInvoice()), DataPassengerCar::getCheckInvoice, bo.getCheckInvoice());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataPassengerCar::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增客运汽车票
     *
     * @param bo 客运汽车票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataPassengerCarBo bo) {
        DataPassengerCar add = MapstructUtils.convert(bo, DataPassengerCar.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客运汽车票
     *
     * @param bo 客运汽车票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataPassengerCarBo bo) {
        DataPassengerCar update = MapstructUtils.convert(bo, DataPassengerCar.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataPassengerCar entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除客运汽车票信息
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
