package org.smartlink.workflow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.smartlink.common.mybatis.core.page.PageQuery;

import org.smartlink.workflow.domain.BpmFormDO;
import org.smartlink.workflow.domain.vo.form.BpmFormVo;


import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 动态表单 Service 接口
 *
 * @author  @风里雾里
 */
public interface BpmFormService {

    /**
     * 创建动态表单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createForm(@Valid BpmFormVo createReqVO);

    /**
     * 更新动态表单
     *
     * @param updateReqVO 更新信息
     */
    void updateForm(@Valid BpmFormVo updateReqVO);

    /**
     * 删除动态表单
     *
     * @param id 编号
     */
    void deleteForm(Long id);

    /**
     * 获得动态表单
     *
     * @param id 编号
     * @return 动态表单
     */
    BpmFormVo getForm(Long id);

    /**
     * 获得动态表单
     *
     * @param id 编号
     * @param fromId 编号
     * @return 动态表单
     */
    List<Map<String, String>> getExpenseAccount(Long id, Long fromId);

    /**
     * 获得动态表单列表
     *
     * @return 动态表单列表
     */
    List<BpmFormVo> getFormList();



    /**
     * 获得动态表单分页
     *
     * @param pageReqVO 分页查询
     * @return 动态表单分页
     */
    Page<BpmFormDO> getFormPage(BpmFormDO pageReqVO, PageQuery pageQuery);

}
