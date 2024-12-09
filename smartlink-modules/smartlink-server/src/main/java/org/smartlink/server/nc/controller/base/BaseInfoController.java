package org.smartlink.server.nc.controller.base;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.annotation.RepeatSubmit;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.dto.TaskSubmitDTO;
import org.smartlink.server.nc.domain.dto.UpdateTaskDTO;
import org.smartlink.server.nc.service.nc.CurrentTaskService;
import org.smartlink.server.nc.service.nc.NcService;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 同步NC基础数据
 * @author: L
 * @create: 2024-11-20
 **/
@RestController
@RequestMapping("/system/base")
@RequiredArgsConstructor
public class BaseInfoController {

    private final NcService ncService;
    private final CurrentTaskService currentTaskService;

    /**
     * 同步用户信息
     *
     * @return
     */
    @ApiOperation("同步用户信息")
    //@SaCheckPermission("system:base:synchronizeUser")
    @GetMapping("/synchronizeUser")
    public R<Void> synchronizeUser() {
        return ncService.synchronizeUser();
    }

    /**
     * 同步组织机构
     * @return
     */
    @ApiOperation("同步组织机构")
    @GetMapping("/synchronizeDepart")
    //@SaCheckPermission("system:base:synchronizeDepart")
    public R<Void> synchronizeDepart(){
        return ncService.synchronizeDepart();
    }

    /**
     * 同步单据类型
     * @return
     */
    @ApiOperation("同步单据类型信息")
    //@SaCheckPermission("system:base:synchronizeBillType")
    @GetMapping("/synchronizeBillType")
    public R<Void> synchronizeBillType(){
        return ncService.synchronizeBillType();
    }

    @ApiOperation("提交影像状态")
    @PostMapping("/taskSubmit")
    @RepeatSubmit()
    public R<DataCurrentTask> taskSubmit(@RequestBody TaskSubmitDTO taskSubmitDTO){
        return currentTaskService.submitTaskState(taskSubmitDTO);
    }

    @ApiOperation("批量影像任务提交")
    @PostMapping("/batchTaskSubmit")
    public R<DataCurrentTask> batchTaskSubmit(@RequestBody TaskSubmitDTO taskSubmitDTO){
        return currentTaskService.batchSubmitTaskState(taskSubmitDTO);
    }

    @ApiOperation("驳回影像状态")
    @PostMapping("/rejectTaskState")
    @RepeatSubmit()
    public R<Void> rejectTaskState(@RequestBody UpdateTaskDTO updateTaskDTO){
        return currentTaskService.rejectTaskState(updateTaskDTO);
    }
}

