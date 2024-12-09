package org.smartlink.server.nc.service.nc.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.utils.DateUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.constant.ParamConstants;
import org.smartlink.server.nc.constant.TaskStateConstants;
import org.smartlink.server.nc.domain.*;
import org.smartlink.server.nc.domain.dto.TaskSubmitDTO;
import org.smartlink.server.nc.domain.dto.UpdateTaskDTO;
import org.smartlink.server.nc.domain.invoice.bo.DataCurrentTaskBo;
import org.smartlink.server.nc.domain.scan.response.InitializationResponse;
import org.smartlink.server.nc.domain.vo.DataCmInfoVo;
import org.smartlink.server.nc.domain.vo.DataCurrentTaskVo;
import org.smartlink.server.nc.factory.NcConfigFactory;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.utils.BatchIdUtils;
import org.smartlink.server.nc.utils.BeanUtils;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CurrentTaskServiceImpl implements CurrentTaskService {

    private final IDataCurrentTaskService currentTaskService;
    private final IDataCmInfoService cmInfoService;
    private final LockTemplate lockTemplate;
    private final IDataImageFilesInfoService dataImageFilesInfoService;
    private final IDataCurrentTaskService dataCurrentTaskService;
    private final IDataBillTypeService dataBillTypeService;

    private final CallNcService callNcService;
    private final IDataImageFilesInfoService iDataImageFilesInfoService;
    private final ISysUserService userService;

    private final IDataImageTreeService imageTreeService;

    @Override
    public R<DataCurrentTask> submitTaskState(TaskSubmitDTO taskSubmitDTO) {
        DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(taskSubmitDTO.getBusinessSerialNo());
        if (ObjectUtil.isEmpty(dataCurrentTask)) {
            return R.fail("提交影像状态失败：当前任务不存在，" + taskSubmitDTO.getBusinessSerialNo());
        }
        if (Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_LOCK_CONTROL))
            && !taskSubmitDTO.getVersion().equals(dataCurrentTask.getVersion())) {
            return R.fail("保存影像状态失败,请刷新页面重试！", dataCurrentTask);
        }
        /**
         * 提交单据前需要判断自定义节点下是否有发票
         * 有自定义树节点并且自定义节点下有值，进行下面逻辑，否则提示不允许提交，请在自定义节点下上传发票
         */
        if ((CollectionUtil.isNotEmpty(taskSubmitDTO.getProductNames())) && imageTreeService.verifyImageTree(taskSubmitDTO.getProductNames(), taskSubmitDTO.getBatchId())) {
            return R.fail("保险类必须上传五矿经纪出具的缴费通知书", dataCurrentTask);
        }
        // 与NC业务系统相关逻辑开启，开启则需要将影像相关数据提交给NCC
        boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
        boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
        if (ncEnabled || bipEnabled) {
            // 与业务系统交互的参数开启
            try {
                taskSubmitDTO.setDataCurrentTask(dataCurrentTask);
                dataCurrentTask = NcConfigFactory.instance().submitTaskStateToBusinessService(taskSubmitDTO);
            } catch (Exception e) {
                log.error(ExceptionUtil.getExceptionMessage(e));
                return R.fail(e.getLocalizedMessage());
            }
        } else {
            // 与业务系统交互的参数关闭情况下的处理
            if (StrUtil.equals(dataCurrentTask.getTaskState(), TaskStateConstants.TASK_STATE_BH_BS)) {
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_BS_WC);
            } else if (StrUtil.equals(dataCurrentTask.getTaskState(), TaskStateConstants.TASK_STATE_BS_WC)) {
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_CS_WC);
            } else {
                dataCurrentTask.setTaskState(TaskStateConstants.TASK_STATE_COMPLETE);
            }
        }
        boolean b = dataCurrentTask.updateById();
        if (!b) {
            return R.fail("保存影像状态失败,请刷新页面重试！", dataCurrentTask);
        }
        return R.ok("保存影像状态成功", dataCurrentTask);
    }

    @Override
    public R<DataCurrentTask> batchSubmitTaskState(TaskSubmitDTO taskSubmitDTO) {
        String userId = taskSubmitDTO.getUserId();
        // 与NC业务系统相关逻辑开启，开启则需要将影像相关数据提交给NCC
        Boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
        // 与BIP业务系统相关逻辑开启，开启则需要将影像相关数据提交给BIP
        Boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
        List<String> businessSerialNoList = taskSubmitDTO.getBusinessSerialNoList();
        List<DataCurrentTask> dataCurrentTaskList = new ArrayList<>();
        for (String businessSerialNo : businessSerialNoList) {
            DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
            if (ObjectUtil.isEmpty(dataCurrentTask)) {
                return R.fail("当前单据任务不存在，业务流水号：" + businessSerialNo);
            } else {
                dataCurrentTaskList.add(dataCurrentTask);
            }
        }
        for (DataCurrentTask dataCurrentTask : dataCurrentTaskList) {
            if (ncEnabled) {
                try {
                    taskSubmitDTO.setDataCurrentTask(dataCurrentTask);
                    // 批扫传专岗角色的NcUserId
                    SysUser sysUser = userService.selectUserById(Long.valueOf(userId));
                    if (ObjectUtil.isNotEmpty(sysUser)) {
                        taskSubmitDTO.setUserId(sysUser.getNcUserId());
                    } else {
                        taskSubmitDTO.setUserId(dataCurrentTask.getUserId());
                    }
                    dataCurrentTask = NcConfigFactory.instance().submitTaskStateToBusinessService(taskSubmitDTO);
                } catch (Exception e) {
                    log.error(ExceptionUtil.getExceptionMessage(e));
                    return R.fail(e.getLocalizedMessage());
                }
            } else if (bipEnabled) {
                try {
                    taskSubmitDTO.setDataCurrentTask(dataCurrentTask);
                    taskSubmitDTO.setUserId(dataCurrentTask.getUserId());
                    dataCurrentTask = NcConfigFactory.instance().submitTaskStateToBusinessService(taskSubmitDTO);
                } catch (Exception e) {
                    log.error(ExceptionUtil.getExceptionMessage(e));
                    DataCurrentTask currentTask = new DataCurrentTask();
                    currentTask.setBusinessSerialNo(taskSubmitDTO.getDataCurrentTask().getBusinessSerialNo());
                    currentTask.setMessage(e.getMessage());
                    dataCurrentTaskService.updateDataCurrentTask(currentTask);
                }
            } else {
                String state = TaskStateConstants.TASK_STATE_COMPLETE;
                if (StrUtil.equals(TaskStateConstants.TASK_STATE_BH_BS, dataCurrentTask.getTaskState())) {
                    state = TaskStateConstants.TASK_STATE_BS_WC;
                } else if (StrUtil.equals(TaskStateConstants.TASK_STATE_BH_CS, dataCurrentTask.getTaskState())) {
                    state = TaskStateConstants.TASK_STATE_CS_WC;
                }
                dataCurrentTask.setTaskState(state);
                dataCurrentTaskService.updateDataCurrentTask(dataCurrentTask);
            }
        }
        return R.ok("批量提交影像状态成功");
    }

    @Override
    public R<Void> rejectTaskState(UpdateTaskDTO updateTaskDTO) {
        DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(updateTaskDTO.getBusinessSerialNo());
        if (ObjectUtil.isEmpty(dataCurrentTask)) {
            return R.fail("驳回影像状态失败：当前任务不存在，" + updateTaskDTO.getBusinessSerialNo());
        }
        if (Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_LOCK_CONTROL))
            && !updateTaskDTO.getVersion().equals(dataCurrentTask.getVersion())) {
            return R.fail("保存影像状态失败，数据已过期！");
        }
        // 与NC业务系统相关逻辑开启，开启则需要将影像相关数据提交给NCC
        boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
        boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
        if (ncEnabled || bipEnabled) {
            try {
                updateTaskDTO.setDataCurrentTask(dataCurrentTask);
                NcConfigFactory.instance().rejectTaskStateBusinessService(updateTaskDTO);
            } catch (Exception e) {
                log.error(ExceptionUtil.getExceptionMessage(e));
                return R.fail(e.getLocalizedMessage());
            }
        } else {
            // 与业务系统交互的参数关闭情况下的处理
            dataCurrentTask.setTaskState(updateTaskDTO.getUpdateState());
        }
        //修改图片状态
        //重扫
        if (CollectionUtil.isNotEmpty(updateTaskDTO.getFileIds()) && updateTaskDTO.getUpdateState().equalsIgnoreCase(TaskStateConstants.TASK_STATE_BH_CS)) {
            for (String fileId : updateTaskDTO.getFileIds()) {
                DataImageFilesInfo imageFilesInfo = dataImageFilesInfoService.selectById(fileId);
                imageFilesInfo.setFileFlowStatus("0");
                dataImageFilesInfoService.updateById(imageFilesInfo);
            }
        }
        dataCurrentTask.setRescanFileSum(StrUtil.join(",", updateTaskDTO.getFileIds()));
        //驳回原因
        dataCurrentTask.setOperateSuggest(StringUtils.isNotEmpty(updateTaskDTO.getMessage()) ? updateTaskDTO.getMessage() : "");
        boolean b = dataCurrentTask.updateById();
        if (!b) {
            return R.fail("保存影像状态失败，数据已过期！");
        }
        log.info("影像驳回成功");
        return R.ok("驳回影像状态成功");
    }

    @Override
    public InitializationResponse initialize(String businessSerialNo) {
        InitializationResponse response = new InitializationResponse();
        DataCurrentTaskBo currentTaskBo = new DataCurrentTaskBo();
        currentTaskBo.setBusinessSerialNo(businessSerialNo);
        currentTaskBo.setBillNum(businessSerialNo);
        DataCurrentTask currentTask = selectInsert(currentTaskBo);
        DataCmInfo cmInfo = new DataCmInfo();
        DataCmInfoBo cmInfoBo = new DataCmInfoBo();
        cmInfoBo.setBusinessSerialNo(businessSerialNo);
        List<DataCmInfoVo> cmInfoVos = cmInfoService.queryList(cmInfoBo);
        if (CollectionUtil.isEmpty(cmInfoVos)) {
            cmInfoBo.setId(UUID.randomUUID().toString(true));
            cmInfoBo.setBatchId(BatchIdUtils.getNewBatchId());
            cmInfoBo.setCreateTime(DateUtils.getNowDate());
            cmInfoService.insertByBo(cmInfoBo);
            BeanUtils.copyBeanProp(cmInfo, cmInfoBo);
        } else {
            BeanUtils.copyBeanProp(cmInfo, cmInfoVos.get(0));
        }
        List<DataBillType> dataBillTypeList = dataBillTypeService.listDataBillTypeByTypeCode(currentTask.getPkBillType());
        if (CollectionUtil.isNotEmpty(dataBillTypeList)) {
            response.setDataBillType(dataBillTypeList.get(0));
        }
        response.setDataCurrentTask(currentTask);
        response.setBatchId(cmInfo.getBatchId());
        return response;
    }

    @Override
    public DataCurrentTask selectInsert(DataCurrentTaskBo task) {
        DataCurrentTask currentTask = new DataCurrentTask();
        DataCurrentTaskVo currentTaskVo = currentTaskService.queryById(task.getBusinessSerialNo());
        if (ObjectUtil.isEmpty(currentTaskVo)) {
            LockInfo lock = null;
            try {
                task.setCreateTime(DateUtils.getNowDate());
                lock = lockTemplate.lock(task.getBusinessSerialNo());
                currentTaskService.insertByBo(task);
            } finally {
                lockTemplate.releaseLock(lock);
            }
            BeanUtils.copyBeanProp(currentTask, task);
        } else {
            BeanUtils.copyBeanProp(currentTask, currentTaskVo);
        }
        return currentTask;
    }
}
