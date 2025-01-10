package org.smartlink.common.entity.domain.business.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.domain.DataReceipt;
import org.smartlink.common.entity.domain.business.domain.bo.DataReceiptBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataReceiptVo;
import org.smartlink.common.entity.domain.business.mapper.DataReceiptMapper;
import org.smartlink.common.entity.domain.business.service.IDataReceiptService;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 小票Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataReceiptServiceImpl implements IDataReceiptService {

    private final DataReceiptMapper baseMapper;

    /**
     * 查询小票
     *
     * @param id 主键
     * @return 小票
     */
    @Override
    public DataReceiptVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询小票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 小票分页列表
     */
    @Override
    public TableDataInfo<DataReceiptVo> queryPageList(DataReceiptBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataReceipt> lqw = buildQueryWrapper(bo);
        Page<DataReceiptVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的小票列表
     *
     * @param bo 查询条件
     * @return 小票列表
     */
    @Override
    public List<DataReceiptVo> queryList(DataReceiptBo bo) {
        LambdaQueryWrapper<DataReceipt> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataReceipt> buildQueryWrapper(DataReceiptBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataReceipt> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCurrencyCode()), DataReceipt::getCurrencyCode, bo.getCurrencyCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceDate()), DataReceipt::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getTime()), DataReceipt::getTime, bo.getTime());
        lqw.eq(StringUtils.isNotBlank(bo.getDiscount()), DataReceipt::getDiscount, bo.getDiscount());
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataReceipt::getFileId, bo.getFileId());
        lqw.like(StringUtils.isNotBlank(bo.getStoreName()), DataReceipt::getStoreName, bo.getStoreName());
        lqw.eq(StringUtils.isNotBlank(bo.getSubTotal()), DataReceipt::getSubTotal, bo.getSubTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getTax()), DataReceipt::getTax, bo.getTax());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataReceipt::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getTips()), DataReceipt::getTips, bo.getTips());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceTotal()), DataReceipt::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getType()), DataReceipt::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getInternationalMark()), DataReceipt::getInternationalMark, bo.getInternationalMark());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataReceipt::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataReceipt::getRegion, bo.getRegion());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataReceipt::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增小票
     *
     * @param bo 小票
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataReceiptBo bo) {
        DataReceipt add = MapstructUtils.convert(bo, DataReceipt.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改小票
     *
     * @param bo 小票
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataReceiptBo bo) {
        DataReceipt update = MapstructUtils.convert(bo, DataReceipt.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataReceipt entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除小票信息
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
