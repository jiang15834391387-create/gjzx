package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.mapper.DataOcrInfoMapper;
import org.smartlink.web.service.nc.IDataOcrInfoService;
import org.springframework.stereotype.Service;

/**
 * ocr信息Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataOcrInfoServiceImpl implements IDataOcrInfoService {

    private final DataOcrInfoMapper baseMapper;

    @Override
    public Boolean deleteByFileId(String fileId) {
        LambdaQueryWrapper<DataOcrInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataOcrInfo::getFileId,fileId);
        return this.baseMapper.delete(queryWrapper) > 0;
    }
}
