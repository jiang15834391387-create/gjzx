package org.smartlink.workflow.mapper;

import org.apache.ibatis.annotations.Select;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.workflow.domain.WfInventory;
import org.smartlink.workflow.domain.vo.WfInventoryVo;

import java.util.List;

public interface WfInventoryMapper extends BaseMapperPlus< WfInventory, WfInventoryVo> {

    /**
     * 查询当前租户下所有的库存数据（包含正常和已下架的数据，用于构建树形结构）
     */
    @Select("SELECT * FROM wf_inventory")
    List<WfInventory> selectAllList();
}
