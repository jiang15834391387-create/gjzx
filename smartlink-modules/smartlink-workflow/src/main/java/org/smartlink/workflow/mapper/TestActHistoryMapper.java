package org.smartlink.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.smartlink.workflow.domain.TestActHistory;

/**
 * 测试活动历史表 Mapper 接口
 * @author 86158
 */
@Mapper
public interface TestActHistoryMapper extends BaseMapper<TestActHistory> {
    // 空的，使用 BaseMapper 的默认方法
}
