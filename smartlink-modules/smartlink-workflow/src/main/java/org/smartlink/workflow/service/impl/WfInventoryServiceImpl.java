package org.smartlink.workflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.workflow.domain.WfInventory;
import org.smartlink.workflow.domain.WfInventoryLedger;
import org.smartlink.workflow.domain.bo.WfInventoryBo;
import org.smartlink.workflow.domain.vo.WfInventoryTreeVo;
import org.smartlink.workflow.domain.vo.WfInventoryVo;
import org.smartlink.workflow.mapper.WfInventoryLedgerMapper;
import org.smartlink.workflow.mapper.WfInventoryMapper;
import org.smartlink.workflow.service.IWfInventoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WfInventoryServiceImpl implements IWfInventoryService {

    private final WfInventoryMapper baseMapper;
    private final WfInventoryLedgerMapper ledgerMapper;

    @Override
    public TableDataInfo<WfInventoryVo> queryPageList(WfInventoryBo bo, PageQuery pageQuery) {
        Page<WfInventoryVo> result = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(result);
    }

    @Override
    public List<WfInventoryVo> queryList(WfInventoryBo bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    private LambdaQueryWrapper<WfInventory> buildQueryWrapper(WfInventoryBo bo) {
        LambdaQueryWrapper<WfInventory> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getItemCode()), WfInventory::getItemCode, bo.getItemCode());
        lqw.like(StrUtil.isNotBlank(bo.getItemName()), WfInventory::getItemName, bo.getItemName());
        lqw.eq(StrUtil.isNotBlank(bo.getCategory()), WfInventory::getCategory, bo.getCategory());
        lqw.eq(StrUtil.isNotBlank(bo.getSupplier()), WfInventory::getSupplier, bo.getSupplier());
        lqw.like(StrUtil.isNotBlank(bo.getWarehouseLocation()), WfInventory::getWarehouseLocation, bo.getWarehouseLocation());
        lqw.like(StrUtil.isNotBlank(bo.getRemark()), WfInventory::getRemark, bo.getRemark());
        String warehouseLocation = bo.getWarehouseLocation();
        if (StrUtil.isNotBlank(warehouseLocation)){
            lqw.eq(WfInventory::getWarehouseLocation, warehouseLocation);
        }
        lqw.eq(StringUtils.isNotBlank(bo.getDelFlag()), WfInventory::getDelFlag, bo.getDelFlag());
        lqw.orderByDesc(WfInventory::getDelFlag,WfInventory::getCreateTime);
        return lqw;
    }

    @Override
    public WfInventoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    @Override
    public Boolean insertByBo(WfInventoryBo bo) {
        WfInventory add = new WfInventory();
        add.setItemCode(bo.getItemCode());
        add.setItemName(bo.getItemName());
        add.setCategory(bo.getCategory());
        add.setSpecification(bo.getSpecification());
        add.setUnit(bo.getUnit());
        add.setPrice(bo.getPrice());
        add.setSupplier(bo.getSupplier());
        add.setQuantity(bo.getQuantity());
        add.setWarehouseLocation(bo.getWarehouseLocation());
        add.setMinStock(bo.getMinStock());
        add.setRemark(bo.getRemark());
        return baseMapper.insert(add) > 0;
    }

    @Override
    public Boolean updateByBo(WfInventoryBo bo) {
        WfInventory update = new WfInventory();
        update.setId(bo.getId());
        update.setItemName(bo.getItemName());
        update.setCategory(bo.getCategory());
        update.setSpecification(bo.getSpecification());
        update.setUnit(bo.getUnit());
        update.setPrice(bo.getPrice());
        update.setSupplier(bo.getSupplier());
        update.setQuantity(bo.getQuantity());
        update.setWarehouseLocation(bo.getWarehouseLocation());
        update.setMinStock(bo.getMinStock());
        update.setRemark(bo.getRemark());
        return baseMapper.updateById(update) > 0;
    }

    @Override
    public Boolean deleteWithValidByIds(List<Long> ids) {
        ArrayList<WfInventory> wfInventories = new ArrayList<>();
        ids.forEach(id -> {
            wfInventories.add(new WfInventory() {{
                setId(id);
                setDelFlag("1");
            }});
        });
        return baseMapper.updateBatchById(wfInventories);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean inbound(WfInventoryBo bo) {
        WfInventory inventory = baseMapper.selectById(bo.getId());
        if (inventory == null) {
            throw new ServiceException("未找到对应的库存物品");
        }
        if (bo.getQuantity() == null || bo.getQuantity() <= 0) {
            throw new ServiceException("入库数量必须大于0");
        }

        Integer beforeQty = inventory.getQuantity();
        Integer afterQty = beforeQty + bo.getQuantity();

        inventory.setQuantity(afterQty);
        baseMapper.updateById(inventory);

        recordLedger(bo, 1, beforeQty, afterQty);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean outbound(WfInventoryBo bo) {
        WfInventory inventory = baseMapper.selectById(bo.getId());
        if (inventory == null) {
            throw new ServiceException("未找到对应的库存物品");
        }
        if (bo.getQuantity() == null || bo.getQuantity() <= 0) {
            throw new ServiceException("出库数量必须大于0");
        }
        if (inventory.getQuantity() < bo.getQuantity()) {
            throw new ServiceException("出库失败：库存不足！当前库存：" + inventory.getQuantity());
        }

        Integer beforeQty = inventory.getQuantity();
        Integer afterQty = beforeQty - bo.getQuantity();

        inventory.setQuantity(afterQty);
        baseMapper.updateById(inventory);

        recordLedger(bo, 2, beforeQty, afterQty);
        return true;
    }

    private void recordLedger(WfInventoryBo bo, Integer type, Integer before, Integer after) {
        WfInventoryLedger ledger = new WfInventoryLedger();
        ledger.setInventoryId(bo.getId());
        ledger.setInventoryName(bo.getItemName());
        ledger.setOperationType(type);
        ledger.setOperationCount(bo.getQuantity());
        ledger.setBeforeQuantity(before);
        ledger.setAfterQuantity(after);
        ledger.setRemark(bo.getRemark());
        ledger.setOperationContent(bo.getOperationContent());
        ledger.setCreateByName(LoginHelper.getLoginUser().getNickname());
        ledger.setRemark(bo.getRemark());
        ledgerMapper.insert(ledger);
    }

    // 路径: org.smartlink.workflow.service.impl.WfInventoryServiceImpl.java
// ... 保留前面的其他方法 ...

    @Override
    public List<WfInventoryTreeVo> queryInventoryTree() {
        // 1. 调用自定义方法查询所有的库存数据（包含正常和已下架/逻辑删除的数据）
        List<WfInventory> allList = baseMapper.selectAllList();

        List<WfInventoryTreeVo> treeList = new ArrayList<>();

        if (allList == null || allList.isEmpty()) {
            return treeList;
        }

        // 2. 将数据分为两拨：正常库存 (del_flag = 0) 和 已下架 (del_flag = 1)
        List<WfInventory> activeList = allList.stream()
            .filter(item -> "0".equals(item.getDelFlag()))
            .collect(Collectors.toList());

        List<WfInventory> deletedList = allList.stream()
            .filter(item -> "1".equals(item.getDelFlag()))
            .collect(Collectors.toList());

        // 3. 构建固定的【库存】(未删除) 父节点
        WfInventoryTreeVo activeRoot = new WfInventoryTreeVo();
        activeRoot.setId("ROOT_ACTIVE");
        activeRoot.setLabel("库存");
        // 调用私有方法构建该节点下的分类与物品
        activeRoot.setChildren(buildCategoryTree(activeList, "ACTIVE_"));
        treeList.add(activeRoot);

        // 4. 构建固定的【已下架】(已删除) 父节点
        WfInventoryTreeVo deletedRoot = new WfInventoryTreeVo();
        deletedRoot.setId("ROOT_DELETED");
        deletedRoot.setLabel("已下架");
        // 调用私有方法构建该节点下的分类与物品
        deletedRoot.setChildren(buildCategoryTree(deletedList, "DELETED_"));
        treeList.add(deletedRoot);

        return treeList;
    }

    /**
     * 私有方法：根据传入的物品列表，按分类进行分组，构建二级树形结构
     */
    private List<WfInventoryTreeVo> buildCategoryTree(List<WfInventory> list, String idPrefix) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 按分类(category)进行分组
        Map<String, List<WfInventory>> groupedMap = list.stream()
            .filter(item -> cn.hutool.core.util.StrUtil.isNotBlank(item.getCategory()))
            .collect(Collectors.groupingBy(WfInventory::getCategory));

        List<WfInventoryTreeVo> categoryNodes = new ArrayList<>();

        // 遍历分组，构建 分类 -> 物品
        for (Map.Entry<String, List<WfInventory>> entry : groupedMap.entrySet()) {
            WfInventoryTreeVo categoryNode = new WfInventoryTreeVo();
            // 加前缀防止 active 和 deleted 状态下出现 ID 冲突
            categoryNode.setId("CAT_" + idPrefix + entry.getKey());
            categoryNode.setLabel(entry.getKey());

            List<WfInventoryTreeVo> children = new ArrayList<>();
            for (WfInventory item : entry.getValue()) {
                WfInventoryTreeVo childNode = new WfInventoryTreeVo();
                // 物品叶子节点的 ID 直接等于 itemCode，方便前端直接取值
                childNode.setId(item.getItemCode());
                childNode.setLabel(item.getItemName());
                childNode.setItemCode(item.getItemCode());
                children.add(childNode);
            }
            categoryNode.setChildren(children);
            categoryNodes.add(categoryNode);
        }

        // 处理没有填写分类的数据，统一放到 "未分类" 下
        List<WfInventory> unCategorizedList = list.stream()
            .filter(item -> cn.hutool.core.util.StrUtil.isBlank(item.getCategory()))
            .collect(Collectors.toList());

        if (!unCategorizedList.isEmpty()) {
            WfInventoryTreeVo unCatNode = new WfInventoryTreeVo();
            unCatNode.setId("CAT_" + idPrefix + "UNCLASSIFIED");
            unCatNode.setLabel("未分类");

            List<WfInventoryTreeVo> unCatChildren = new ArrayList<>();
            for (WfInventory item : unCategorizedList) {
                WfInventoryTreeVo childNode = new WfInventoryTreeVo();
                childNode.setId(item.getItemCode());
                childNode.setLabel(item.getItemName());
                childNode.setItemCode(item.getItemCode());
                unCatChildren.add(childNode);
            }
            unCatNode.setChildren(unCatChildren);
            categoryNodes.add(unCatNode);
        }

        return categoryNodes;
    }
}
