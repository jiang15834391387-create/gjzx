package org.smartlink.web.service.nc.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.invoice.DataOcrDetails;
import org.smartlink.web.domain.invoice.DataOcrInfo;
import org.smartlink.web.mapper.DataOcrDetailsMapper;
import org.smartlink.web.mapper.DataOcrInfoMapper;
import org.smartlink.web.service.nc.IDataOcrInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private final DataOcrDetailsMapper dataOcrDetailsMapper;

    @Override
    public Boolean deleteByFileId(String fileId) {
        LambdaQueryWrapper<DataOcrInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataOcrInfo::getFileId,fileId);
        return this.baseMapper.delete(queryWrapper) > 0;
    }

    @Override
    public DataOcrInfo getByFileId(String fileId) {
        LambdaQueryWrapper<DataOcrInfo> dataOcrInfoLqw = new LambdaQueryWrapper<>();
        dataOcrInfoLqw.eq(DataOcrInfo::getFileId,fileId);
        DataOcrInfo ocrInfo = this.baseMapper.selectOne(dataOcrInfoLqw);
        if (ocrInfo == null) {
            return null;
        }
        LambdaQueryWrapper<DataOcrDetails> dataOcrDetailsLqw = new LambdaQueryWrapper<>();
        dataOcrDetailsLqw.eq(DataOcrDetails::getFileId,fileId);
        final List<DataOcrDetails> ocrDetailsList = this.dataOcrDetailsMapper.selectList(dataOcrDetailsLqw);
        for (DataOcrDetails dataOcrDetails : ocrDetailsList) {
            dataOcrDetails.setParams(JSONObject.parseObject(dataOcrDetails.getConfidence()));
        }
        ocrInfo.setDetails(ocrDetailsList);
        return ocrInfo;
    }

    @Override
    public Boolean insert(DataOcrInfo ocrInfo) {
        return this.baseMapper.insert(ocrInfo) > 0;
    }
}
