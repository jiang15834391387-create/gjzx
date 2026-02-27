package org.smartlink.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartlink.common.core.domain.R;
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
import org.smartlink.workflow.domain.TestActHistory;
import org.smartlink.workflow.domain.TestExpenseReimbursement;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.WfDefinitionConfig;
import org.smartlink.workflow.domain.bo.TestExpenseReimbursementBo;
import org.smartlink.workflow.domain.vo.ActHistoryInfoVo;
import org.smartlink.workflow.domain.vo.ConsumptionDetailsVo;
import org.smartlink.workflow.domain.vo.DetailsVo;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;
import org.smartlink.workflow.domain.vo.form.BpmFormVo;
import org.smartlink.workflow.mapper.TestActHistoryMapper;
import org.smartlink.workflow.mapper.TestExpenseReimbursementMapper;
import org.smartlink.workflow.mapper.TestFormManageMapper;
import org.smartlink.workflow.mapper.WfDefinitionConfigMapper;
import org.smartlink.workflow.service.BpmFormService;
import org.smartlink.workflow.service.ITestExpenseReimbursementService;
import org.smartlink.workflow.service.IWfContractService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final TestActHistoryMapper actHistoryMapper;
    private final TestActHistoryMapper testActHistoryMapper;
    private final IWfContractService iWfContractService;
    private static final String DRAFT = "draft"; // 草稿
    private static final String IN_PROGRESS = "in_progress"; // 履行中
    private static final String INVALID = "invalid"; // 失效
    private static final String SETTLED = "settled"; // 已结清

    public void contractStatus(String key,Long contractId, String nodeId,Long workflowId,String status){
        iWfContractService.updateStatus(key,contractId,nodeId,workflowId,status);
    }

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

        TestExpenseReimbursement add = null;
        try {
            if(StringUtils.isEmpty(bo.getDataChannel())){
                bo.setDataChannel("pc");
            }

            ArrayList<Map<String, String>> list=null;
            String detailsData = bo.getDetailsData();
            if(!StringUtils.isEmpty(detailsData)){
                List<Map<String, Object>> maps = parseJsonString(detailsData);
                if (CollectionUtils.isEmpty(maps)){
                    new ServiceException("请求参数不全");
                }
                list = joinImageFile(maps);
                if(CollectionUtils.isNotEmpty(list)){
                    validImage(list);
                }
            }

            add = MapstructUtils.convert(bo, TestExpenseReimbursement.class);
            validEntityBeforeSave(add);
            //validFormType(add);
            if (StringUtils.isBlank(add.getStatus())) {
                add.setStatus(BusinessStatusEnum.DRAFT.getStatus());
            }
            TestFormManage byType = getByType(bo.getFromType());
            if(byType==null){
                new RuntimeException("该表单不存在");
            }
            add.setCreateDept(LoginHelper.getDeptId());
            add.setFromManageId(byType.getId());
            boolean  flag = baseMapper.insert(add) > 0;
            if (flag) {
                bo.setId(add.getId());
            }

            Long contractId = bo.getContractId();
            String nodeId = bo.getNodeId();
            if (contractId != null&&nodeId!=null){
                contractStatus(bo.getFromType(), contractId,nodeId,add.getId(), "1");
            }

            /*if(CollectionUtils.isNotEmpty(list)){
                dataImageFilesInfoService.bindAndRelieve(add.getId().toString(), list, true);
            }*/
            TestActHistory testActHistory = new TestActHistory();
            testActHistory.setName("草稿");
            testActHistory.setProcessInstanceId(add.getId().toString());
            testActHistory.setStatus("draft");
            testActHistory.setStatusName("草稿");
            testActHistory.setProcessInstanceId(add.getId().toString());
            testActHistory.setAssignee(LoginHelper.getUserId().toString());
            testActHistory.setStartTime(new Date());
            testActHistory.setEndTime(new Date());
            testActHistory.setRunDuration("0秒");

            actHistoryMapper.insert(testActHistory);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return MapstructUtils.convert(add, TestExpenseReimbursementVo.class);
    }

    private ArrayList<Map<String, String>> joinImageFile(List<Map<String, Object>> maps) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Object type = map.get("type");
            if (type!=null&& type.equals("uploadInvoice")) {
                Object jsonString = map.get("value");
                if(jsonString!=null){
                    JSONArray jsonArray = null;
                    if (jsonString instanceof JSONArray) {
                        // 已经是 JSONArray，直接用
                        jsonArray = (JSONArray) jsonString;
                    } else if (jsonString instanceof String) {
                        // 如果是字符串，转一下
                        jsonArray = new JSONArray((String) jsonString);
                    }
                        //JSONArray jsonArray = new JSONArray((List<?>) jsonString);
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                if (jsonObject != null) {
                                    String id = jsonObject.getString("id");
                                    String t = jsonObject.getString("type");
                                    HashMap<String, String> objectObjectHashMap = new HashMap<>();
                                    objectObjectHashMap.put("id", id);
                                    objectObjectHashMap.put("type", t);
                                    list.add(objectObjectHashMap);
                                }
                            }
                        }
                    }
            }
        }
        return list;

    }

    private void validImage(List<Map<String, String>> list) {
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        //校验发票是否使用（待完善）


        Long l = dataImageFilesInfoService.checkFile(list);
        if(l>0){
            throw new ServiceException("不可重复绑定");
        }
    }

    public static List<Map<String, Object>> parseJsonString(String jsonString) {
        List<Map<String, Object>> result = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Map<String, Object> item = new HashMap<>();
            String key = jsonObject.getString("key");
            Object value = jsonObject.get("value");
            String type = jsonObject.getString("type");
            if(StringUtils.isNotEmpty(key)&&  value!=null&& StringUtils.isNotEmpty(type)){
                item.put("key", jsonObject.getString("key"));
                item.put("type", type);
                item.put("value", value);
                result.add(item);
            }
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

            Long contractId = bo.getContractId();
            String nodeId = bo.getNodeId();
            if (contractId != null&&nodeId!=null){
                contractStatus(bo.getFromType(), contractId,nodeId,update.getId(), "1");
            }

            if(i>0){
                String status = update.getStatus();
                if(status.equals("draft")){
                    List<TestActHistory> cg = testActHistoryMapper.selectList(new LambdaQueryWrapper<TestActHistory>().eq(TestActHistory::getProcessInstanceId,bo.getId()).eq(TestActHistory::getStatus, "draft"));
                    if(CollectionUtils.isNotEmpty(cg)){
                        TestActHistory testActHistory = cg.get(0);
                        Date startTime = testActHistory.getStartTime();
                        String duration = getDuration(System.currentTimeMillis() - startTime.getTime());
                        TestActHistory updateData = new TestActHistory();
                        updateData.setName("修改");
                        updateData.setProcessInstanceId(String.valueOf(bo.getId()));
                        updateData.setStatus("update");
                        updateData.setStatusName("修改");
                        updateData.setAssignee(LoginHelper.getUserId().toString());
                        updateData.setStartTime(new Date());
                        updateData.setEndTime(new Date());
                        updateData.setRunDuration("0秒");
                        testActHistoryMapper.insert(updateData);
                    }
                }
            }
            return MapstructUtils.convert(update, TestExpenseReimbursementVo.class);
        } catch (Exception e) {
            log.error("修改失败，执行回滚{}",e);
            throw new RuntimeException("失败");
        }

    }

    private String getDuration(long time) {

        long day = time / (24 * 60 * 60 * 1000);
        long hour = (time / (60 * 60 * 1000) - day * 24);
        long minute = ((time / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long second = (time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);

        if (day > 0) {
            return day + "天" + hour + "小时" + minute + "分钟";
        }
        if (hour > 0) {
            return hour + "小时" + minute + "分钟";
        }
        if (minute > 0) {
            return minute + "分钟";
        }
        if (second > 0) {
            return second + "秒";
        } else {
            return 0 + "秒";
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
        //ArrayList<String> list = new ArrayList<>();
        try {
            baseMapper.delBatchById(ids, LoginHelper.getUserId());
            /*if (CollectionUtils.isNotEmpty(ids)){
                for (Long id : ids){
                    dataImageFilesInfoService.bindAndRelieve(id.toString(), list, false);
                }
            }*/
            List<String> idList = StreamUtils.toList(ids, String::valueOf);
            workflowService.deleteRunAndHisInstance(idList);
            for (Long id : ids){
                TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(id);
                if(testExpenseReimbursement!=null){
                    invoiceStatus(testExpenseReimbursement,null);
                }
                Long contractId = testExpenseReimbursement.getContractId();
                String nodeId = testExpenseReimbursement.getNodeId();
                if (contractId != null&&nodeId!=null){
                    contractStatus(testExpenseReimbursement.getFromType(), contractId,nodeId,testExpenseReimbursement.getId(), "0");
                }

            }
        } catch (Exception e) {
            log.error("删除失败，执行回滚{}",e);
            throw new RuntimeException("失败");
        }

        return isValid;
    }

    @Override
    public List<TestFormManage> byFromId(String type) {
        return testFormManageMapper.selectList(new QueryWrapper<TestFormManage>().eq("form_type",type).eq("is_deleted",0));
    }

    @Override
    public R<Void> setReceiptUrl(TestExpenseReimbursementBo bo) {
        Long id = bo.getId();
        String receiptUrl = bo.getReceiptUrl();
        if(id==null||StringUtils.isEmpty(receiptUrl)){
            return R.fail("请求参数不全");
        }
        TestExpenseReimbursement testExpenseReimbursement = baseMapper.selectById(id);
        if(testExpenseReimbursement==null){
            return R.fail("单据不存在");
        }
        String status = testExpenseReimbursement.getStatus();
        if (!status.equals(BusinessStatusEnum.FINISH.getStatus())){
            return R.fail("单据未完成");
        }
        testExpenseReimbursement.setReceiptUrl(receiptUrl);
        baseMapper.updateById(testExpenseReimbursement);
        return R.ok();
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
                invoiceStatus(testExpenseReimbursement,processEvent.getStatus());
            }
        }
    }

    public void invoiceStatus(TestExpenseReimbursement vo,String status){
        ArrayList<Map<String, String>> idList = getIdList(vo);
        Long id = vo.getId();
        if(CollectionUtils.isNotEmpty(idList)){
            if(!StringUtils.isEmpty(status)){
                if(status.equals(BusinessStatusEnum.DRAFT.getStatus())
                    ||status.equals(BusinessStatusEnum.WAITING.getStatus())){
                    //绑定(参数：List id)
                    dataImageFilesInfoService.bindAndRelieve(id.toString(), idList, false);
                    dataImageFilesInfoService.bindAndRelieve(id.toString(), idList, true);
                }else if (status.equals(BusinessStatusEnum.CANCEL.getStatus())
                    ||status.equals(BusinessStatusEnum.FINISH.getStatus())
                    ||status.equals(BusinessStatusEnum.INVALID.getStatus())
                    ||status.equals(BusinessStatusEnum.TERMINATION.getStatus())){
                    //解绑(参数：List id)
                    dataImageFilesInfoService.bindAndRelieve(id.toString(), idList, false);
                }
            }else {
                //解绑(参数：List id)
                dataImageFilesInfoService.bindAndRelieve(id.toString(), idList, false);
            }
        }
    }

    public ArrayList<Map<String, String>> getIdList(TestExpenseReimbursement bo) {
        ArrayList<Map<String, String>> list=null;
        String detailsData = bo.getDetailsData();
        if(!StringUtils.isEmpty(detailsData)){
                List<Map<String, Object>> maps = parseJsonString(detailsData);
                if (!CollectionUtils.isEmpty(maps)) {
                    list = joinImageFile(maps);
                    if (CollectionUtils.isNotEmpty(list)) {
                        return list;
                    }
                }
        }
        return list;
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
