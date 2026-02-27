package org.smartlink.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.json.utils.JsonUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.system.domain.vo.SysDictDataVo;
import org.smartlink.system.mapper.SysDictDataMapper;
import org.smartlink.workflow.domain.WfContract;
import org.smartlink.workflow.domain.bo.WfContractBo;
import org.smartlink.workflow.domain.vo.WfContractPaymentNodeVo;
import org.smartlink.workflow.domain.vo.WfContractTreeVo;
import org.smartlink.workflow.domain.vo.WfContractVo;
import org.smartlink.workflow.mapper.WfContractMapper;
import org.smartlink.workflow.service.IWfContractService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern TREE_ID_PATTERN = Pattern.compile("^year-(\\d{4})(?:-month-(\\d{1,2})(?:-type-([12]))?)?$");
    private final WfContractMapper baseMapper;
    private final SysDictDataMapper sysDictDataMapper;

    @Override
    public WfContractVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public TableDataInfo<WfContractVo> queryPageList(WfContractBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<WfContract> lqw = buildQueryWrapper(bo);
        lqw.eq(WfContract::getIsDeleted, 0);
        String treeId = bo.getTreeId();
        if(StringUtils.isNotEmpty(treeId)){
            if(!treeId.contains("-")){
                lqw.eq(WfContract::getId, treeId);
            }else {
                applyTreeIdQuery(treeId, lqw);
            }
        }
        Page<WfContractVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    private void applyTreeIdQuery(String treeId, LambdaQueryWrapper<WfContract> lqw) {
        if (StringUtils.isBlank(treeId)) {
            return;
        }
        Matcher matcher = TREE_ID_PATTERN.matcher(treeId);
        if (!matcher.matches()) {
            return;
        }

        String year = matcher.group(1);
        String month = matcher.group(2);
        String type = matcher.group(3);
        if (StringUtils.isNotEmpty(month)){
            String months=month;
            if (10>Integer.parseInt(month)){
                months="0"+month;
            }
            lqw.likeRight(WfContract::getSignDate, year + "-" + months);
        }else if (StringUtils.isNotEmpty(year)){
            lqw.likeRight(WfContract::getSignDate, year);
        }
        if (StringUtils.isNotBlank(type)) {
            lqw.eq(WfContract::getContractType, type);
        }}
    @Override
    public List<WfContractVo> queryList(WfContractBo bo) {
        LambdaQueryWrapper<WfContract> lqw = buildQueryWrapper(bo);
        lqw.eq(WfContract::getIsDeleted, 0);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public List<WfContractTreeVo> treeList(WfContractBo bo) {
        List<WfContractVo> contracts = queryList(bo);
        List<WfContractVo> sortedContracts = contracts.stream()
            .sorted(Comparator.comparing(WfContractVo::getSignDate, Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparing(WfContractVo::getId, Comparator.nullsLast(Comparator.reverseOrder())))
            .toList();

        Map<Integer, List<WfContractVo>> yearGroup = new LinkedHashMap<>();
        for (WfContractVo contract : sortedContracts) {
            LocalDate date = contract.getSignDate() == null
                ? LocalDate.now()
                : contract.getSignDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            yearGroup.computeIfAbsent(date.getYear(), k -> new ArrayList<>()).add(contract);
        }

        List<WfContractTreeVo> yearNodes = new ArrayList<>();
        for (Map.Entry<Integer, List<WfContractVo>> yearEntry : yearGroup.entrySet()) {
            Integer year = yearEntry.getKey();
            String yearId = "year-" + year;

            WfContractTreeVo yearNode = new WfContractTreeVo();
            yearNode.setId(yearId);
            yearNode.setParentId("0");
            yearNode.setNodeType("year");
            yearNode.setLabel(year + "年");
            yearNode.setTotal(yearEntry.getValue().size());

            Map<Integer, List<WfContractVo>> monthGroup = new LinkedHashMap<>();
            for (WfContractVo contract : yearEntry.getValue()) {
                LocalDate date = contract.getSignDate() == null
                    ? LocalDate.now()
                    : contract.getSignDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                monthGroup.computeIfAbsent(date.getMonthValue(), k -> new ArrayList<>()).add(contract);
            }

            List<WfContractTreeVo> monthNodes = new ArrayList<>();
            for (Map.Entry<Integer, List<WfContractVo>> monthEntry : monthGroup.entrySet()) {
                Integer month = monthEntry.getKey();
                String monthId = yearId + "-month-" + month;

                WfContractTreeVo monthNode = new WfContractTreeVo();
                monthNode.setId(monthId);
                monthNode.setParentId(yearId);
                monthNode.setNodeType("month");
                monthNode.setLabel(month + "月");
                monthNode.setTotal(monthEntry.getValue().size());

                List<WfContractTreeVo> typeNodes = new ArrayList<>();
                typeNodes.add(buildTypeNode(monthEntry.getValue(), TYPE_PAY, "付款合同", monthId));
                typeNodes.add(buildTypeNode(monthEntry.getValue(), TYPE_RECEIVE, "收款合同", monthId));
                monthNode.setChildren(typeNodes);
                monthNodes.add(monthNode);
            }

            yearNode.setChildren(monthNodes);
            yearNodes.add(yearNode);
        }
        return yearNodes;
    }
    private WfContractTreeVo buildTypeNode(List<WfContractVo> contracts, String type, String label, String parentId) {
        List<WfContractVo> typeContracts = contracts.stream().filter(c -> type.equals(c.getContractType())).toList();
        WfContractTreeVo typeNode = new WfContractTreeVo();
        typeNode.setId(parentId + "-type-" + type);
        typeNode.setParentId(parentId);
        typeNode.setNodeType("type");
        typeNode.setLabel(label);
        typeNode.setTotal(typeContracts.size());

        List<WfContractTreeVo> contractNodes = new ArrayList<>();
        for (WfContractVo contract : typeContracts) {
            WfContractTreeVo leaf = new WfContractTreeVo();
            leaf.setId("contract-" + contract.getId());
            leaf.setParentId(typeNode.getId());
            leaf.setNodeType("contract");
            leaf.setLabel(contract.getContractName());
            leaf.setContractNo(contract.getContractNo());
            leaf.setContractId(contract.getId());
            leaf.setTotal(1);
            contractNodes.add(leaf);
        }
        typeNode.setChildren(contractNodes);
        return typeNode;
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
        lqw.eq(bo.getSignUserId()!=null, WfContract::getSignUserId, bo.getSignUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getContractType()), WfContract::getContractType, bo.getContractType());
        lqw.eq(StringUtils.isNotBlank(bo.getContractStatus()), WfContract::getContractStatus, bo.getContractStatus());
        lqw.like(StringUtils.isNotBlank(bo.getContractNo()), WfContract::getContractNo, bo.getContractNo());
        lqw.like(StringUtils.isNotBlank(bo.getContractName()), WfContract::getContractName, bo.getContractName());
        lqw.like(StringUtils.isNotBlank(bo.getCompanyName()), WfContract::getCompanyName, bo.getCompanyName());

        lqw.and(StringUtils.isNotBlank(bo.getKeyword()), wrapper -> wrapper
            .like(WfContract::getContractNo, bo.getKeyword())
            .or().like(WfContract::getContractName, bo.getKeyword())
            .or().like(WfContract::getCompanyName, bo.getKeyword()));
        lqw.orderByDesc(WfContract::getCreateTime);
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

    @Override
    public void updateStatus(String key,Long contractId, String nodeId,Long workflowId,String status) {
        List<SysDictDataVo> sysDictDataVos = sysDictDataMapper.selectDictDataByType("hetong");
        if(CollectionUtils.isNotEmpty(sysDictDataVos)){
            AtomicInteger flag= new AtomicInteger();
            sysDictDataVos.forEach(vo -> {
                String dictValue = vo.getDictValue();
                if (dictValue.equals(key)){
                    flag.set(1);

                }
            });
            if (flag.get()==1){
                WfContract wfContract = baseMapper.selectById(contractId);
                if (wfContract != null) {
                    String paymentNodes = wfContract.getPaymentNodes();
                    WfContractPaymentNodeVo wfContractPaymentNodeVo = new WfContractPaymentNodeVo();
                    wfContractPaymentNodeVo.setNodeId(nodeId);
                    wfContractPaymentNodeVo.setIsUsed(status);
                    if(workflowId!=null){
                        wfContractPaymentNodeVo.setWorkFlowId(String.valueOf(workflowId));
                    }else {
                        wfContractPaymentNodeVo.setWorkFlowId("");
                    }
                    String s = updatePaymentNodesJson(paymentNodes, wfContractPaymentNodeVo);
                    wfContract.setPaymentNodes(s);
                    wfContract.setUpdateBy(LoginHelper.getUserId());
                    wfContract.setUpdateTime(new Date());
                    baseMapper.updateById(wfContract);
                }
            }
        }
    }
    /**
     * 解析并修改支付节点JSON。
     *
     * @param paymentNodesJson 原始节点JSON字符串
     * @param updateNode       需要更新的节点参数（按nodeId匹配；若nodeId为空则按nodeName匹配）
     * @return 修改后的节点JSON字符串
     */
    private String updatePaymentNodesJson(String paymentNodesJson, WfContractPaymentNodeVo updateNode) {
        if (updateNode == null) {
            throw new ServiceException("更新节点不能为空");
        }
        List<WfContractPaymentNodeVo> nodes = parsePaymentNodes(paymentNodesJson);
        if (CollUtil.isEmpty(nodes)) {
            throw new ServiceException("支付节点不存在");
        }

        boolean matched = false;
        for (WfContractPaymentNodeVo node : nodes) {
            boolean isMatch = StringUtils.isNotBlank(updateNode.getNodeId())
                ? Objects.equals(node.getNodeId(), updateNode.getNodeId())
                : Objects.equals(node.getNodeName(), updateNode.getNodeName());
            if (!isMatch) {
                continue;
            }
            matched = true;
            if (StringUtils.isNotBlank(updateNode.getNodeId())) {
                node.setNodeId(updateNode.getNodeId());
            }
            if (StringUtils.isNotBlank(updateNode.getNodeName())) {
                node.setNodeName(updateNode.getNodeName());
            }
            if (updateNode.getNodeAmount() != null) {
                node.setNodeAmount(updateNode.getNodeAmount());
            }
            if (StringUtils.isNotBlank(updateNode.getPaymentRatio())) {
                node.setPaymentRatio(updateNode.getPaymentRatio());
            }
            if (StringUtils.isNotBlank(updateNode.getNodeStatus())) {
                node.setNodeStatus(updateNode.getNodeStatus());
            }
            if (updateNode.getPlanDate() != null) {
                node.setPlanDate(updateNode.getPlanDate());
            }
            if (updateNode.getRemark() != null) {
                node.setRemark(updateNode.getRemark());
            }
            if (updateNode.getIsUsed() != null) {
                node.setIsUsed(updateNode.getIsUsed());
            }
            if (updateNode.getWorkFlowId() != null) {
                node.setWorkFlowId(updateNode.getWorkFlowId());
            }
            break;
        }
        if (!matched) {
            throw new ServiceException("支付节点不存在");
        }
        return JsonUtils.toJsonString(nodes);
    }



    private List<WfContractPaymentNodeVo> parsePaymentNodes(String paymentNodes) {
        if (StringUtils.isBlank(paymentNodes)) {
            return new ArrayList<>();
        }
        try {
            return JsonUtils.parseObject(paymentNodes, new TypeReference<List<WfContractPaymentNodeVo>>() {
            });
        } catch (RuntimeException e) {
            throw new ServiceException("支付节点数据格式错误");
        }
    }
}
