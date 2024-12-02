package org.smartlink.web.service.nc.impl;

import cn.hutool.core.bean.BeanUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author L
 */
@RequiredArgsConstructor
@Service
public class DataOcrServiceImpl implements IDataOcrService {

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

    @Override
    public R<Void> ocrInsertBaseEntity(String k, BaseEntity t) throws ClassNotFoundException {
        Map<String, Object> map = BeanUtil.beanToMap(t);
        map.put("type", k);
        if (ObjectUtil.isEmpty(map.get("type"))) {
            return R.fail("发票类型为空");
        }
        //插入或更新
        if (StrUtil.isEmpty(InvoiceConstants.INVOICE_ClASS_TYPE.get(k))) {
            return R.ok();
        }
        BaseEntity baseEntity = (BaseEntity) BeanUtil.toBean(map, Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(k)));
        baseEntity.insert();
        List<BaseEntity> list;
        if (baseEntity instanceof DataFlightItinerary) {
            list = (List<BaseEntity>) map.get("dataFlights");
        } else {
            list = (List<BaseEntity>) map.get("details");
        }
        if (list != null) {
            for (BaseEntity entity : list) {
                entity.insertOrUpdate();
            }
        }
        return R.ok();
    }

    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param key        发票类型
     * @param baseEntity 发票实体
     * @return 0 失败 1 成功
     */
    @Override
    public int ocrInsertOrUpdateByBaseEntity(String key, BaseEntity baseEntity) throws Exception {
        Map<String, Object> map = BeanUtil.beanToMap(baseEntity);
        map.put("type", key);
        map.put("isStaging", baseEntity.getBillSaved());

        R<Void> voidR = ocrInsertOrUpdate(map);
        int code = voidR.getCode();
        return code == 200 ? 1 : 0;
    }

    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param map 查询参数  fileId 文件Id , type 发票类型 ,operate 操作类型
     * @return 插入或保存结果
     */
    @Override
    public R<Void> ocrInsertOrUpdate(Map<String, Object> map) throws ClassNotFoundException {
        if (ObjectUtil.isEmpty(map.get("type"))) {
            return R.fail("发票类型为空");
        }
        String type = map.get("type").toString();
        if (ObjectUtil.isNotEmpty(map.get("ocrFileId"))) {
            map.put("fileId", map.get("ocrFileId").toString());
        }
        Object o = map.get("fileId");
        map.remove("type");
        //插入或更新
        if (StrUtil.isEmpty(InvoiceConstants.INVOICE_ClASS_TYPE.get(type))) {
            return R.ok();
        }

        BaseEntity baseEntity = (BaseEntity) BeanUtil.toBean(map, Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(type)));
        QueryWrapper<BaseEntity> query = new QueryWrapper<>();
        query.eq("file_id", o);
        boolean b = baseEntity.update(query);
        if (!b) {
            b = baseEntity.insert();
        }
        List<BaseEntity> list;
        if (baseEntity instanceof DataFlightItinerary) {
            list = (List<BaseEntity>) map.get("dataFlights");
        } else {
            list = (List<BaseEntity>) map.get("details");
        }
        if (list != null) {
            for (BaseEntity entity : list) {
                if (entity instanceof DataFlights) {
                    DataFlights dataFlights = (DataFlights) entity;
                    dataFlights.setFileId(o.toString());
                } else {
                    DataOcrDetails dataOcrDetails = (DataOcrDetails) entity;
                    dataOcrDetails.setFileId(o.toString());
                }
                entity.insertOrUpdate();
            }
        }


        //修改文件类型 根据文件ID
        if (ObjectUtil.isNotEmpty(o)) {
            String fileId = o.toString();
            DataImageFilesInfo dataImageFilesInfo = dataImageFilesInfoMapper.selectById(fileId);
            // 多票据文件不修改文件类型
            if (ObjectUtil.isNotEmpty(dataImageFilesInfo) && !StrUtil.equals(dataImageFilesInfo.getFileType(), InvoiceConstants.INVOICE_MUCH_NCC)) {
                dataImageFilesInfo.setFileType(type);
                //dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_UPDATE);
                dataImageFilesInfoMapper.updateById(dataImageFilesInfo);
            }
        }
        return b ? R.ok() : R.fail();
    }

    @Override
    public List<BaseEntity> queryInvoiceInfoByTypeAndFileId(String invoiceType, String fileId) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (!StrUtil.equals(invoiceType, InvoiceConstants.IMAGE_OTHERS)) {
            BaseEntity baseEntity = (BaseEntity) Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(invoiceType)).newInstance();
            QueryWrapper<BaseEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("file_id", fileId);
            return baseEntity.selectList(queryWrapper);
        } else {
            return null;
        }
    }

    /**
     * 根据文件ID查询发票信息
     *
     * @param fileId 文件Id
     * @return 发票内容
     */
    @Override
    public BaseEntity ocrQueryByFileId(String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        DataImageFilesInfo dataImageFilesInfo = dataImageFilesInfoMapper.selectById(fileId);
        if (ObjectUtil.isEmpty(dataImageFilesInfo)) {
            return null;
        }
        String type = dataImageFilesInfo.getFileType();
        if (StrUtil.isEmpty(InvoiceConstants.INVOICE_ClASS_TYPE.get(type))) {
            return null;
        }
        BaseEntity baseEntity = (BaseEntity) Class.forName(InvoiceConstants.INVOICE_ClASS_TYPE.get(type)).newInstance();
        QueryWrapper<BaseEntity> baseEntityLaqw = new QueryWrapper<>();
        baseEntityLaqw.eq("file_id", fileId);
        baseEntity = baseEntity.selectOne(baseEntityLaqw);
        if (BeanUtil.isNotEmpty(baseEntity)) {
            baseEntity.setOcrFileType(type);
        }
        if (baseEntity instanceof DataOcrInfo) {
            DataOcrInfo dataOcrInfo = (DataOcrInfo) baseEntity;
            Map<String, Object> map = new HashMap();
            map.put("ocr_id", dataOcrInfo.getId());
            List<DataOcrDetails> list = dataOcrDetailsMapper.selectByMap(map);
            dataOcrInfo.setDetails(list);
            return dataOcrInfo;
        }
        if (baseEntity instanceof DataFlightItinerary) {
            DataFlightItinerary dataFlightItinerary = (DataFlightItinerary) baseEntity;
            Map<String, Object> map = new HashMap();
            map.put("ocr_id", dataFlightItinerary.getId());
            List<DataFlights> list = dataFlightsMapper.selectByMap(map);
            dataFlightItinerary.setDataFlights(list);
            return dataFlightItinerary;
        }
        if (baseEntity instanceof DataDidiItinerary) {
            DataDidiItinerary dataDidiItinerary = (DataDidiItinerary) baseEntity;
            Map<String, Object> map = new HashMap();
            map.put("ocr_id", dataDidiItinerary.getId());
            List<DataDidiItineraryDetails> list = dataDidiItineraryDetailsMapper.selectByMap(map);
            dataDidiItinerary.setDetails(list);
            return dataDidiItinerary;
        }
        return baseEntity;
    }
}
