package org.smartlink.web.service.nc.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.constant.TaskStateConstants;
import org.smartlink.web.domain.DataCurrentTask;
import org.smartlink.web.domain.invoice.bo.DataCurrentTaskBo;
import org.smartlink.web.mapper.DataCurrentTaskMapper;
import org.smartlink.web.service.nc.IDataCurrentTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataCurrentTaskServiceImpl implements IDataCurrentTaskService {

    private final DataCurrentTaskMapper baseMapper;

    @Override
    public DataCurrentTask selectDataCurrentTaskByBusinessSerialNo(String businessSerialNo) {
        LambdaQueryWrapper<DataCurrentTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataCurrentTask::getBusinessSerialNo,businessSerialNo);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Boolean insertOrUpdateDataCurrentTaskByBusinessSerialNo(DataCurrentTask dataCurrentTask) {
        DataCurrentTask dataCurrentTaskResult = this.baseMapper.selectById(dataCurrentTask.getBusinessSerialNo());
        if(ObjectUtil.isNotEmpty(dataCurrentTaskResult)){
            return this.baseMapper.updateById(dataCurrentTask)>0;
        }else{
            return this.baseMapper.insert(dataCurrentTask)>0;
        }
    }

    @Override
    public List<DataCurrentTask> getTaskListByUserId(String userId) {
        // 提供给NCC业务使用，查询出该用户的代办任务列表
        LambdaQueryWrapper<DataCurrentTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataCurrentTask::getUserId,userId);
        queryWrapper.in(DataCurrentTask::getTaskState, TaskStateConstants.TASK_STATE_SCAN,TaskStateConstants.CHARGE_BACK,TaskStateConstants.TASK_STATE_BH_BS,TaskStateConstants.TASK_STATE_BH_CS);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 新增任务
     *
     * @param bo 任务
     * @return 结果
     */
    @Override
    public Boolean insertByBo(DataCurrentTaskBo bo) {
        DataCurrentTask add = BeanUtil.toBean(bo, DataCurrentTask.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setBusinessSerialNo(add.getBusinessSerialNo());
        }
        return flag;
    }

    @Override
    public int updateDataCurrentTask(DataCurrentTask dataCurrentTask) {
        return this.baseMapper.updateById(dataCurrentTask);
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(DataCurrentTask entity) {
        //TODO 做一些数据校验,如唯一约束
    }
}
