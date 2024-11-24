package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.DataCmInfo;
import org.smartlink.web.mapper.DataCmInfoMapper;
import org.smartlink.web.service.nc.IDataCmInfoService;
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
}
