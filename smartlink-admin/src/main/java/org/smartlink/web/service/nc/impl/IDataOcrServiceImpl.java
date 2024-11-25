package org.smartlink.web.service.nc.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.imagefilesinfo.DataImageTree;
import org.smartlink.web.domain.invoice.*;
import org.smartlink.web.domain.modle.BaseEntity;
import org.smartlink.web.mapper.*;
import org.smartlink.web.service.nc.IDataOcrService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author L
 */
@RequiredArgsConstructor
@Service
public class IDataOcrServiceImpl implements IDataOcrService {

    private final DataImageFilesInfoMapper dataImageFilesInfoMapper;
    private final DataImageTreeMapper dataImageTreeMapper;
    private final DataDidiItineraryDetailsMapper dataDidiItineraryDetailsMapper;
    private final DataFlightsMapper dataFlightsMapper;
    private final DataOcrDetailsMapper dataOcrDetailsMapper;
    @Override
    public R<String> deleteMultipleFile(List<String> fileIds) throws Exception {
        for (String fileId : fileIds) {
            this.deleteFileById(fileId);
        }
        return R.ok();
    }

    private boolean deleteFileById(String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        DataImageFilesInfo filesInfo = dataImageFilesInfoMapper.selectById(fileId);
        if (ObjectUtil.isEmpty(filesInfo)) {
            return false;
        }
        //删除OCR
        if (filesInfo.getFileType().equals(InvoiceConstants.INVOICE_MUCH_NCC) && StrUtil.isNotBlank(filesInfo.getIncludeTypeArr())) {
            String[] split = filesInfo.getIncludeTypeArr().split(",");
            for (String s : split) {
                deleteInvoiceDataByType(s, filesInfo.getFileId());
            }
        } else {
            //单票
            deleteInvoiceDataByType(filesInfo.getFileType(), filesInfo.getFileId());
        }
        // 删除树表 根据fileId
        LambdaQueryWrapper<DataImageTree> dataImageTreeLqw = new LambdaQueryWrapper<>();
        dataImageTreeLqw.eq(DataImageTree::getImageId, fileId);
        dataImageTreeMapper.delete(dataImageTreeLqw);
        return filesInfo.deleteById();
    }

    @Override
    public void deleteInvoiceDataByType(String type, String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (StrUtil.isNotEmpty(type) && StrUtil.isNotEmpty(InvoiceConstants.INVOICE_ClASS_TYPE.get(type))) {
            BaseEntity baseEntity = (BaseEntity) Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(type)).newInstance();
            deleteInvoiceData(baseEntity, fileId);
        }
    }

    /**
     * 删除发票信息
     *
     * @param baseEntity 发票实体
     * @param fileId     文件ID
     */
    @Override
    public void deleteInvoiceData(BaseEntity baseEntity, String fileId) {
        if (baseEntity.getClass().equals(DataDidiItinerary.class)) {
            LambdaQueryWrapper<DataDidiItineraryDetails> dataDidiItineraryDetailsLqw = new LambdaQueryWrapper<>();
            dataDidiItineraryDetailsLqw.eq(DataDidiItineraryDetails::getFileId, fileId);
            dataDidiItineraryDetailsMapper.delete(dataDidiItineraryDetailsLqw);
        }
        if (baseEntity.getClass().equals(DataFlightItinerary.class)) {
            LambdaQueryWrapper<DataFlights> dataFlightsLqw = new LambdaQueryWrapper<>();
            dataFlightsLqw.eq(DataFlights::getFileId, fileId);
            dataFlightsMapper.delete(dataFlightsLqw);
        }
        if (baseEntity.getClass().equals(DataOcrInfo.class)) {
            LambdaQueryWrapper<DataOcrDetails> dataOcrDetailsLqw = new LambdaQueryWrapper<>();
            dataOcrDetailsLqw.eq(DataOcrDetails::getFileId, fileId);
            dataOcrDetailsMapper.delete(dataOcrDetailsLqw);
        }
        QueryWrapper<BaseEntity> baseEntitylqw = new QueryWrapper<>();
        baseEntitylqw.eq("file_id", fileId);
        boolean delete = baseEntity.delete(baseEntitylqw);
    }
    /**
     * 根据文件ID查询发票信息(仅供删除使用)
     */
    @Override
    public List<BaseEntity> ocrQueryByFileIdAll(DataImageFilesInfo filesInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        String type = filesInfo.getFileType();
        if (StrUtil.isEmpty(InvoiceConstants.INVOICE_ClASS_TYPE.get(type))) {
            return Collections.emptyList();
        }
        BaseEntity baseEntity = (BaseEntity) Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(type)).newInstance();
        QueryWrapper<BaseEntity> baseEntityQueryWrapper = new QueryWrapper<>();
        baseEntityQueryWrapper.eq("file_id", filesInfo.getFileId());
        return baseEntity.selectList(baseEntityQueryWrapper);
    }
}
