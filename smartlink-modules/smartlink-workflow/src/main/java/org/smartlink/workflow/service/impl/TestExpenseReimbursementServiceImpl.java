package org.smartlink.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartlink.common.core.domain.dto.RoleDTO;
import org.smartlink.common.core.domain.event.ProcessEvent;
import org.smartlink.common.core.domain.event.ProcessTaskEvent;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.enums.BusinessStatusEnum;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.service.WorkflowService;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.StreamUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.satoken.utils.LoginHelper;
import org.smartlink.system.domain.SysUser;
import org.smartlink.system.mapper.SysDictDataMapper;
import org.smartlink.system.mapper.SysUserMapper;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.WfDefinitionConfig;
import org.smartlink.workflow.domain.bo.TestExpenseReimbursementBo;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;
import org.smartlink.workflow.mapper.TestExpenseReimbursementMapper;
import org.smartlink.workflow.mapper.TestFormManageMapper;
import org.smartlink.workflow.mapper.WfDefinitionConfigMapper;
import org.smartlink.workflow.service.ITestExpenseReimbursementService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 费用报销申请Service业务层处理
 *
 * @author lili
 * @date 2025-01-07
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TestExpenseReimbursementServiceImpl implements ITestExpenseReimbursementService {

    private final TestExpenseReimbursementMapper baseMapper;
    private final SysDictDataMapper sysDictDataMapper;
    private final WfDefinitionConfigMapper wfDefinitionConfigMapper;
    private final TestFormManageMapper testFormManageMapper;
    private final SysUserMapper userMapper;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final WorkflowService workflowService;
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
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), TestExpenseReimbursement::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryType()), TestExpenseReimbursement::getCategoryType, bo.getCategoryType());
        lqw.eq(StringUtils.isNotBlank(bo.getFromType()), TestExpenseReimbursement::getFromType, bo.getFromType());
        lqw.orderByDesc(BaseEntity::getCreateTime);
        String deviceType = bo.getDeviceType();
        /*if(deviceType.equals("app")){
            lqw.eq(TestExpenseReimbursement::getCreateBy,LoginHelper.getUserId());
        }*/
        List<Long> loginUser = getLoginUser(LoginHelper.getLoginUser());
        if(loginUser!=null&&loginUser.size()>0){
            lqw.in(TestExpenseReimbursement::getCreateBy,loginUser);
        }
        Page<TestExpenseReimbursementVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }


    public List<Long> getLoginUser(LoginUser user){
        ArrayList<Long> list = new ArrayList<>();
        List<RoleDTO> roles = user.getRoles();
        if(CollectionUtils.isNotEmpty(roles)){
            AtomicReference<Integer> dataScope= new AtomicReference<>(5);
            roles.forEach(a->{
                String scope = a.getDataScope();
                if (!StringUtils.isEmpty(scope)){
                    Integer l = Integer.valueOf(scope);
                    if(l< dataScope.get()){
                        dataScope.set(l);
                    }
                }
            });
            Integer scope = dataScope.get();
            if(scope==1||scope==4){
                return null;
            }else if (scope==3){
                Long deptId = user.getDeptId();
                List<SysUser> deptId1 = userMapper.selectList(new QueryWrapper<SysUser>().eq("dept_id", deptId));
                if(deptId1!=null&&deptId1.size()>0){
                    for (SysUser sysUser : deptId1) {
                        list.add(sysUser.getUserId());
                    }
                }
            }else {
                list.add(user.getUserId());
            }

        }
        return list;
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
        lqw.eq(StringUtils.isNotBlank(bo.getFromType()), TestExpenseReimbursement::getFromType, bo.getFromType());
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryType()), TestExpenseReimbursement::getCategoryType, bo.getCategoryType());
        lqw.eq(StringUtils.isNotBlank(bo.getStatus()), TestExpenseReimbursement::getStatus, bo.getStatus());
        lqw.like(StringUtils.isNotBlank(bo.getCategoryName()), TestExpenseReimbursement::getCategoryName, bo.getCategoryName());
        lqw.eq(bo.getCreateTime() != null, TestExpenseReimbursement::getCreateTime, bo.getCreateTime());
        lqw.like(StringUtils.isNotBlank(bo.getFromName()), TestExpenseReimbursement::getFromName, bo.getFromName());
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
    public TestExpenseReimbursementVo insertByBo(TestExpenseReimbursementBo bo) {

        if(StringUtils.isEmpty(bo.getDataChannel())){
            bo.setDataChannel("pc");
        }
        String detailsData = bo.getDetailsData();
        if(StringUtils.isEmpty(detailsData)){
            new ServiceException("请求参数不全");
        }
        List<Map<String, String>> maps = parseJsonString(detailsData);
        if (CollectionUtils.isEmpty(maps)){
            new ServiceException("请求参数不全");
        }
        ArrayList<String> list = joinImageFile(maps);
        validImage(list);
        TestExpenseReimbursement add = MapstructUtils.convert(bo, TestExpenseReimbursement.class);
        validEntityBeforeSave(add);
        //validFormType(add);
        if (StringUtils.isBlank(add.getStatus())) {
            add.setStatus(BusinessStatusEnum.DRAFT.getStatus());
        }
        TestFormManage byType = getByType(bo.getFromType());
        if(byType==null){
            new ServiceException("该表单不存在");
        }
        add.setCreateDept(LoginHelper.getDeptId());
        add.setFromManageId(byType.getId());
        boolean  flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        if(CollectionUtils.isNotEmpty(list)){
            dataImageFilesInfoService.bindAndRelieve(add.getId().toString(), list, true);
        }
        return MapstructUtils.convert(add, TestExpenseReimbursementVo.class);


    }

    private ArrayList<String> joinImageFile(List<Map<String, String>> maps) {
        ArrayList<String> list = new ArrayList<>();
        for (Map<String, String> map : maps) {
            String type = map.get("type");
            if (StringUtils.isNotBlank(type)&& type.equals("uploadInvoice")) {
                String s = map.get("value");
                if(!StringUtils.isEmpty(s)){
                    String[] urls = s.split(",");
                    list.addAll(Arrays.asList(urls));
                }
            }
        }
        return list;

    }

    private void validImage(List<String> list) {
        Long l = dataImageFilesInfoService.checkFile(list);
        if(l>0){
            throw new ServiceException("不可重复绑定");
        }
    }

    public static List<Map<String, String>> parseJsonString(String jsonString) {
        List<Map<String, String>> result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, String> item = new HashMap<>();
            item.put("key", jsonObject.getString("key"));
            item.put("type", jsonObject.getString("type"));
            item.put("value", jsonObject.getString("value"));
            result.add(item);
        }
        return result;
    }

    /**
     * 修改费用报销申请
     *
     * @param bo 费用报销申请
     * @return 是否修改成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TestExpenseReimbursementVo updateByBo(TestExpenseReimbursementBo bo) {
        TestExpenseReimbursement update = MapstructUtils.convert(bo, TestExpenseReimbursement.class);
        updValidEntityBeforeSave(update);
        TestFormManage byType = getByType(bo.getFromType());
        if(byType==null){
            new ServiceException("该表单不存在");
        }
        update.setFromManageId(byType.getId());
        try {
            int i = baseMapper.updateById(update);
            return MapstructUtils.convert(update, TestExpenseReimbursementVo.class);
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

    private TestFormManage getByType(String type){
        TestFormManage testFormManage = testFormManageMapper.selectOne(new QueryWrapper<TestFormManage>().eq("form_type",type).eq("is_deleted",0));
        if(testFormManage!=null){
            return testFormManage;
        }
        return null;
    }

    private void validFormType(TestExpenseReimbursement entity){
        String fromType = entity.getFromType();
        if(StringUtils.isEmpty(fromType)){
            throw new ServiceException("请先配置对应的工作流程");
        }
        List<WfDefinitionConfig> wfDefinitionConfigs = wfDefinitionConfigMapper.selectList();

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
            if (CollectionUtils.isNotEmpty(ids)){
                for (Long id : ids){
                    dataImageFilesInfoService.bindAndRelieve(id.toString(), null, false);
                }
            }
            List<String> idList = StreamUtils.toList(ids, String::valueOf);
            workflowService.deleteRunAndHisInstance(idList);
        } catch (Exception e) {
            log.error("删除失败，执行回滚{}",e);
            throw new RuntimeException("失败");
        }

        return isValid;
    }


    //@EventListener(condition = "#processEvent.key.startsWith('bxd')")
    public void processHandler(ProcessEvent processEvent) {
        log.info("当前任务执行了{}", processEvent.toString());
        TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(Long.valueOf(processEvent.getBusinessKey()));
        if(testExpenseReimbursement!=null){
            testExpenseReimbursement.setStatus(processEvent.getStatus());
            if (processEvent.isSubmit()) {
                testExpenseReimbursement.setStatus(BusinessStatusEnum.WAITING.getStatus());
            }
            baseMapper.updateById(testExpenseReimbursement);
        }
    }


    //@EventListener(condition = "#processTaskEvent.key.startsWith('bxd')")
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






    @EventListener(ProcessEvent.class)
    public void processHandler2(ProcessEvent processEvent) {
        log.info("当前任务执行了{}", processEvent.toString());
        List<WfDefinitionConfig> list = getWfDefinitionConfigById(processEvent.getKey());
        if(!CollectionUtils.isEmpty(list)){
            TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(Long.valueOf(processEvent.getBusinessKey()));
            if(testExpenseReimbursement!=null){
                testExpenseReimbursement.setStatus(processEvent.getStatus());
                if (processEvent.isSubmit()) {
                    testExpenseReimbursement.setStatus(BusinessStatusEnum.WAITING.getStatus());
                }
                baseMapper.updateById(testExpenseReimbursement);
            }
        }
    }


    @EventListener(ProcessTaskEvent.class)
    public void processTaskHandler2(ProcessTaskEvent processTaskEvent) {
        List<WfDefinitionConfig> list = getWfDefinitionConfigById(processTaskEvent.getKey());
        if(!CollectionUtils.isEmpty(list)){
           log.info("当前任务执行了{}", processTaskEvent.toString());
           TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(Long.valueOf(processTaskEvent.getBusinessKey()));
           if(testExpenseReimbursement!=null){
               testExpenseReimbursement.setStatus(BusinessStatusEnum.WAITING.getStatus());
               baseMapper.updateById(testExpenseReimbursement);
           }

        }
    }

    public List<WfDefinitionConfig> getWfDefinitionConfigById(String key) {
        List<WfDefinitionConfig> wfDefinitionConfigs = wfDefinitionConfigMapper.selectList(new QueryWrapper<WfDefinitionConfig>().eq("process_key", key));
        return wfDefinitionConfigs;
    }



}
