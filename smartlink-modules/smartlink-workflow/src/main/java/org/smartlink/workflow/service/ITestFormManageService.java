package org.smartlink.workflow.service;

import org.smartlink.common.core.domain.R;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.workflow.domain.bo.TestFormManageBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.workflow.domain.vo.WfCategoryVo;

import java.util.Collection;
import java.util.List;

/**
 * 单管理Service接口
 *
 * @author Lion Li
 * @date 2025-01-11
 */
public interface ITestFormManageService {

    /**
     * 查询单管理
     *
     * @param id 主键
     * @return 单管理
     */
    TestFormManageVo queryById(Long id);

    /**
     * 分页查询单管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 单管理分页列表
     */
    TableDataInfo<TestFormManageVo> queryPageList(TestFormManageBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的单管理列表
     *
     * @param bo 查询条件
     * @return 单管理列表
     */
    List<TestFormManageVo> queryList(TestFormManageBo bo);

    /**
     * 新增单管理
     *
     * @param bo 单管理
     * @return 是否新增成功
     */
    R<Void> insertByBo(TestFormManageBo bo);

    /**
     * 修改单管理
     *
     * @param bo 单管理
     * @return 是否修改成功
     */
    R<Void> updateByBo(TestFormManageBo bo);

    /**
     * 校验并批量删除单管理信息
     *
     * @param ids     待删除的主键集合
     * @return 是否删除成功
     */
    R<Void> deleteWithValidByIds(Collection<Long> ids);

    List<TestFormManageVo> selectBy(String type);

    List<TestFormManageVo> queryPageListGroup();

    List<TestFormManageVo> selectFrom(Long categoryId);

    List<WfCategoryVo> selectCategory();

    List<TestFormManageVo> selectAllFrom();

    R<Void> updateStatus(Long id, Integer status);
}
