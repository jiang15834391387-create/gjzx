package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataQuotaInvoice;
import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.smartlink.common.entity.domain.business.domain.bo.DataQuotaInvoiceBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataQuotaInvoiceVo;
import org.smartlink.common.entity.domain.business.mapper.DataQuotaInvoiceMapper;
import org.smartlink.common.entity.domain.business.service.IDataQuotaInvoiceService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 定额发票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataQuotaInvoiceServiceImpl implements IDataQuotaInvoiceService {

    private final DataQuotaInvoiceMapper baseMapper;

    /**
     * 查询定额发票
     *
     * @param id 主键
     * @return 定额发票
     */
    @Override
    public DataQuotaInvoiceVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insert(DataQuotaInvoice dataOcrInfo) {
        boolean flag = baseMapper.insert(dataOcrInfo) > 0;
        return flag;
    }

    /**
     * 分页查询定额发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 定额发票分页列表
     */
    @Override
    public TableDataInfo<DataQuotaInvoiceVo> queryPageList(DataQuotaInvoiceBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataQuotaInvoice> lqw = buildQueryWrapper(bo);
        Page<DataQuotaInvoiceVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的定额发票列表
     *
     * @param bo 查询条件
     * @return 定额发票列表
     */
    @Override
    public List<DataQuotaInvoiceVo> queryList(DataQuotaInvoiceBo bo) {
        LambdaQueryWrapper<DataQuotaInvoice> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public DataQuotaInvoice selectOneByFileId(String fileId) {
        DataQuotaInvoice result = buildQueryWrapperByFileId(fileId);
        return result;
    }

    private DataQuotaInvoice buildQueryWrapperByFileId(String fileId) {
        LambdaQueryWrapper<DataQuotaInvoice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DataQuotaInvoice::getFileId, fileId); // 使用 Lambda 表达式
        return baseMapper.selectOne(lambdaQueryWrapper); // 返回查询结果
    }

    private LambdaQueryWrapper<DataQuotaInvoice> buildQueryWrapper(DataQuotaInvoiceBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataQuotaInvoice> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataQuotaInvoice::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), DataQuotaInvoice::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataQuotaInvoice::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataQuotaInvoice::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataQuotaInvoice::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), DataQuotaInvoice::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataQuotaInvoice::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getCompanySeal()), DataQuotaInvoice::getCompanySeal, bo.getCompanySeal());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataQuotaInvoice::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getMoneyUppercase()), DataQuotaInvoice::getMoneyUppercase, bo.getMoneyUppercase());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataQuotaInvoice::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataQuotaInvoice::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增定额发票
     *
     * @param bo 定额发票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataQuotaInvoiceBo bo) {
        DataQuotaInvoice add = MapstructUtils.convert(bo, DataQuotaInvoice.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改定额发票
     *
     * @param bo 定额发票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataQuotaInvoiceBo bo) {
        DataQuotaInvoice update = MapstructUtils.convert(bo, DataQuotaInvoice.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataQuotaInvoice entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除定额发票信息
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
