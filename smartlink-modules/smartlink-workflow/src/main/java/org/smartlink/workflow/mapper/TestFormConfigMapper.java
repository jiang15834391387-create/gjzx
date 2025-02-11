package org.smartlink.workflow.mapper;


import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.workflow.domain.TestFormConfig;
import org.smartlink.workflow.domain.vo.TestFormConfigVo;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * 单配置Mapper接口
 *
 * @author Lion Li
 * @date 2025-01-11
 */
public interface TestFormConfigMapper extends BaseMapperPlus<TestFormConfig, TestFormConfigVo> {

    void deletedFromConfig(@Param("list") Collection<Long> list);
}
