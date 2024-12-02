package org.smartlink.web.service.nc.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.DataCmInfo;
import org.smartlink.web.mapper.DataCmInfoMapper;
import org.smartlink.web.service.nc.IDataCmInfoService;
import org.smartlink.web.utils.BatchIdUtils;
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
}
