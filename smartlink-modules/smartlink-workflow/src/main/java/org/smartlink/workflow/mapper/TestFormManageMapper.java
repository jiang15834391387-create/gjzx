package org.smartlink.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.Collection;
import java.util.List;

/**
 * 单管理Mapper接口
 *
 * @author Lion Li
 * @date 2025-01-11
 */
public interface TestFormManageMapper extends BaseMapperPlus<TestFormManage, TestFormManageVo> {

    void deletedFromManage(@Param("list")Collection<Long> list, @Param("userId")Long userId ,@Param("tenantId")String tenantId);
    List<Long> selectBy(@Param("type")String type ,@Param("tenantId")Long tenantId);
}
