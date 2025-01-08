package org.smartlink.business.service.Impl;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.smartlink.business.domain.bo.DataNonTaxBo;
import org.smartlink.business.domain.vo.DataNonTaxVo;
import org.smartlink.business.domain.DataNonTax;
import org.smartlink.business.mapper.DataNonTaxMapper;
import org.smartlink.business.service.IDataNonTaxService;
import org.smartlink.common.core.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 非税收入类票据Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@RequiredArgsConstructor
@Service
public class DataNonTaxServiceImpl implements IDataNonTaxService {

    private final DataNonTaxMapper baseMapper;

    /**
     * 查询非税收入类票据
     *
     * @param id 主键
     * @return 非税收入类票据
     */
    @Override
    public DataNonTaxVo queryById(String id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 分页查询非税收入类票据列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 非税收入类票据分页列表
     */
    @Override
    public TableDataInfo<DataNonTaxVo> queryPageList(DataNonTaxBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<DataNonTax> lqw = buildQueryWrapper(bo);
        Page<DataNonTaxVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的非税收入类票据列表
     *
     * @param bo 查询条件
     * @return 非税收入类票据列表
     */
    @Override
    public List<DataNonTaxVo> queryList(DataNonTaxBo bo) {
        LambdaQueryWrapper<DataNonTax> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataNonTax> buildQueryWrapper(DataNonTaxBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataNonTax> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getFileId()), DataNonTax::getFileId, bo.getFileId());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), DataNonTax::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceNumber()), DataNonTax::getInvoiceNumber, bo.getInvoiceNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceCode()), DataNonTax::getInvoiceCode, bo.getInvoiceCode());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckCode()), DataNonTax::getCheckCode, bo.getCheckCode());
        lqw.eq(StringUtils.isNotBlank(bo.getChecker()), DataNonTax::getChecker, bo.getChecker());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiver()), DataNonTax::getReceiver, bo.getReceiver());
        lqw.eq(StringUtils.isNotBlank(bo.getElectronicMark()), DataNonTax::getElectronicMark, bo.getElectronicMark());
        lqw.eq(StringUtils.isNotBlank(bo.getKind()), DataNonTax::getKind, bo.getKind());
        lqw.eq(StringUtils.isNotBlank(bo.getOtherInfo()), DataNonTax::getOtherInfo, bo.getOtherInfo());
        lqw.eq(StringUtils.isNotBlank(bo.getPayee()), DataNonTax::getPayee, bo.getPayee());
        lqw.eq(StringUtils.isNotBlank(bo.getPayer()), DataNonTax::getPayer, bo.getPayer());
        lqw.eq(StringUtils.isNotBlank(bo.getSocialCreditCode()), DataNonTax::getSocialCreditCode, bo.getSocialCreditCode());
        lqw.eq(StringUtils.isNotBlank(bo.getBlockChain()), DataNonTax::getBlockChain, bo.getBlockChain());
        lqw.eq(StringUtils.isNotBlank(bo.getPaymentCode()), DataNonTax::getPaymentCode, bo.getPaymentCode());
        lqw.eq(StringUtils.isNotBlank(bo.getPayeeCode()), DataNonTax::getPayeeCode, bo.getPayeeCode());
        lqw.eq(StringUtils.isNotBlank(bo.getPayerAccountNumber()), DataNonTax::getPayerAccountNumber, bo.getPayerAccountNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getPayerAccountOpeningBank()), DataNonTax::getPayerAccountOpeningBank, bo.getPayerAccountOpeningBank());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiverAccountNumber()), DataNonTax::getReceiverAccountNumber, bo.getReceiverAccountNumber());
        lqw.eq(StringUtils.isNotBlank(bo.getReceiverAccountOpeningBank()), DataNonTax::getReceiverAccountOpeningBank, bo.getReceiverAccountOpeningBank());
        lqw.eq(StringUtils.isNotBlank(bo.getHandler()), DataNonTax::getHandler, bo.getHandler());
        lqw.eq(StringUtils.isNotBlank(bo.getRegion()), DataNonTax::getRegion, bo.getRegion());
        lqw.eq(bo.getInvoiceTotal() != null, DataNonTax::getInvoiceTotal, bo.getInvoiceTotal());
        lqw.eq(StringUtils.isNotBlank(bo.getTotalWords()), DataNonTax::getTotalWords, bo.getTotalWords());
        lqw.eq(bo.getInvoiceDate() != null, DataNonTax::getInvoiceDate, bo.getInvoiceDate());
        lqw.eq(StringUtils.isNotBlank(bo.getInvoiceStamp()), DataNonTax::getInvoiceStamp, bo.getInvoiceStamp());
        lqw.eq(StringUtils.isNotBlank(bo.getSaveToken()), DataNonTax::getSaveToken, bo.getSaveToken());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataNonTax::getDeleteFlag, bo.getDeleteFlag());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckResult()), DataNonTax::getCheckResult, bo.getCheckResult());
        lqw.eq(StringUtils.isNotBlank(bo.getPushBusinessInfoFlag()), DataNonTax::getPushBusinessInfoFlag, bo.getPushBusinessInfoFlag());
        return lqw;
    }

    /**
     * 新增非税收入类票据
     *
     * @param bo 非税收入类票据
     * @return 是否新增成功
     */
    @Override
    public Boolean insertByBo(DataNonTaxBo bo) {
        DataNonTax add = MapstructUtils.convert(bo, DataNonTax.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改非税收入类票据
     *
     * @param bo 非税收入类票据
     * @return 是否修改成功
     */
    @Override
    public Boolean updateByBo(DataNonTaxBo bo) {
        DataNonTax update = MapstructUtils.convert(bo, DataNonTax.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(DataNonTax entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 校验并批量删除非税收入类票据信息
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
