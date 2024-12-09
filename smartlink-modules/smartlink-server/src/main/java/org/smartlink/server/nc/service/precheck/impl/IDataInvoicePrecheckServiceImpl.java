package org.smartlink.server.nc.service.precheck.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheck;
import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheckBo;
import org.smartlink.server.nc.domain.precheck.DataInvoicePrecheckVo;
import org.smartlink.server.nc.mapper.DataInvoicePrecheckMapper;
import org.smartlink.server.nc.service.precheck.IDataInvoicePrecheckService;
import org.smartlink.server.nc.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 发票预校验Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class IDataInvoicePrecheckServiceImpl implements IDataInvoicePrecheckService {

    private final DataInvoicePrecheckMapper baseMapper;
    /**
     * 查询发票预校验列表
     *
     * @param bo 发票预校验
     * @return 发票预校验
     */
    @Override
    public List<DataInvoicePrecheckVo> queryList(DataInvoicePrecheckBo bo) {
        LambdaQueryWrapper<DataInvoicePrecheck> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataInvoicePrecheck> buildQueryWrapper(DataInvoicePrecheckBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<DataInvoicePrecheck> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCheckType()), DataInvoicePrecheck::getCheckType, bo.getCheckType());
        lqw.like(StringUtils.isNotBlank(bo.getCheckName()), DataInvoicePrecheck::getCheckName, bo.getCheckName());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckSwitch()), DataInvoicePrecheck::getCheckSwitch, bo.getCheckSwitch());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckContent1()), DataInvoicePrecheck::getCheckContent1, bo.getCheckContent1());
        lqw.eq(StringUtils.isNotBlank(bo.getCheckContent2()), DataInvoicePrecheck::getCheckContent2, bo.getCheckContent2());
        lqw.eq(StringUtils.isNotBlank(bo.getTenantId()), DataInvoicePrecheck::getTenantId, bo.getTenantId());
        return lqw;
    }
}
