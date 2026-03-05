// 路径: org.smartlink.workflow.service.IWfInventoryLedgerService.java
package org.smartlink.workflow.service;

import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.WfInventoryLedger;
import org.smartlink.workflow.domain.vo.WfInventoryLedgerVo;

import java.util.List;

public interface IWfInventoryLedgerService {

    /**
     * 查询出入库台账分页列表
     */
    TableDataInfo<WfInventoryLedgerVo> queryPageList(WfInventoryLedger bo, PageQuery pageQuery);

    /**
     * 查询出入库台账列表 (导出用)
     */
    List<WfInventoryLedgerVo> queryList(WfInventoryLedger bo);

    /**
     * 根据 ID 集合查询台账列表 (PDF 导出用)
     */
    List<WfInventoryLedgerVo> queryListByIds(List<Long> ids);
}
