// 路径: org.smartlink.workflow.service.impl.WfInventoryLedgerServiceImpl.java
package org.smartlink.workflow.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.WfInventory;
import org.smartlink.workflow.domain.WfInventoryLedger;
import org.smartlink.workflow.domain.vo.WfInventoryLedgerVo;
import org.smartlink.workflow.mapper.WfInventoryLedgerMapper;
import org.smartlink.workflow.service.IWfInventoryLedgerService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class WfInventoryLedgerServiceImpl implements IWfInventoryLedgerService {

    private final WfInventoryLedgerMapper baseMapper;

    /**
     * 构建查询条件
     */
    private LambdaQueryWrapper<WfInventoryLedger> buildQueryWrapper(WfInventoryLedger bo) {
        LambdaQueryWrapper<WfInventoryLedger> lqw = Wrappers.lambdaQuery();
        // 根据库存物品 ID 查询
        lqw.eq(bo.getInventoryId() != null, WfInventoryLedger::getInventoryId, bo.getInventoryId());
        // 根据操作类型(入库/出库)查询
        lqw.eq(bo.getOperationType() != null, WfInventoryLedger::getOperationType, bo.getOperationType());
        // 按操作时间倒序排列
        lqw.orderByDesc(WfInventoryLedger::getCreateTime);
        lqw.like(StrUtil.isNotBlank(bo.getRemark()), WfInventoryLedger::getRemark, bo.getRemark());
        return lqw;
    }

    @Override
    public TableDataInfo<WfInventoryLedgerVo> queryPageList(WfInventoryLedger bo, PageQuery pageQuery) {
        Page<WfInventoryLedgerVo> result = baseMapper.selectVoPage(pageQuery.build(), buildQueryWrapper(bo));
        return TableDataInfo.build(result);
    }

    @Override
    public List<WfInventoryLedgerVo> queryList(WfInventoryLedger bo) {
        return baseMapper.selectVoList(buildQueryWrapper(bo));
    }

    @Override
    public List<WfInventoryLedgerVo> queryListByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return baseMapper.selectVoByIds(ids);
    }
}
