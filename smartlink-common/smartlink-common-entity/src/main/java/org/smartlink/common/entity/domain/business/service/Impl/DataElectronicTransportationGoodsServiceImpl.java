package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataElectronicTransportationGoods;
import org.smartlink.common.entity.domain.business.domain.bo.DataElectronicTransportationGoodsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataElectronicTransportationGoodsVo;
import org.smartlink.common.entity.domain.business.mapper.DataElectronicTransportationGoodsMapper;
import org.smartlink.common.entity.domain.business.service.IDataElectronicTransportationGoodsService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 货物运输电子收款凭证Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataElectronicTransportationGoodsServiceImpl implements IDataElectronicTransportationGoodsService {

    private final DataElectronicTransportationGoodsMapper baseMapper;

    /**
     * 查询货物运输电子收款凭证
     *
     * @param id 主键
     * @return 货物运输电子收款凭证
     */
    @Override
    public DataElectronicTransportationGoodsVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询货物运输电子收款凭证列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 货物运输电子收款凭证分页列表
     */
    @Override
    public TableDataInfo<DataElectronicTransportationGoodsVo> queryPageList(DataElectronicTransportationGoodsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataElectronicTransportationGoods> lqw = buildQueryWrapper(bo);
        Page<DataElectronicTransportationGoodsVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的货物运输电子收款凭证列表
     *
     * @param bo 查询条件
     * @return 货物运输电子收款凭证列表
     */
    @Override
    public List<DataElectronicTransportationGoodsVo> queryList(DataElectronicTransportationGoodsBo bo) {
        LambdaQueryWrapper<DataElectronicTransportationGoods> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataElectronicTransportationGoods> buildQueryWrapper(DataElectronicTransportationGoodsBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataElectronicTransportationGoods> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataElectronicTransportationGoods::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getDate()), DataElectronicTransportationGoods::getDate, bo.getDate());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessLicenseNumber()), DataElectronicTransportationGoods::getBusinessLicenseNumber, bo.getBusinessLicenseNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicReceiptNumber()), DataElectronicTransportationGoods::getElectronicReceiptNumber, bo.getElectronicReceiptNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getProducer()), DataElectronicTransportationGoods::getProducer, bo.getProducer());
        lqw.eq(StringUtils.isNotBlank(bo.getShipper()), DataElectronicTransportationGoods::getShipper, bo.getShipper());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalPrice()), DataElectronicTransportationGoods::getTotalPrice, bo.getTotalPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalCn()), DataElectronicTransportationGoods::getTotalCn, bo.getTotalCn());
        lqw.eq(StringUtils.isNotBlank(bo.getTransporter()), DataElectronicTransportationGoods::getTransporter, bo.getTransporter());
        lqw.eq(StringUtils.isNotBlank(bo.getTransporterIdNumber()), DataElectronicTransportationGoods::getTransporterIdNumber, bo.getTransporterIdNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataElectronicTransportationGoods::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataElectronicTransportationGoods::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增货物运输电子收款凭证
     *
     * @param bo 货物运输电子收款凭证
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataElectronicTransportationGoodsBo bo) {
        DataElectronicTransportationGoods add = MapstructUtils.convert(bo, DataElectronicTransportationGoods.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改货物运输电子收款凭证
     *
     * @param bo 货物运输电子收款凭证
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataElectronicTransportationGoodsBo bo) {
        DataElectronicTransportationGoods update = MapstructUtils.convert(bo, DataElectronicTransportationGoods.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataElectronicTransportationGoods entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除货物运输电子收款凭证信息
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
