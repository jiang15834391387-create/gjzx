package org.smartlink.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.WfContract;
import org.smartlink.workflow.domain.bo.WfContractBo;
import org.smartlink.workflow.domain.vo.WfContractTreeVo;
import org.smartlink.workflow.domain.vo.WfContractVo;
import org.smartlink.workflow.mapper.WfContractMapper;
import org.smartlink.workflow.service.IWfContractService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class WfContractServiceImpl implements IWfContractService {

    private static final String TYPE_PAY = "1";
    private static final String TYPE_RECEIVE = "2";
    private static final String DRAFT = "draft"; // 草稿
    private static final String IN_PROGRESS = "in_progress"; // 履行中
    private static final String INVALID = "invalid"; // 失效
    private static final String SETTLED = "settled"; // 已结清

    private final WfContractMapper baseMapper;

    @Override
    public WfContractVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<WfContractVo> queryPageList(WfContractBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfContract> lqw = buildQueryWrapper(bo);
        Page<WfContractVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Override
    public List<WfContractVo> queryList(WfContractBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public List<WfContractTreeVo> treeList(WfContractBo bo) {
        List<WfContractVo> contracts = queryList(bo);
        List<WfContractTreeVo> roots = new ArrayList<>();
        roots.add(buildTypeNode(TYPE_PAY, "付款合同", contracts));
        roots.add(buildTypeNode(TYPE_RECEIVE, "收款合同", contracts));
        return roots;
    }

    private WfContractTreeVo buildTypeNode(String type, String label, List<WfContractVo> contracts) {
        WfContractTreeVo typeNode = new WfContractTreeVo();
        typeNode.setId("type-" + type);
        typeNode.setParentId("0");
        typeNode.setNodeType("type");
        typeNode.setLabel(label);

        List<WfContractVo> typeContracts = contracts.stream()
            .filter(c -> type.equals(c.getContractType()))
            .sorted(Comparator.comparing(WfContractVo::getSignDate, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(WfContractVo::getId, Comparator.nullsLast(Comparator.reverseOrder())))
            .toList();

        Map<String, List<WfContractVo>> yearMonthGroup = new LinkedHashMap<>();
        for (WfContractVo contract : typeContracts) {
            LocalDate date = contract.getSignDate() == null
                ? LocalDate.now()
                : contract.getSignDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String yearMonth = String.format("%d-%02d", date.getYear(), date.getMonthValue());
            yearMonthGroup.computeIfAbsent(yearMonth, k -> new ArrayList<>()).add(contract);
        }

        List<WfContractTreeVo> ymNodes = new ArrayList<>();
        for (Map.Entry<String, List<WfContractVo>> entry : yearMonthGroup.entrySet()) {
            WfContractTreeVo ymNode = new WfContractTreeVo();
            ymNode.setId(typeNode.getId() + "-" + entry.getKey());
            ymNode.setParentId(typeNode.getId());
            ymNode.setNodeType("month");
            ymNode.setLabel(entry.getKey());
            ymNode.setTotal(entry.getValue().size());

            List<WfContractTreeVo> contractNodes = new ArrayList<>();
            for (WfContractVo contract : entry.getValue()) {
                WfContractTreeVo leaf = new WfContractTreeVo();
                leaf.setId("contract-" + contract.getId());
                leaf.setParentId(ymNode.getId());
                leaf.setNodeType("contract");
                leaf.setLabel(contract.getContractName());
                leaf.setContractNo(contract.getContractNo());
                leaf.setContractId(contract.getId());
                contractNodes.add(leaf);
            }
            ymNode.setChildren(contractNodes);
            ymNodes.add(ymNode);
        }
        typeNode.setChildren(ymNodes);
        typeNode.setTotal(typeContracts.size());
        return typeNode;
    }

    private LambdaQueryWrapper<WfContract> buildQueryWrapper(WfContractBo bo) {
        LambdaQueryWrapper<WfContract> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getContractType()), WfContract::getContractType, bo.getContractType());
        lqw.eq(StringUtils.isNotBlank(bo.getContractStatus()), WfContract::getContractStatus, bo.getContractStatus());
        lqw.like(StringUtils.isNotBlank(bo.getContractNo()), WfContract::getContractNo, bo.getContractNo());
        lqw.like(StringUtils.isNotBlank(bo.getContractName()), WfContract::getContractName, bo.getContractName());
        lqw.like(StringUtils.isNotBlank(bo.getCompanyName()), WfContract::getCompanyName, bo.getCompanyName());
        lqw.and(StringUtils.isNotBlank(bo.getKeyword()), wrapper -> wrapper
            .like(WfContract::getContractNo, bo.getKeyword())
            .or().like(WfContract::getContractName, bo.getKeyword())
            .or().like(WfContract::getCompanyName, bo.getKeyword()));
        lqw.orderByDesc(WfContract::getSignDate, WfContract::getCreateTime, WfContract::getId);
        return lqw;
    }

    @Override
    public Boolean insertByBo(WfContractBo bo) {
        WfContract add = MapstructUtils.convert(bo, WfContract.class);
        //calcUnpaidAmount(add);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Override
    public Boolean updateByBo(WfContractBo bo) {
        WfContract update = MapstructUtils.convert(bo, WfContract.class);
        //calcUnpaidAmount(update);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /*private void calcUnpaidAmount(WfContract entity) {
        if (entity.getContractAmount() == null) {
            entity.setContractAmount(BigDecimal.ZERO);
        }
        if (entity.getPaidAmount() == null) {
            entity.setPaidAmount(BigDecimal.ZERO);
        }
        entity.setUnpaidAmount(entity.getContractAmount().subtract(entity.getPaidAmount()));
        if (entity.getContractAmount().compareTo(BigDecimal.ZERO) > 0) {
            int progress = entity.getPaidAmount().multiply(BigDecimal.valueOf(100))
                .divide(entity.getContractAmount(), 0, RoundingMode.HALF_UP)
                .intValue();
            entity.setProgress(Math.min(Math.max(progress, 0), 100));
        } else {
            entity.setProgress(0);
        }
    }*/

    private void validEntityBeforeSave(WfContract entity) {
        if (!TYPE_PAY.equals(entity.getContractType()) && !TYPE_RECEIVE.equals(entity.getContractType())) {
            throw new IllegalArgumentException("合同类型仅支持 1(付款合同) 或 2(收款合同)");
        }
    }

    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (CollUtil.isEmpty(ids)) {
            return true;
        }
        ArrayList<WfContract> wfContracts = new ArrayList<>();
        for (Long id : ids){
            WfContract wfContract = new WfContract();
            wfContract.setId(id);
            wfContract.setIsDeleted(1);
            wfContracts.add(wfContract);
        }
        return baseMapper.updateBatchById(wfContracts);
    }
}
