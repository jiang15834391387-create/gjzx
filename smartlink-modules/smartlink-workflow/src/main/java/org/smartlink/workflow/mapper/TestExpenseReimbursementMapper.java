package org.smartlink.workflow.mapper;

import org.apache.ibatis.annotations.Param;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;

import java.util.Collection;
import java.util.List;

/**
 * 费用报销申请Mapper接口
 *
 * @author Lion Li
 * @date 2025-01-07
 */
public interface TestExpenseReimbursementMapper extends BaseMapperPlus<TestExpenseReimbursement, TestExpenseReimbursementVo> {

    void delBatchById(@Param("list")Collection<Long> list, @Param("userId")Long userId);
}
