package org.smartlink.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.event.ProcessEvent;
import org.smartlink.common.core.domain.event.ProcessTaskEvent;
import org.smartlink.common.core.enums.BusinessStatusEnum;
import org.smartlink.common.core.service.DictService;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.system.domain.SysDictData;
import org.smartlink.system.domain.vo.SysDictDataVo;
import org.smartlink.system.mapper.SysDictDataMapper;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.TestLeave;
import org.smartlink.workflow.domain.bo.TestExpenseReimbursementBo;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;
import org.smartlink.workflow.mapper.TestExpenseReimbursementMapper;
import org.smartlink.workflow.service.ITestExpenseReimbursementService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

/**
 * 费用报销申请Service业务层处理
 *
 * @author Lion Li
 * @date 2025-01-07
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TestExpenseReimbursementServiceImpl implements ITestExpenseReimbursementService {

    private final TestExpenseReimbursementMapper baseMapper;
    private final SysDictDataMapper sysDictDataMapper;

    /**
     * 查询费用报销申请
     *
     * @param id 主键
     * @return 费用报销申请
     */
    @Override
    public TestExpenseReimbursementVo queryById(Long id){
        TestExpenseReimbursementVo testExpenseReimbursementVo = baseMapper.selectVoById(id);
        return testExpenseReimbursementVo;
    }

    /**
     * 分页查询费用报销申请列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 费用报销申请分页列表
     */
    @Override
    public TableDataInfo<TestExpenseReimbursementVo> queryPageList(TestExpenseReimbursementBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TestExpenseReimbursement> lqw = buildQueryWrapper(bo);
        lqw.eq( TestExpenseReimbursement::getIsDeleted, 0);
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), TestExpenseReimbursement::getProjectName, bo.getProjectName());
        lqw.like(StringUtils.isNotBlank(bo.getReimbursementUser()), TestExpenseReimbursement::getReimbursementUser, bo.getReimbursementUser());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), TestExpenseReimbursement::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getProjectType()), TestExpenseReimbursement::getProjectType, bo.getProjectType());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        String deviceType = bo.getDeviceType();
        if(deviceType.equals("APP")){
            lqw.eq(TestExpenseReimbursement::getCreateBy,LoginHelper.getUserId());
        }
        Page<TestExpenseReimbursementVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询符合条件的费用报销申请列表
     *
     * @param bo 查询条件
     * @return 费用报销申请列表
     */
    @Override
    public List<TestExpenseReimbursementVo> queryList(TestExpenseReimbursementBo bo) {
        LambdaQueryWrapper<TestExpenseReimbursement> lqw = buildQueryWrapper(bo);
        lqw.eq( TestExpenseReimbursement::getIsDeleted, 0);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TestExpenseReimbursement> buildQueryWrapper(TestExpenseReimbursementBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TestExpenseReimbursement> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getProjectName()), TestExpenseReimbursement::getProjectName, bo.getProjectName());
        lqw.eq(StringUtils.isNotBlank(bo.getProjectType()), TestExpenseReimbursement::getProjectType, bo.getProjectType());
        lqw.eq(StringUtils.isNotBlank(bo.getReimbursementReason()), TestExpenseReimbursement::getReimbursementReason, bo.getReimbursementReason());
        lqw.eq(StringUtils.isNotBlank(bo.getReimbursementUser()), TestExpenseReimbursement::getReimbursementUser, bo.getReimbursementUser());
        lqw.eq(bo.getSubmitTime() != null, TestExpenseReimbursement::getSubmitTime, bo.getSubmitTime());
        lqw.eq(StringUtils.isNotBlank(bo.getCollectionInformation()), TestExpenseReimbursement::getCollectionInformation, bo.getCollectionInformation());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), TestExpenseReimbursement::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增费用报销申请
     *
     * @param bo 费用报销申请
     * @return 是否新增成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(TestExpenseReimbursementBo bo) {
        boolean flag = false;
        try {
            TestExpenseReimbursement add = MapstructUtils.convert(bo, TestExpenseReimbursement.class);
            validEntityBeforeSave(add);
            if (StringUtils.isBlank(add.getStatus())) {
                add.setStatus(BusinessStatusEnum.DRAFT.getStatus());
            }
            add.setCreateDept(LoginHelper.getDeptId());
            flag = baseMapper.insert(add) > 0;
        } catch (Exception e) {
            log.error("新增费用报销申请失败{}",e);
            throw new RuntimeException("失败");
        }
        return flag;
    }

    /**
     * 修改费用报销申请
     *
     * @param bo 费用报销申请
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateByBo(TestExpenseReimbursementBo bo) {
        TestExpenseReimbursement update = MapstructUtils.convert(bo, TestExpenseReimbursement.class);
        updValidEntityBeforeSave(update);
        try {
            int i = baseMapper.updateById(update);
            return true;
        } catch (Exception e) {
            log.error("修改失败，执行回滚{}",e);
            throw new RuntimeException("失败");
        }

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(TestExpenseReimbursement entity){
        entity.setCreateBy(LoginHelper.getUserId());
        entity.setUpdateBy(LoginHelper.getUserId());
        entity.setTenantId(LoginHelper.getTenantId());
    }
    /**
     * 修改前的数据校验
     */
    private void updValidEntityBeforeSave(TestExpenseReimbursement entity){
        entity.setUpdateBy(LoginHelper.getUserId());
        entity.setTenantId(LoginHelper.getTenantId());
    }

    /**
     * 校验并批量删除费用报销申请信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        try {
            baseMapper.delBatchById(ids, LoginHelper.getUserId());
        } catch (Exception e) {
            log.error("删除失败，执行回滚{}",e);
            throw new RuntimeException("失败");
        }

        return isValid;
    }


    @EventListener(condition = "#processEvent.key.startsWith('reimbursement')")
    public void processHandler(ProcessEvent processEvent) {
        log.info("当前任务执行了{}", processEvent.toString());
        TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(Long.valueOf(processEvent.getBusinessKey()));
        testExpenseReimbursement.setStatus(processEvent.getStatus());
        if (processEvent.isSubmit()) {
            testExpenseReimbursement.setStatus(BusinessStatusEnum.WAITING.getStatus());
        }
        baseMapper.updateById(testExpenseReimbursement);
    }


    @EventListener(condition = "#processTaskEvent.key.startsWith('reimbursement')")
    public void processTaskHandler(ProcessTaskEvent processTaskEvent) {
        // 所有demo案例的申请人节点id
        List<String> list = sysDictDataMapper.selectDictList();
        if(list!=null && list.size()>0){
            String [] ids = list.toArray(new String[list.size()]);
            if (StringUtils.equalsAny(processTaskEvent.getTaskDefinitionKey(), ids)) {
                log.info("当前任务执行了{}", processTaskEvent.toString());
                TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(Long.valueOf(processTaskEvent.getBusinessKey()));
                testExpenseReimbursement.setStatus(BusinessStatusEnum.WAITING.getStatus());
                baseMapper.updateById(testExpenseReimbursement);
            }
        }
    }
}
