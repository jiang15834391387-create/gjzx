package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.mapper.DataImageFilesInfoMapper;
import org.smartlink.web.service.nc.IDataImageFilesInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * 图片文件Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataImageFilesInfoServiceImpl implements IDataImageFilesInfoService {

    private final DataImageFilesInfoMapper baseMapper;

    @Override
    public List<DataImageFilesInfo> selectAllByBatchIdList(List<String> batchIdList) {
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DataImageFilesInfo::getBatchId, batchIdList);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<DataImageFilesInfo> selectByBatchId(String batchId) {
        final LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataImageFilesInfo::getBatchId, batchId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public DataImageFilesInfo selectById(String fileId) {
        return this.baseMapper.selectById(fileId);
    }
}
