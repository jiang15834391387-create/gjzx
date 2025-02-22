package org.smartlink.workflow.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.BpmFormDO;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.bo.TestFormManageBo;
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.workflow.domain.vo.form.BpmFormFieldRespDTO;
import org.smartlink.workflow.domain.vo.form.BpmFormPageReqVO;
import org.smartlink.workflow.domain.vo.form.BpmFormRespVO;
import org.smartlink.workflow.domain.vo.form.BpmFormSaveReqVO;
import org.smartlink.workflow.mapper.BpmFormMapper;
import org.smartlink.workflow.service.BpmFormService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

/**
 * 动态表单 Service 实现类
 *
 * @author 风里雾里
 */
@Service
@Validated
public class BpmFormServiceImpl implements BpmFormService {

    @Resource
    private BpmFormMapper formMapper;

    @Override
    public Long createForm(BpmFormSaveReqVO createReqVO) {
        // 插入
        BpmFormDO form = MapstructUtils.convert(createReqVO, BpmFormDO.class);
        formMapper.insert(form);
        // 返回
        return form.getId();
    }

    @Override
    public void updateForm(BpmFormSaveReqVO updateReqVO) {
        // 校验存在
        validateFormExists(updateReqVO.getId());
        // 更新
        BpmFormDO updateObj = MapstructUtils.convert(updateReqVO, BpmFormDO.class);
        formMapper.updateById(updateObj);
    }

    @Override
    public void deleteForm(Long id) {
        // 校验存在
        this.validateFormExists(id);
        // 删除
       // formMapper.deleteById(id);
        BpmFormDO bpmFormDO = formMapper.selectById(id);

    }

    private void validateFormExists(Long id) {
        if (formMapper.selectById(id) == null) {
            throw new RuntimeException("动态表单不存在");
        }
    }

    @Override
    public BpmFormDO getForm(Long id) {
        return formMapper.selectById(id);
    }

    @Override
    public List<BpmFormDO> getFormList() {
        return formMapper.selectList();
    }

    @Override
    public List<BpmFormDO> getFormList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return formMapper.selectBatchIds(ids);
    }

    @Override
    public TableDataInfo<BpmFormRespVO> getFormPage(BpmFormRespVO pageReqVO, PageQuery pageQuery) {
        LambdaQueryWrapper<BpmFormDO> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(pageReqVO.getName()), BpmFormDO::getName, pageReqVO.getName());
        Page<BpmFormRespVO> result = formMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);


    }


}
