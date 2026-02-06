package org.smartlink.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.workflow.domain.*;
import org.smartlink.workflow.domain.vo.WfCategoryVo;
import org.smartlink.workflow.mapper.BpmFormMapper;
import org.smartlink.workflow.mapper.TestExpenseReimbursementMapper;
import org.smartlink.workflow.mapper.WfCategoryMapper;
import org.smartlink.workflow.service.IWfDefinitionConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.smartlink.workflow.domain.bo.TestFormManageBo;
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.workflow.mapper.TestFormManageMapper;
import org.smartlink.workflow.service.ITestFormManageService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 单管理Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TestFormManageServiceImpl implements ITestFormManageService {

    private final TestFormManageMapper baseMapper;
    private final WfCategoryMapper wfCategoryMapper;
    private final BpmFormMapper bpmFormMapper;
    @Resource
    private TestExpenseReimbursementMapper expenseReimbursementMapper;
    private final IWfDefinitionConfigService wfDefinitionConfigService;
    /**
     * 查询单管理
     *
     * @param id 主键
     * @return 单管理
     */
    @Override
    public TestFormManageVo queryById(Long id){
        TestFormManageVo testFormManageVo = baseMapper.selectVoById(id);
        return testFormManageVo;
    }

    /**
     * 分页查询单管理列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 单管理分页列表
     */
    @Override
public TableDataInfo<TestFormManageVo> queryPageList(TestFormManageBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TestFormManage> lqw = buildQueryWrapper(bo);
        Page<TestFormManageVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的单管理列表
     *
     * @param bo 查询条件
     * @return 单管理列表
     */
    @Override
    public List<TestFormManageVo> queryList(TestFormManageBo bo) {
        LambdaQueryWrapper<TestFormManage> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TestFormManage> buildQueryWrapper(TestFormManageBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TestFormManage> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFormName()), TestFormManage::getFormName, bo.getFormName());
        lqw.like(StringUtils.isNotBlank(bo.getFormBindName()), TestFormManage::getFormBindName, bo.getFormBindName());
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), TestFormManage::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getFormType()), TestFormManage::getFormType, bo.getFormType());
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryType()), TestFormManage::getCategoryType, bo.getCategoryType());
        lqw.eq(TestFormManage::getIsDeleted, 0);
        return lqw;
    }

    /**
     * 新增单管理
     *
     * @param bo 单管理
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> insertByBo(TestFormManageBo bo) {
        try {
            String formName = bo.getFormName();
            String formType = bo.getFormType();
            if(StringUtils.isEmpty(formName) || StringUtils.isEmpty(formType)){
                return R.fail("参数错误");
            }
            List<TestFormManage> nameList = baseMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_name", formName).eq("is_deleted", 0));
            if(nameList!=null && nameList.size()>0){
                return R.fail("表单名称重复");
            }
            List<TestFormManage> testFormManages = baseMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_type", formType).eq("is_deleted", 0));
            if(testFormManages!=null && testFormManages.size()>0){
                return R.fail("表单类型重复");
            }

            TestFormManage add = MapstructUtils.convert(bo, TestFormManage.class);
        boolean flag = false;
            String s = validEntityBeforeSave(add);
            if(!StringUtils.isEmpty(s)){
                return R.fail(s);
            }
            add.setCreateDept(LoginHelper.getDeptId());
            add.setTenantId(LoginHelper.getTenantId());


            List<WfDefinitionConfig> configList=wfDefinitionConfigService.selectTableName(add.getFormType());
            if(CollectionUtils.isNotEmpty(configList)){
                add.setIsBindModel(1);
            }

            flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }
        } catch (Exception e) {
            log.info("添加失败{}",e);
            throw new RuntimeException("失败");
        }
        return R.ok();
    }


    /**
     * 修改单管理
     *
     * @param bo 单管理
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> updateByBo(TestFormManageBo bo) {
        try {
            String formName = bo.getFormName();
            String formType = bo.getFormType();
            if(StringUtils.isEmpty(formName) || StringUtils.isEmpty(formType)){
                return R.fail("参数错误");
            }
            List<TestFormManage> nameList = baseMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_name", formName).ne("id",bo.getId()).eq("is_deleted", 0));
            if(nameList!=null && nameList.size()>0){
                return R.fail("表单名称重复");
            }
            List<TestFormManage> testFormManages = baseMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_type", formType).ne("id",bo.getId()).eq("is_deleted", 0));
            if(testFormManages!=null && testFormManages.size()>0){
                return R.fail("表单类型重复");
            }

            TestFormManage update = MapstructUtils.convert(bo, TestFormManage.class);
            String s = validEntityBeforeSave(update);
            if(!StringUtils.isEmpty(s)){
                return R.fail(s);
            }
            update.setIsBindModel(0);
            List<WfDefinitionConfig> configList=wfDefinitionConfigService.selectTableName(bo.getFormType());
            if(CollectionUtils.isNotEmpty(configList)){
                update.setIsBindModel(1);
            }

            baseMapper.updateById(update);
        } catch (Exception e) {
            log.info("修改失败{}",e);
            throw new RuntimeException("失败");
        }
        return R.ok();
    }

    /**
     * 保存前的数据校验
     */
    private String validEntityBeforeSave(TestFormManage entity){
        Long categoryId = entity.getCategoryId();
        Long formBindId = entity.getFormBindId();
        if(categoryId!=null){
            List<WfCategory> wfCategories = wfCategoryMapper.selectList(new QueryWrapper<WfCategory>().eq("id",categoryId));
            if(wfCategories==null || wfCategories.size()==0){
                return "绑定流程分类类型不存在";
            }
        }
        if(formBindId!=null){
            List<BpmFormDO> bpmFormDOS = bpmFormMapper.selectList(new QueryWrapper<BpmFormDO>().eq("id",formBindId).eq("is_deleted", 0));
            if(bpmFormDOS==null || bpmFormDOS.size()==0){
                return "绑定表单类型不存在";
            }
        }
        return null;
    }

    /**
     * 校验并批量删除单管理信息
     *
     * @param ids     待删除的主键集合
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Void> deleteWithValidByIds(Collection<Long> ids) {
        ids.forEach(aLong -> {
            List<TestExpenseReimbursement> fromId = expenseReimbursementMapper.selectList(
                new QueryWrapper<TestExpenseReimbursement>()
                    .eq("from_manage_id", aLong));
            if(fromId!=null&&fromId.size()>0){
                throw new ServiceException("该表单已被使用无法删除");
            }
        });


        try {
            baseMapper.deletedFromManage(ids,LoginHelper.getUserId(),LoginHelper.getTenantId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return R.ok();
    }

    @Override
    public List<TestFormManageVo> selectBy(String type) {
        List<TestFormManage> formPurposeType = baseMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_type", type));
        if(formPurposeType!=null&&formPurposeType.size()>0){
            List<TestFormManageVo> objects = new ArrayList<>(formPurposeType.size());
            formPurposeType.forEach(formManage->{
                TestFormManageVo testFormManageVo = new TestFormManageVo();
                BeanUtils.copyProperties(formManage,testFormManageVo);
                objects.add(testFormManageVo);
            });
            return objects;
        }
        return null;
    }

    @Override
    public List<TestFormManageVo> queryPageListGroup() {
        LambdaQueryWrapper<TestFormManage> lqw = Wrappers.lambdaQuery();
        lqw.groupBy(TestFormManage::getCategoryType);
        lqw.eq(TestFormManage::getIsDeleted, 0);
        List<TestFormManageVo> testFormManageVos = baseMapper.selectVoList(lqw);
        return testFormManageVos;
    }

    @Override
    public List<TestFormManageVo> selectFrom(Long categoryId) {
        if(categoryId!=null){
            LambdaQueryWrapper<TestFormManage> lqw = Wrappers.lambdaQuery();
            lqw.eq(TestFormManage::getCategoryId, categoryId);
            lqw.eq(TestFormManage::getIsBindModel, 1);
            lqw.eq(TestFormManage::getIsDeleted, 0);
            lqw.eq(TestFormManage::getStatus, 1);
            lqw.orderByDesc(TestFormManage::getSort);
            List<TestFormManageVo> testFormManageVos = baseMapper.selectVoList(lqw);
            return testFormManageVos;
        }
        return List.of();
    }

    @Override
    public List<WfCategoryVo> selectCategory() {
        /*LambdaQueryWrapper<TestFormManage> lqw = Wrappers.lambdaQuery();
        lqw.eq(TestFormManage::getIsBindModel, 1);
        lqw.eq(TestFormManage::getIsDeleted, 0);

        List<TestFormManageVo> testFormManageVos = baseMapper.selectVoList(lqw);
        if(testFormManageVos!=null&&testFormManageVos.size()>0){
            List<TestFormManageVo> uniqueUsersByGender = testFormManageVos.stream()
                .collect(Collectors.collectingAndThen(
                    Collectors.groupingBy(TestFormManageVo::getCategoryId),
                    map -> map.values().stream()
                        .map(list -> list.get(0))
                        .toList()
                ));
            return uniqueUsersByGender;
        }*/


        return wfCategoryMapper.selectVoList();
    }

    @Override
    public List<TestFormManageVo> selectAllFrom() {
        LambdaQueryWrapper<TestFormManage> lqw = Wrappers.lambdaQuery();
        lqw.eq(TestFormManage::getIsBindModel, 1);
        lqw.eq(TestFormManage::getIsDeleted, 0);
        lqw.orderByDesc(TestFormManage::getSort);
        List<TestFormManageVo> testFormManageVos = baseMapper.selectVoList(lqw);
        return testFormManageVos;
    }

    @Override
    public R<Void> updateStatus(Long id, Integer status) {
        baseMapper.update(null,
            new LambdaUpdateWrapper<TestFormManage>()
                .set(TestFormManage::getStatus, status)
                .eq(TestFormManage::getId, id));
        return null;
    }
}
