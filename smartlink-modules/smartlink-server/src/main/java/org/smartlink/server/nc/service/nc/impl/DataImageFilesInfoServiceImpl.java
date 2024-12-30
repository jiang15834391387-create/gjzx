package org.smartlink.server.nc.service.nc.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.nc.constant.Constants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.mapper.DataImageFilesInfoMapper;
import org.smartlink.server.nc.service.nc.IDataImageFilesInfoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
    public List<DataImageFilesInfo> selectAllByType(String type) {
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataImageFilesInfo::getFileType, type);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public DataImageFilesInfo selectById(String fileId) {
        return this.baseMapper.selectById(fileId);
    }
    @Override
    public Boolean updateById(DataImageFilesInfo filesInfo) {
        return this.baseMapper.updateById(filesInfo) > 0;
    }

    /**
     * 根据文件ID查询
     *
     * @param fileIds fileIds
     * @return {@link DataImageFilesInfo}
     */
    @Override
    public List<DataImageFilesInfo> selectDataImageFilesInfoListByFileIdList(List<String> fileIds) {
        if (CollUtil.isEmpty(fileIds)) {
            return Collections.emptyList();
        }
        final LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DataImageFilesInfo::getFileId, fileIds);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<DataImageFilesInfo> selectByBatchIdAndCip(String batchId, String cip) {
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataImageFilesInfo::getBatchId, batchId);
        queryWrapper.and(u->u.ne(DataImageFilesInfo::getCip, Constants.YBZ).or().isNull(DataImageFilesInfo::getCip));
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<DataImageFilesInfo> fuzzySelectAllByBarCode(String barCode) {
        LambdaQueryWrapper<DataImageFilesInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(DataImageFilesInfo::getBarCode, barCode);
        return this.baseMapper.fuzzySelectAllByBarCode(queryWrapper);
    }

    @Override
    public Boolean insert(DataImageFilesInfo filesInfo) {
        return this.baseMapper.insert(filesInfo) > 0;
    }

    @Override
    public Boolean deleteById(String fileId) {
        return this.baseMapper.deleteById(fileId) > 0;
    }
}
