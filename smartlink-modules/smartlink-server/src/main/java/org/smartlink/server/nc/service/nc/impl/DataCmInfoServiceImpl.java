package org.smartlink.server.nc.service.nc.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.DataCmInfoBo;
import org.smartlink.server.nc.domain.vo.DataCmInfoVo;
import org.smartlink.server.nc.mapper.DataCmInfoMapper;
import org.smartlink.server.nc.service.nc.IDataCmInfoService;
import org.smartlink.server.nc.utils.BatchIdUtils;
import org.smartlink.server.nc.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务、图片中间关联Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataCmInfoServiceImpl implements IDataCmInfoService {

    private final DataCmInfoMapper baseMapper;
    @Override
    public List<DataCmInfo> selectDataCmInfoListByBusinessSerialNoList(List<String> businessSerialNoList) {
        LambdaQueryWrapper<DataCmInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DataCmInfo::getBusinessSerialNo,businessSerialNoList);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<DataCmInfo> selectDataCmInfoByBusinessSerialNo(String businessSerialNo) {
        LambdaQueryWrapper<DataCmInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataCmInfo::getBusinessSerialNo,businessSerialNo);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public DataCmInfo saveCminfo(String barcode) {
        List<DataCmInfo> byBusinessSerialNo = this.findAllByBarCode(barcode);
        if (CollUtil.isEmpty(byBusinessSerialNo)){
            String batchId = BatchIdUtils.getNewBatchId();
            DataCmInfo dataCmInfo = new DataCmInfo();
            dataCmInfo.setBatchId(batchId);
            dataCmInfo.setBusinessSerialNo(barcode);
            dataCmInfo.setUpdateTime(DateUtil.date());
            dataCmInfo.setId(IdUtil.simpleUUID());
            baseMapper.insert(dataCmInfo);
            return dataCmInfo;
        }else{
            return byBusinessSerialNo.get(0);
        }
    }

    @Override
    public List<DataCmInfo> findAllByBarCode(String barCode) {
        final LambdaQueryWrapper<DataCmInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataCmInfo::getBusinessSerialNo, barCode);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<DataCmInfo> findAllByBatchId(String batchId) {
        final LambdaQueryWrapper<DataCmInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataCmInfo::getBatchId, batchId);
        return this.baseMapper.selectList(queryWrapper);
    }

    /**
     * 查询任务、图片中间关联列表
     *
     * @param bo 任务、图片中间关联
     * @return 任务、图片中间关联
     */
    @Override
    public List<DataCmInfoVo> queryList(DataCmInfoBo bo) {
        LambdaQueryWrapper<DataCmInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<DataCmInfo> buildQueryWrapper(DataCmInfoBo bo) {
        LambdaQueryWrapper<DataCmInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getBatchId()), DataCmInfo::getBatchId, bo.getBatchId());
        lqw.eq(StringUtils.isNotBlank(bo.getBusinessSerialNo()), DataCmInfo::getBusinessSerialNo, bo.getBusinessSerialNo());
        lqw.eq(StringUtils.isNotBlank(bo.getDeleteFlag()), DataCmInfo::getDeleteFlag, bo.getDeleteFlag());
        return lqw;
    }

    /**
     * 新增任务、图片中间关联
     *
     * @param bo 任务、图片中间关联
     * @return 结果
     */
    @Override
    public Boolean insertByBo(DataCmInfoBo bo) {
        DataCmInfo add = BeanUtil.toBean(bo, DataCmInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }
    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(DataCmInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }
}
