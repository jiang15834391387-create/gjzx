package org.smartlink.workflow.service;

import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.bo.WfInventoryBo;
import org.smartlink.workflow.domain.vo.WfInventoryTreeVo;
import org.smartlink.workflow.domain.vo.WfInventoryVo;
import java.util.List;

public interface IWfInventoryService {
    TableDataInfo<WfInventoryVo> queryPageList(WfInventoryBo bo, PageQuery pageQuery);
    List<WfInventoryVo> queryList(WfInventoryBo bo);
    WfInventoryVo queryById(Long id);
    Boolean insertByBo(WfInventoryBo bo);
    Boolean updateByBo(WfInventoryBo bo);
    Boolean deleteWithValidByIds(List<Long> ids);
    Boolean inbound(WfInventoryBo bo);
    Boolean outbound(WfInventoryBo bo);
    /**
     * 动态获取库存分类与物品的树形结构
     */
    List<WfInventoryTreeVo> queryInventoryTree();
}
