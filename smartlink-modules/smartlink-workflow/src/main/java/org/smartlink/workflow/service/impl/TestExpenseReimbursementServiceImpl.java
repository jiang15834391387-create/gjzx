package org.smartlink.workflow.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartlink.business.doman.dto.InvoiceDataDTO;
import org.smartlink.business.doman.dto.StructureDataDTO;
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
import org.smartlink.common.entity.domain.business.domain.*;
import org.smartlink.common.entity.domain.business.mapper.*;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.ocr.constant.InvoiceConstants;
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
    private final DataNonTaxMapper dataNonTaxMapper;
    private final DataImageFilesInfoMapper filesInfoMapper;
    private final DataMotorVehicleSaleMapper motorVehicleSaleMapper;
    private final DataUsedCarSalesMapper usedCarSalesMapper;
    private final DataFlightItineraryMapper flightItineraryMapper;
    private final DataFlightsItineraryDetailMapper flightsItineraryDetailMapper;
    private final DataSteamerTicketMapper steamerTicketMapper;
    private final DataMedicalTreatmentMapper dataMedicalTreatmentMapper;
    private final DataMedicalTreatmentDetailMapper dataMedicalTreatmentDetailMapper;
    private final DataQuotaInvoiceMapper quotaInvoiceMapper;
    private final DataTaxiTicketsMapper taxiTicketsMapper;
    private final DataRailwayTicketMapper railwayTicketMapper;
    private final DataPassengerCarMapper passengerCarMapper;
    private final DataTollRoadsMapper tollRoadsMapper;
    private final DataReceiptMapper dataReceiptMapper;
    private final DataDidiItineraryMapper didiItineraryMapper;
    private final DataDidiItineraryDetailsMapper didiItineraryDetailsMapper;
    private final DataDutyPaidProofMapper paidProofMapper;
    private final DataDutyPaidProofDetailsMapper paidProofDetailsMapper;
    private final DataCustomsImportGoodsDetailMapper customsImportGoodsDetailMapper;
    private final DataCustomsImxportGoodsMapper customsImportGoodsMapper;
    private final DataCustomsExportGoodsMapper customsExportGoodsMapper;
    private final DataCustomsExportGoodsDetailMapper customsExportGoodsDetailMapper;
    private final DataCustomsSpecialPaymentMapper customsSpecialPaymentMapper;
    private final DataElectronicTransportationGoodsMapper paymentMapper;
    private final DataOcrInfoMapper ocrInfoMapper;
    private final DataOcrDetailsMapper ocrDetailsMapper;
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
            /*if(CollectionUtils.isNotEmpty(list)){
                dataImageFilesInfoService.bindAndRelieve(add.getId().toString(), list, true);
            }*/
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


//        Long l = dataImageFilesInfoService.checkFile(list);
//        if(l>0){
//            throw new ServiceException("不可重复绑定");
//        }
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

    @Override
    public R<List<StructureDataDTO>> selectStructureData(List<String> workIds) {
        if (CollectionUtils.isEmpty(workIds)) {
            return R.fail("业务单号不能为空");
        }
        List<StructureDataDTO> dataDTOs = new ArrayList<>();

        for (String workId : workIds) {
            // 查询当前 workId 的银行回单
            TestExpenseReimbursement reimbursement = baseMapper.selectById(workId);
            if (ObjectUtil.isEmpty(reimbursement)) {
                log.info("业务单号: {} 对应的银行回单不存在", workId);
            }
            //查询当前 workId 的所有发票附件
            List<DataImageFilesInfo> filesInfos = filesInfoMapper.selectList(
                new LambdaQueryWrapper<DataImageFilesInfo>()
                    .eq(DataImageFilesInfo::getWorkflowId, workId)
            );
            if (CollectionUtils.isEmpty(filesInfos)) {
                log.info("业务单号: {} 对应的发票附件不存在", workId);
                continue;
            }
            List<InvoiceDataDTO> invoiceData = new ArrayList<>();
            for (DataImageFilesInfo filesInfo : filesInfos) {
                String invoiceType = filesInfo.getInvoice();
                String fileId = filesInfo.getFileId();
                InvoiceDataDTO dataDTO = selectTypeInvoice(invoiceType, fileId);
                dataDTO.setInvoiceImg(filesInfo.getSurl());
                invoiceData.add(dataDTO);
            }
            StructureDataDTO structureDataDTO = new StructureDataDTO();
            structureDataDTO.setReceiptUrl(reimbursement.getReceiptUrl());
            structureDataDTO.setInvoiceInfo(invoiceData);
            dataDTOs.add(structureDataDTO);
        }
        if (CollectionUtils.isEmpty(dataDTOs)) {
            return R.fail("所有业务单号对应的银行回单或发票附件均不存在");
        }
        return R.ok(dataDTOs);
    }

    @Override
    public List<Map<String,String>> selectList() {
        List<SysUser> sysUsers = userMapper.selectList(new QueryWrapper<SysUser>().eq("del_flag", "0").eq("status", "0"));
        if(CollectionUtils.isNotEmpty(sysUsers)){
            List<Map<String,String>> list=new ArrayList<>();
            for (SysUser sysUser : sysUsers){
                Map<String,String> map=new HashMap<>();
                map.put("userId",sysUser.getUserId().toString());
                map.put("userName",sysUser.getUserName());
                map.put("nickName",sysUser.getNickName());
                list.add( map);
            }
            return list;
        }else {
            return null;
        }
    }

    private InvoiceDataDTO selectTypeInvoice(String invoiceType, String fileId) {
        Object info= null;
        switch (invoiceType) {
            //增值税、机打
            case InvoiceConstants.GLORITY_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_ELECTRON_TAX_SPECIAL_CODE:
            case InvoiceConstants.GLORITY_TAX_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_CODE:
            case InvoiceConstants.GLORITY_ELECTRONIC_ROAD_TOLLS_CODE:
            case InvoiceConstants.GLORITY_ROLL_TICKET_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_VAT_SPECIAL_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_ORDINARY_INVOICE_CODE:
            case InvoiceConstants.GLORITY_AIRCRAFT_INVOICE_CODE:
            case InvoiceConstants.DIGITAL_INVOICE_LIST:
                info = ocrInfoMapper.selectOne(new LambdaQueryWrapper<DataOcrInfo>().eq(DataOcrInfo::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //机动车
            case InvoiceConstants.GLORITY_MOTOR_VEHICLE_SALE_CODE:
                info =motorVehicleSaleMapper.selectOne(new LambdaQueryWrapper<DataMotorVehicleSale>().eq(DataMotorVehicleSale::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //航空运输电子客票行程单
            case InvoiceConstants.GLORITY_FLIGHT_ITINERARY_CODE:
                info =flightItineraryMapper.selectOne(new LambdaQueryWrapper<DataFlightItinerary>().eq(DataFlightItinerary::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //二手车
            case InvoiceConstants.GLORITY_USED_CAR_SALES_CODE:
                info =usedCarSalesMapper.selectOne(new LambdaQueryWrapper<DataUsedCarSales>().eq(DataUsedCarSales::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //船票
            case InvoiceConstants.GLORITY_STEAMER_TICKET_CODE:
                info =steamerTicketMapper.selectOne(new LambdaQueryWrapper<DataSteamerTicket>().eq(DataSteamerTicket::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //医疗票明细票
            case InvoiceConstants.MEDICAL_TICKET_DETAILS_CODE:
            case InvoiceConstants.MEDICAL_RECEIPTS_CODE:
                info=dataMedicalTreatmentMapper.selectOne(new LambdaQueryWrapper<DataMedicalTreatment>().eq(DataMedicalTreatment::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //非税收入类发票
            case InvoiceConstants.NON_TAX_REVENUE_RECEIPTS_CODE:
                info =dataNonTaxMapper.selectOne(new LambdaQueryWrapper<DataNonTax>().eq(DataNonTax::getFileId, fileId));
                return new InvoiceDataDTO(null, info);

            //定额发票
            case InvoiceConstants.GLORITY_QUOTA_INVOICE_CODE:
                info =quotaInvoiceMapper.selectOne(new LambdaQueryWrapper<DataQuotaInvoice>().eq(DataQuotaInvoice::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //出租车发票
            case InvoiceConstants.GLORITY_TAXI_TICKETS_CODE:
                info =taxiTicketsMapper.selectOne(new LambdaQueryWrapper<DataTaxiTickets>().eq(DataTaxiTickets::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //火车发票
            case InvoiceConstants.GLORITY_RAILWAY_TICKET_CODE:
                info =railwayTicketMapper.selectOne(new LambdaQueryWrapper<DataRailwayTicket>().eq(DataRailwayTicket::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //客运车发票
            case InvoiceConstants.GLORITY_PASSENGER_TICKET_CODE:
                info =passengerCarMapper.selectOne(new LambdaQueryWrapper<DataPassengerCar>().eq(DataPassengerCar::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //过路费发票
            case InvoiceConstants.GLORITY_TOLL_ROADS_CODE:
                info =tollRoadsMapper.selectOne(new LambdaQueryWrapper<DataTollRoads>().eq(DataTollRoads::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //小票/可报销其他发票
            case InvoiceConstants.GLORITY_RECEIPT_CODE:
            case InvoiceConstants.REIMBURSABLE_OTHER_CODE:
                info =dataReceiptMapper.selectOne(new LambdaQueryWrapper<DataReceipt>().eq(DataReceipt::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //出行发票/滴滴
            case InvoiceConstants.GLORITY_DIDI_ITINERARY_CODE:
                info =didiItineraryMapper.selectOne(new LambdaQueryWrapper<DataDidiItinerary>().eq(DataDidiItinerary::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //完税证明发票
            case InvoiceConstants.GLORITY_DUTY_PAID_PROOF_CODE:
                info =paidProofMapper.selectOne(new LambdaQueryWrapper<DataDutyPaidProof>().eq(DataDutyPaidProof::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //海关进口货物报关单发票
            case InvoiceConstants.CUSTOMS_IMPORTED_GOODS_CODE:
                info =customsImportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsImxportGoods>().eq(DataCustomsImxportGoods::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //海关出口货物报关单发票
            case InvoiceConstants.CUSTOMS_EXPORT_GOODS_CODE:
                info =customsExportGoodsMapper.selectOne(new LambdaQueryWrapper<DataCustomsExportGoods>().eq(DataCustomsExportGoods::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //海关专用缴款书发票
            case InvoiceConstants.CUSTOMS_SPECIAL_PAYMENT_VOUCHER_CODE:
                info =customsSpecialPaymentMapper.selectOne(new LambdaQueryWrapper<DataCustomsSpecialPayment>().eq(DataCustomsSpecialPayment::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            //货物运输电子收款凭证发票
            case InvoiceConstants.ELECTRONIC_PAYMENT_GOODS_TRANSPORTATION_CODE:
                info =paymentMapper.selectOne(new LambdaQueryWrapper<DataElectronicTransportationGoods>().eq(DataElectronicTransportationGoods::getFileId, fileId));
                return new InvoiceDataDTO(null, info);
            default: {
                return new InvoiceDataDTO(null,null);
            }
        }
    }


}
