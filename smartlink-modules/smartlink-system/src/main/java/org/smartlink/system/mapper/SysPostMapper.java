package org.smartlink.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.smartlink.common.mybatis.annotation.DataColumn;
import org.smartlink.common.mybatis.annotation.DataPermission;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.system.domain.SysPost;
import org.smartlink.system.domain.vo.SysPostVo;

import java.util.List;

/**
 * 岗位信息 数据层
 *
 * @author Lion Li
 */
public interface SysPostMapper extends BaseMapperPlus<SysPost, SysPostVo> {

    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "create_by")
    })
    Page<SysPostVo> selectPagePostList(@Param("page") Page<SysPostVo> page, @Param(Constants.WRAPPER) Wrapper<SysPost> queryWrapper);

    /**
     * 查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    List<SysPostVo> selectPostsByUserId(Long userId);

}
