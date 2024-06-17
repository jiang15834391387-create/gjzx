package org.smartlink.demo.mapper;

import org.smartlink.common.mybatis.annotation.DataColumn;
import org.smartlink.common.mybatis.annotation.DataPermission;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.demo.domain.TestTree;
import org.smartlink.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTree, TestTreeVo> {

}
