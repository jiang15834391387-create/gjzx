package org.smartlink.server.nc.service.scan.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.*;
import cn.hutool.extra.compress.CompressUtil;
import cn.hutool.extra.compress.extractor.Extractor;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.XML;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.server.nc.constant.*;
import org.smartlink.server.nc.domain.DataCurrentTask;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.dto.NcImageServiceDTO;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageFilesInfoVo;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageTree;
import org.smartlink.server.nc.domain.invoice.DataMotorVehicleSale;
import org.smartlink.server.nc.domain.invoice.DataOcrDetails;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.smartlink.server.nc.domain.invoice.DataUsedCarSales;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.domain.scan.dto.FileUploadDTO;
import org.smartlink.server.nc.domain.scan.dto.XmlInvoiceDTO;
import org.smartlink.server.nc.domain.xietong.DataSynergyAttachment;
import org.smartlink.server.nc.factory.CheckFactory;
import org.smartlink.server.nc.factory.NcConfigFactory;
import org.smartlink.server.nc.helper.LoginHelper;
import org.smartlink.server.nc.mapper.DataCurrentTaskMapper;
import org.smartlink.server.nc.mapper.DataImageFilesInfoMapper;
import org.smartlink.server.nc.ocr.service.bean.IdentificationData;
import org.smartlink.server.nc.ocr.service.factory.OcrFactory;
import org.smartlink.server.nc.oss.factory.OssFactory;
import org.smartlink.server.nc.properties.NcProperties;
import org.smartlink.server.nc.service.brecheck.BreCheckInvoiceService;
import org.smartlink.server.nc.service.document.DocumentFactory;
import org.smartlink.server.nc.service.hardwaremessage.HardWareMessageService;
import org.smartlink.server.nc.service.nc.*;
import org.smartlink.server.nc.service.scan.ScanImageService;
import org.smartlink.server.nc.utils.BatchIdUtils;
import org.smartlink.server.nc.utils.BeanCopyUtils;
import org.smartlink.server.nc.utils.ExceptionUtil;
import org.smartlink.server.nc.utils.GetRequestUtils;
import org.smartlink.server.nc.utils.document.PDFUtil;
import org.smartlink.server.nc.utils.file.FileUtils;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
/**
 * @author L
 * @title 文件上传实现
 * @description 文件上传实现
 * @date
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScanImageServiceImpl implements ScanImageService {

    private final CurrentTaskService currentTaskService;
    private final IDataImageTreeService imageTreeService;
    private final IDataOcrService dataOcrService;
    private final IDataOcrInfoService dataOcrInfoService;
    private final IDataCmInfoService dataCmInfoService;
    private final IDataCurrentTaskService dataCurrentTaskService;
    private final DataCurrentTaskMapper dataCurrentTaskMapper;
    private final DataImageFilesInfoMapper imageFilesInfoMapper;
    private final BreCheckInvoiceService breCheckInvoiceService;
    private final HardWareMessageService hardWareMessageService;
    private final IDataFlightsService flightsService;

    @Override
    public DataImageFilesInfoVo uploadImage(FileUploadDTO fileUploadDTO, DataImageFilesInfo dataImageFilesInfo, MultipartFile multipartFile) throws Exception {
        //是否小程序上传
        boolean wechatUpload = StrUtil.equals(fileUploadDTO.getCip(), Constants.WECHAT_SYMBOL);
        //是否切图
        boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_CUT));
        //是否单图旋转
        boolean isRotate = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_IMG_ROTATE));
        //是否OCR
        boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_OFF)) && !wechatUpload && Boolean.valueOf(fileUploadDTO.getIsOcr());
        //是否查验
        boolean checkOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CHECK_OFF));
        //是否自定义树节点
        String treeNode = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.TREE_NODE_CODE);

        if ("xml".equalsIgnoreCase(FileUtils.getFileSuffix(fileUploadDTO.getFileName()))) {
            dataImageFilesInfo = adaptationXml(fileUploadDTO, dataImageFilesInfo, multipartFile, wechatUpload, checkOff, treeNode);
            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
            ncImageServiceDTO.setFile(Base64.encode(multipartFile.getBytes()));
            ncImageServiceDTO.setUploadBusinessType(NcConstant.NORMAL_UPLOAD_INVOICE);
            dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
            dataImageFilesInfo.updateById();
            //返回
            return BeanCopyUtils.copy(dataImageFilesInfo, new DataImageFilesInfoVo());
        }
        final boolean isMatch = Boolean.parseBoolean(fileUploadDTO.getIsMatch());
        if(isMatch){//流水号包含_,说明是从二次匹配页面上传的发票
            dataImageFilesInfo.setFileStatus(FileStatusConstants.MATCH_FAILED);
            dataImageFilesInfo.setMessage("匹配失败");
        }
        /**
         * 根据流水号判断是否为二次匹配上传，如果为二次匹配上传
         * 二次匹配只走合合，票小米，税务云识别不查验
         * 如果是ncc识别或者BIP识别开启,使用二次匹配上传就需要开启合合，票小米，税务云3个中任意一个，同时不走ncc识别和bip识别
         */
        //压缩图片
        ByteArrayOutputStream byteArrayOutputStream = FileUtils.thumbnailImage(multipartFile, FilenameUtils.getExtension(fileUploadDTO.getFileName()));
        //缩略图
        ByteArrayOutputStream cutThumbnailImgFile = FileUtils.thumbnailSmall(byteArrayOutputStream.toByteArray());
        //压缩图片存储
        UploadResult originalImage = OssFactory.instance().upload(byteArrayOutputStream.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), "JPG");
        //缩略图存储
        UploadResult smallImage = OssFactory.instance().upload(cutThumbnailImgFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small_" + dataImageFilesInfo.getFileId()), "JPG");
        //设置图片存储信息
        dataImageFilesInfo.setIurl(originalImage.getUrl());
        dataImageFilesInfo.setUrl(originalImage.getUrl());
        dataImageFilesInfo.setSurl(smallImage.getUrl());

        DataImageTree bigImageTree = new DataImageTree();
        //初始化图片信息和树节点
        saveOrUpdateImageTree(dataImageFilesInfo, fileUploadDTO, wechatUpload, bigImageTree);
        // 判断NCC开关是否启用
        String status = NcProperties.getDefaultPropertiesStatus();
        // 与NC业务系统相关逻辑
        boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
        boolean multigraph = false;
        //业务参数判断
        if (isMatch || ocrOff || (wechatUpload && Boolean.parseBoolean(fileUploadDTO.getIsOcr()))) {
            //图片识别
            List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfo, byteArrayOutputStream.toByteArray());
            //是否多图
            multigraph = CollectionUtil.isNotEmpty(identificationData) && identificationData.size() > 1;
            //单图旋转
            if (!multigraph && isRotate) {
                //单图且旋转
                //切图
                byte[] bytes = FileUtils.imageCut(byteArrayOutputStream.toByteArray(), identificationData.get(0).t.getCoordinate(), identificationData.get(0).t.getBase64(), identificationData.get(0).t.getOption(), identificationData.get(0).t.getOrientation());
                //切割图片压缩
                ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
                //切割缩略图
                ByteArrayOutputStream cutThumbnailSmallFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                //切割压缩图片存储
                UploadResult cutOriginalImage = OssFactory.instance().upload(cutThumbnailFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), "JPG");
                //切割缩略图存储
                UploadResult cutSmallImage = OssFactory.instance().upload(cutThumbnailSmallFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small" + dataImageFilesInfo.getFileId()), "JPG");
                dataImageFilesInfo.setIurl(cutOriginalImage.getUrl());
                dataImageFilesInfo.setSurl(cutSmallImage.getUrl());
            }
            int i = 1;
            HashSet<String> typeSet = new HashSet<>();
            for (IdentificationData identificationDatum : identificationData) {
                typeSet.add(identificationDatum.k);
                if (multigraph && isCrop) {
                    //切图
                    DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
                    dataImageFilesInfoSm.setParentFileId(dataImageFilesInfo.getFileId());
                    dataImageFilesInfoSm.setFolderId(fileUploadDTO.getFolderId());
                    dataImageFilesInfoSm.setIsUse(fileUploadDTO.getIsUse());
                    dataImageFilesInfoSm.setCip(fileUploadDTO.getCip());
                    dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
                    if(!isMatch){//
                        dataImageFilesInfoSm.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
                        dataImageFilesInfoSm.setMessage("上传成功");
                    }else{
                        dataImageFilesInfoSm.setFileStatus(FileStatusConstants.MATCH_FAILED);
                        dataImageFilesInfoSm.setMessage("匹配失败");
                    }

                    dataImageFilesInfoSm.setBatchId(dataImageFilesInfo.getBatchId());

                    //切图
                    byte[] bytes = FileUtils.imageCut(byteArrayOutputStream.toByteArray(), identificationDatum.t.getCoordinate(), identificationDatum.t.getBase64(), identificationDatum.t.getOption(), identificationDatum.t.getOrientation());
                    //切割图片压缩
                    ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
                    //切割缩略图
                    ByteArrayOutputStream cutThumbnailSmallFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                    //切割压缩图片存储
                    UploadResult cutOriginalImage = OssFactory.instance().upload(cutThumbnailFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfoSm.getFileId()), "JPG");
                    //切割缩略图存储
                    UploadResult cutSmallImage = OssFactory.instance().upload(cutThumbnailSmallFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small_" + dataImageFilesInfoSm.getFileId()), "JPG");
                    dataImageFilesInfoSm.setFileName(i + "_" + dataImageFilesInfo.getFileName());
                    dataImageFilesInfoSm.setUserId(dataImageFilesInfo.getUserId());
                    dataImageFilesInfoSm.setIurl(cutOriginalImage.getUrl());
                    dataImageFilesInfoSm.setUrl(cutOriginalImage.getUrl());
                    dataImageFilesInfoSm.setSurl(cutSmallImage.getUrl());
                    dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
                    dataImageFilesInfoSm.setFileType(identificationDatum.k);
                    Field field = identificationDatum.t.getClass().getDeclaredField("ncImageId");
                    field.setAccessible(true);
                    String ncImageId = (String) field.get(identificationDatum.t);
                    dataImageFilesInfoSm.setNcImageId(ncImageId);
                    identificationDatum.t.setOcrFileId(dataImageFilesInfoSm.getFileId());
                    identificationDatum.t.setBillSaved(dataImageFilesInfo.getIsStaging());
                    dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationDatum.k, identificationDatum.t);

                    //查验方法
                    if(!isMatch){//二次匹配不走查验
                        checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfoSm, fileUploadDTO);
                    }
                    //图片预处理
                    breCheckInvoiceService.breCheckInvoice(dataImageFilesInfoSm, fileUploadDTO.getOrgCode());
                    //重扫、补扫修改影像文件状态
                    upImgStatus(dataImageFilesInfoSm, fileUploadDTO.getTaskStatus(), fileUploadDTO.getSupplementaryScan());
                    DataImageTree dataImageTreeSm = new DataImageTree();
                    //更新源文件,保存树节点
                    saveOrUpdateImageTree(dataImageFilesInfoSm, fileUploadDTO, true, dataImageTreeSm);
                    //原图是附件
                    dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
                    /**
                     * ncc识别开启,从二次匹配单据上传发票,不进入下面发票处理;
                     * ncc识别开启,从nc同步过来的单据上传发票,进入下面发票处理;
                     */
                    if (!StrUtil.equals(dataImageFilesInfoSm.getFileStatus(),FileStatusConstants.INVOICE_SUPPLER) && !StrUtil.equals(dataImageFilesInfoSm.getFileStatus(),FileStatusConstants.INVOICE_ALREADY_EXISTS) && StrUtil.equals(status, Constants.CONFIG_OFF_STATUS) && Boolean.parseBoolean(fileUploadDTO.getIsOcr()) && !wechatUpload) {
                        if(!isMatch) {
                            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
                            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfoSm);
                            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
                            ncImageServiceDTO.setFile(Base64.encode(multipartFile.getBytes()));
                            ncImageServiceDTO.setUploadBusinessType(NcConstant.NORMAL_UPLOAD_INVOICE);
                            dataImageFilesInfoSm = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
                        }
                    }
                    //是否自定义节点
                    if (StrUtil.isNotBlank(treeNode) && StrUtil.isNotBlank(fileUploadDTO.getBillType())) {
                        saveCustomize(treeNode, dataImageTreeSm, dataImageFilesInfoSm, fileUploadDTO.getBillType());
                    }
                    i++;
                } else if (!isCrop && multigraph) {
                    dataOcrService.ocrInsertBaseEntity(identificationDatum.k, identificationDatum.t);
                    if(!isMatch){//二次匹配不走查验
                        checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfo, fileUploadDTO);
                    }
                    //原图是多票据
                    dataImageFilesInfo.setFileType(InvoiceConstants.INVOICE_MUCH_NCC);
                } else {
                    if(!StrUtil.equals(identificationDatum.k,InvoiceConstants.IMAGE_OTHERS)) {
                        Field field = identificationDatum.t.getClass().getDeclaredField("ncImageId");
                        field.setAccessible(true);
                        String ncImageId = (String) field.get(identificationDatum.t);
                        dataImageFilesInfo.setNcImageId(ncImageId);
                    }
                    identificationDatum.t.setBillSaved(dataImageFilesInfo.getIsStaging());
                    dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationDatum.k, identificationDatum.t);
                    if(!isMatch) {//二次匹配不走查验
                        checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfo, fileUploadDTO);
                    }
                    //单图类型
                    dataImageFilesInfo.setFileType(identificationDatum.k);
                }
            }
            String typeArr = String.join(Constants.CONNECT_COMMA_SYMBOL, typeSet);
            dataImageFilesInfo.setIncludeTypeArr(typeArr);
        }
        /**
         * ncc识别开启,从二次匹配单据上传发票,不进入下面发票处理;
         * ncc识别开启,从nc同步过来的单据上传发票,进入下面发票处理;
         */
        if (ncEnabled && !StrUtil.equals(dataImageFilesInfo.getFileStatus(),FileStatusConstants.INVOICE_SUPPLER) && !StrUtil.equals(dataImageFilesInfo.getFileStatus(),FileStatusConstants.INVOICE_ALREADY_EXISTS) && StrUtil.equals(status, Constants.CONFIG_OFF_STATUS) && Boolean.parseBoolean(fileUploadDTO.getIsOcr()) && !wechatUpload && !multigraph) {
            if(!isMatch) {
                NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
                ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
                ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
                ncImageServiceDTO.setFile(Base64.encode(multipartFile.getBytes()));
                ncImageServiceDTO.setUploadBusinessType(NcConstant.NORMAL_UPLOAD_INVOICE);
                dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
            }
        }

        boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
        if (bipEnabled && Boolean.parseBoolean(fileUploadDTO.getIsOcr())) {
            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
            ncImageServiceDTO.setFile(Base64.encode(multipartFile.getBytes()));
            ncImageServiceDTO.setBarcode(fileUploadDTO.getBarCode());
            ncImageServiceDTO.setBillId(fileUploadDTO.getBusinessSerialNo());
            //ncImageServiceDTO.setYtenantId(dataImageFilesInfo.getTenantId());
            dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
        }

        //二次匹配上传文件为附件，直接保存为匹配成功
        if(isMatch&&StrUtil.equals(dataImageFilesInfo.getFileType(),InvoiceConstants.IMAGE_OTHERS)){
            dataImageFilesInfo.setFileStatus(FileStatusConstants.MATCH_SUCCESSFUL);
            dataImageFilesInfo.setMessage("匹配成功");
        }
        //重扫、补扫修改影像文件状态
        upImgStatus(dataImageFilesInfo, fileUploadDTO.getTaskStatus(), fileUploadDTO.getSupplementaryScan());
        //图片预处理

        breCheckInvoiceService.breCheckInvoice(dataImageFilesInfo, fileUploadDTO.getOrgCode());
        //更新源文件,保存树节点
        saveOrUpdateImageTree(dataImageFilesInfo, fileUploadDTO, wechatUpload, bigImageTree);

        //是否自定义树节点
        if (StrUtil.isNotBlank(treeNode) && StrUtil.isNotBlank(fileUploadDTO.getBillType())) {
            saveCustomize(treeNode, bigImageTree, dataImageFilesInfo, fileUploadDTO.getBillType());
        }

        //返回
        return BeanCopyUtils.copy(dataImageFilesInfo, new DataImageFilesInfoVo());
    }

    private DataImageFilesInfo upImgStatus(DataImageFilesInfo dataImageFilesInfo, String taskStatus, String supp) {
        if (TaskStateConstants.TASK_STATE_BH_CS.equalsIgnoreCase(taskStatus)) {
            //重扫
            dataImageFilesInfo.setFileFlowStatus("4");
        }
        if (TaskStateConstants.TASK_STATE_BH_BS.equalsIgnoreCase(taskStatus)) {
            dataImageFilesInfo.setFileFlowStatus("1");
        }
        if ("Y".equalsIgnoreCase(supp)) {
            dataImageFilesInfo.setFileFlowStatus("3");
        }
        return dataImageFilesInfo;
    }
    /**
     * 自定义树节点生成
     *
     * @param code               树节点编号
     * @param dataImageTree      原树节点信息
     * @param dataImageFilesInfo 图片信息
     * @param dataCurrentTask    单据类型
     * @return 树节点信息
     */
    private DataImageTree saveCustomize(String code, DataImageTree dataImageTree, DataImageFilesInfo dataImageFilesInfo, String dataCurrentTask) {
        List<DataImageTree> dataImageTrees = imageTreeService.selectList(new LambdaQueryWrapper<DataImageTree>()
            .eq(DataImageTree::getProductType, code)
            .isNotNull(DataImageTree::getBatchId)
            .isNotNull(DataImageTree::getMatchingFileRules));
        for (DataImageTree imageTree : dataImageTrees) {
            if (ArrayUtil.containsIgnoreCase(imageTree.getMatchingBillRules().split(","), dataCurrentTask) && ArrayUtil.containsIgnoreCase(imageTree.getMatchingFileRules().split(","), dataImageFilesInfo.getFileType())) {
                dataImageTree.setProductId(dataImageFilesInfo.getFileId());
                dataImageTree.setParentId(imageTree.getProductId());
                dataImageTree.setProductName(dataImageFilesInfo.getFileName());
                dataImageTree.setProductType(imageTree.getProductType());
                dataImageTree.setImageId(dataImageFilesInfo.getFileId());
                dataImageTree.setBatchId(dataImageFilesInfo.getBatchId());
                imageTreeService.saveOrUpdate(dataImageTree);
            }
        }
        return dataImageTree;
    }
    /**
     * @param checkOff            是否查验
     * @param wechatUpload        是否微信上传
     * @param identificationDatum 识别
     * @param dataImageFilesInfo  图片
     * @param fileUploadDTO       上传信息
     * @throws Exception 异常
     */
    private void checkInvoice(boolean checkOff, boolean wechatUpload, IdentificationData identificationDatum, DataImageFilesInfo dataImageFilesInfo, FileUploadDTO fileUploadDTO) throws Exception {
        // 判断小程序入口上传的发票 如果没有开启小程序查验开关，不允许走查验逻辑
        if (wechatUpload && !Boolean.parseBoolean(fileUploadDTO.getIsCheck())) {
            return;
        }
        // 判断PC端入口上传的发票 如果没有开启PC端查验开关，不允许走查验逻辑
        if (!wechatUpload && !checkOff) {
            return;
        }
        if (identificationDatum.k.equalsIgnoreCase(InvoiceConstants.TAX_SPECIAL_INVOICE)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.TAX_INVOICE)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ROLL_TICKET)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_OFD_INVOICE)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.MOTOR_VEHICLE_SALE)
            || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.USED_CAR_SALES)
        ) {
            identificationDatum.t.setOcrFileId(dataImageFilesInfo.getFileId());
            //图片查验
            BaseEntity baseEntity = null;
            try {
                baseEntity = CheckFactory.instance().checkInvoke(dataImageFilesInfo, GetRequestUtils.getRequest(identificationDatum.t));
            } catch (Exception e) {
                log.error("查验出现异常："+ ExceptionUtil.getExceptionMessage(e));
                //查验失败错误原因填写
                dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
                dataImageFilesInfo.setMessage(e.getLocalizedMessage());
                if(ObjectUtil.isNotEmpty(baseEntity)){
                    baseEntity.setCheckResult(e.getLocalizedMessage());
                    // 更新OCR
                    this.updateBaseEntityByCheck(identificationDatum, baseEntity);
                }else {
                    throw new Exception(e.getLocalizedMessage());
                }
            }
            if (baseEntity != null && StrUtil.equals(baseEntity.getCheckInvoice(), CheckConstant.SUCCESS_CHECK)) {
                // 更新OCR
                this.updateBaseEntityByCheck(identificationDatum, baseEntity);
            } else {
                if(baseEntity != null){
                    //查验失败错误原因填写
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.INVOICE_CHECK_FAIL);
                    dataImageFilesInfo.setMessage(baseEntity.getCheckResult());
                    // 更新OCR
                    this.updateBaseEntityByCheck(identificationDatum, baseEntity);
                }
            }
        }
    }

    /**
     * 根据查验结果更新OCR表数据
     *
     * @param identificationData
     * @param baseResult
     */
    private void updateBaseEntityByCheck(IdentificationData identificationData, BaseEntity baseResult) throws ClassNotFoundException {
        if (identificationData.t instanceof DataMotorVehicleSale) {
            DataMotorVehicleSale dataMotorVehicleSale = (DataMotorVehicleSale) identificationData.t;
            if (baseResult instanceof DataMotorVehicleSale) {
                String id = dataMotorVehicleSale.getId();
                DataMotorVehicleSale motorVehicleSaleResult = (DataMotorVehicleSale) baseResult;
                BeanUtil.copyProperties(motorVehicleSaleResult, dataMotorVehicleSale);
                dataMotorVehicleSale.setId(id);
            } else {
                dataMotorVehicleSale.setCheckResult(baseResult.getCheckResult());
            }
            dataMotorVehicleSale.setFileId(identificationData.t.getOcrFileId());
            dataOcrService.ocrUpdateByBaseEntity(identificationData.k, dataMotorVehicleSale);
        } else if (identificationData.t instanceof DataUsedCarSales) {
            DataUsedCarSales dataUsedCarSales = (DataUsedCarSales) identificationData.t;
            if (baseResult instanceof DataUsedCarSales) {
                String id = dataUsedCarSales.getId();
                DataUsedCarSales usedCarSalesResult = (DataUsedCarSales) baseResult;
                BeanUtil.copyProperties(usedCarSalesResult, dataUsedCarSales);
                dataUsedCarSales.setId(id);
            } else {
                dataUsedCarSales.setCheckResult(baseResult.getCheckResult());
            }
            dataUsedCarSales.setFileId(identificationData.t.getOcrFileId());
            dataOcrService.ocrUpdateByBaseEntity(identificationData.k, dataUsedCarSales);
        } else {
            DataOcrInfo dataOcrInfo = (DataOcrInfo) identificationData.t;
            if (baseResult instanceof DataOcrInfo) {
                // 删除识别出来的ocr明细
                List<DataOcrDetails> details = dataOcrInfo.getDetails();
                if(CollectionUtil.isNotEmpty(details)){
                    for (DataOcrDetails detail : details) {
                        detail.deleteById();
                    }
                }
                String id = dataOcrInfo.getId();
                DataOcrInfo ocrInfoResult = (DataOcrInfo) baseResult;
                BeanUtil.copyProperties(ocrInfoResult, dataOcrInfo);
                dataOcrInfo.setId(id);
            } else {
                dataOcrInfo.setCheckResult(baseResult.getCheckResult());
            }
            dataOcrInfo.setFileId(identificationData.t.getOcrFileId());
            dataOcrService.ocrUpdateByBaseEntity(identificationData.k, dataOcrInfo);
        }
    }
    /**
     * 生成树节点
     */
    private DataImageTree saveOrUpdateImageTree(DataImageFilesInfo dataImageFilesInfo, FileUploadDTO fileUploadDTO, boolean wechatUpload, DataImageTree dataImageTree) {
        if (StrUtil.equals("1", dataImageFilesInfo.getWechatInvoiceFlag())) {
            dataImageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_INVOICE);
        }
        imageFilesInfoMapper.insertOrUpdate(dataImageFilesInfo);
        //批扫信息存储
        if (!wechatUpload && StrUtil.equals(fileUploadDTO.getScanType(), ScanTypeConstants.BATCH_SCAN)) {
            dataImageTree.setBatchBusinessQuote(LoginHelper.getLoginUser().getLoginId());
        }
        dataImageTree.setProductId(dataImageFilesInfo.getFileId());
        dataImageTree.setParentId(dataImageFilesInfo.getFileType());
        //附件类型加判断
        if (!ArrayUtil.containsIgnoreCase(InvoiceConstants.INVOICE_ClASS_TYPE.keySet().toArray(new String[0]), dataImageFilesInfo.getFileType())) {
            dataImageTree.setParentId(InvoiceConstants.IMAGE_OTHERS);
        }
        // 多票据文件类型重新赋值
        if (StrUtil.equals(InvoiceConstants.INVOICE_MUCH_NCC, dataImageFilesInfo.getFileType())) {
            dataImageTree.setParentId(InvoiceConstants.INVOICE_MUCH_NCC);
        }
        dataImageTree.setProductName(dataImageFilesInfo.getFileName());
        dataImageTree.setProductLevel(2L);
        dataImageTree.setProductType("0");
        dataImageTree.setStatus("0");
        dataImageTree.setOrderNum(0L);
        dataImageTree.setImageId(dataImageFilesInfo.getFileId());
        dataImageTree.setBatchId(dataImageFilesInfo.getBatchId());
        if ("Y".equalsIgnoreCase(fileUploadDTO.getSupplementaryScan())) {
            dataImageTree.setParentId(InvoiceConstants.AFTER_FILE);
            dataImageTree.setIsMove("1");
        }
        if(StrUtil.isNotBlank(fileUploadDTO.getProductId())){
            dataImageTree.setParentId(fileUploadDTO.getProductId());
        }
        imageTreeService.saveOrUpdate(dataImageTree);
        return dataImageTree;
    }
    /**
     * 适配xml格式电票
     */
    private DataImageFilesInfo adaptationXml(FileUploadDTO fileUploadDTO, DataImageFilesInfo dataImageFilesInfo, MultipartFile multipartFile,
                                             boolean wechatUpload, boolean checkOff, String treeNode) throws Exception {
        if(StrUtil.equalsAnyIgnoreCase(FileUtils.getFileSuffix(fileUploadDTO.getFileName()),"zip")){
            // 解压出zip包中的xml文件
            File file = new File(multipartFile.getOriginalFilename());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(multipartFile.getBytes());
            fileOutputStream.close();
            Extractor extractor = CompressUtil.createExtractor(CharsetUtil.defaultCharset(), FileUtil.file(file));
            File zipFile = FileUtil.file(BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()));
            extractor.extract(zipFile);
            if(zipFile.exists()){
                File[] files = zipFile.listFiles();
                File xmlFile = null;
                for (File o : files) {
                    if (o.getName().endsWith(".xml") || o.getName().endsWith(".XML")) {
                        xmlFile = o;
                    }
                }
                if(ObjectUtil.isNotEmpty(xmlFile)){
                    dataImageFilesInfo.setFileName(xmlFile.getName());
                    dataImageFilesInfo.setFileSize(String.valueOf(xmlFile.length()));
                    multipartFile = new MockMultipartFile("file", xmlFile.getName(), null, new FileInputStream(xmlFile));
                    dataImageFilesInfo.setTempFile(multipartFile);
                }else{
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                    dataImageFilesInfo.setMessage("xml文件不存在");
                    return dataImageFilesInfo;
                }
            }else{
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                dataImageFilesInfo.setMessage("解压文件路径不存在");
                return dataImageFilesInfo;
            }
        }
        XmlInvoiceDTO xmlInvoiceDTO = xmlEInvoice(multipartFile, fileUploadDTO, dataImageFilesInfo);
        //存源文件
        UploadResult uploadDocument = OssFactory.instance().upload(multipartFile.getBytes(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()) + "_xml", "XML");
        multipartFile = xmlInvoiceDTO.getFile();
        BeanCopyUtils.copy(xmlInvoiceDTO.getDataImageFilesInfo(), dataImageFilesInfo);
        //压缩图片
        ByteArrayOutputStream byteArrayOutputStream = FileUtils.thumbnailImage(multipartFile, FilenameUtils.getExtension(fileUploadDTO.getFileName()));
        //缩略图
        ByteArrayOutputStream cutThumbnailImgFile = FileUtils.thumbnailSmall(byteArrayOutputStream.toByteArray());
        //压缩图片存储
        UploadResult originalImage = OssFactory.instance().upload(byteArrayOutputStream.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), "JPG");
        //缩略图存储
        UploadResult smallImage = OssFactory.instance().upload(cutThumbnailImgFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small_" + dataImageFilesInfo.getFileId()), "JPG");

        //设置图片存储信息
        dataImageFilesInfo.setIurl(originalImage.getUrl());
        dataImageFilesInfo.setUrl(uploadDocument.getUrl());
        dataImageFilesInfo.setSurl(smallImage.getUrl());
        String fileType = dataImageFilesInfo.getFileType();
        DataImageTree bigImageTree = new DataImageTree();
        //初始化图片信息和树节点
        saveOrUpdateImageTree(dataImageFilesInfo, fileUploadDTO, wechatUpload, bigImageTree);//重扫、补扫修改影像文件状态
        upImgStatus(dataImageFilesInfo, fileUploadDTO.getTaskStatus(), fileUploadDTO.getSupplementaryScan());
        IdentificationData identificationData = new IdentificationData(dataImageFilesInfo.getFileType(), xmlInvoiceDTO.getDataOcrInfo());
        //查验方法
        checkInvoice(checkOff, wechatUpload, identificationData, dataImageFilesInfo, fileUploadDTO);
        dataImageFilesInfo.setFileType(fileType);
        //图片预处理
        breCheckInvoiceService.breCheckInvoice(dataImageFilesInfo, fileUploadDTO.getOrgCode());
        //更新源文件,保存树节点
        saveOrUpdateImageTree(dataImageFilesInfo, fileUploadDTO, wechatUpload, bigImageTree);
        //是否自定义树节点
        if (StrUtil.isNotBlank(treeNode) && StrUtil.isNotBlank(fileUploadDTO.getBillType())) {
            saveCustomize(treeNode, bigImageTree, dataImageFilesInfo, fileUploadDTO.getBillType());
        }
        return dataImageFilesInfo;
    }

    /**
     * xml格式文件校验
     *
     * @param file
     */
    @Override
    public XmlInvoiceDTO xmlEInvoice(MultipartFile file, FileUploadDTO dto, DataImageFilesInfo imageFilesInfo) throws Exception {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        /*读取xml文件中的内容*/
        String xmlString = new String(file.getBytes());
        org.json.JSONObject jsonObject = XML.toJSONObject(xmlString);
        if (jsonObject.has("EInvoice")) {
            JSONObject json = JSONObject.parseObject(jsonObject.get("EInvoice").toString());
            //发票票种
            JSONObject EInvoiceType = JSONObject.parseObject(JSONObject.parseObject(JSONObject.parseObject(json.getString("Header")).getString("InherentLabel")).getString("EInvoiceType"));
            //票种类
            JSONObject generalOrSpecialVAT = JSONObject.parseObject(JSONObject.parseObject(JSONObject.parseObject(json.getString("Header")).getString("InherentLabel")).getString("GeneralOrSpecialVAT"));
            //税务监制信息
            JSONObject taxSupervisionInfo = JSONObject.parseObject(json.getString("TaxSupervisionInfo"));
            //销售方信息
            JSONObject SellerInformation = JSONObject.parseObject(JSONObject.parseObject(json.getString("EInvoiceData")).getString("SellerInformation"));
            //购买方信息
            JSONObject BuyerInformation = JSONObject.parseObject(JSONObject.parseObject(json.getString("EInvoiceData")).getString("BuyerInformation"));
            //票面基本信息
            JSONObject BasicInformation = JSONObject.parseObject(JSONObject.parseObject(json.getString("EInvoiceData")).getString("BasicInformation"));
            //票面明细信息
            JSONObject IssuItemInformation = JSONObject.parseObject(JSONObject.parseObject(json.getString("EInvoiceData")).getString("IssuItemInformation"));

            String labelCode = generalOrSpecialVAT.get("LabelCode").toString();
            String r = "";
            if (XmlInvoiceConstants.XML_TAX.equals(EInvoiceType.getString("LabelCode"))) {
                r = "Y";
            } else {
                r = "N";
            }
            if (XmlInvoiceConstants.XML_TAX_SPECIAL_INVOICE.equals(labelCode) && EInvoiceType.getString("LabelCode").equals(XmlInvoiceConstants.XML_TAX)) {
                imageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_OFD_INVOICE);
            } else if (XmlInvoiceConstants.XML_TAX_INVOICE.equals(labelCode) && EInvoiceType.getString("LabelCode").equals(XmlInvoiceConstants.XML_TAX)) {
                imageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_INVOICE);
            }

            String strs[] = new String[9];
            strs[0] = "发票类型：" + generalOrSpecialVAT.getString("LabelName");
            strs[1] = "是否是电子发票：" + r;
            strs[2] = "发票号码：" + taxSupervisionInfo.getString("InvoiceNumber");
            strs[3] = "购买方：" + BuyerInformation.getString("BuyerName");
            strs[4] = "购买方纳税人识别号：" + BuyerInformation.getString("BuyerIdNum");
            strs[5] = "金额：¥" + BasicInformation.getString("TotalAmWithoutTax");
            strs[6] = "开票日期：" + taxSupervisionInfo.getString("IssueTime");
            strs[7] = "销售方：" + SellerInformation.getString("SellerName");
            strs[8] = "销售方纳税人识别号：" + SellerInformation.getString("SellerIdNum");

            // 获取BufferedImage对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(createImage(strs), "jpg", baos);
            //转换为MultipartFile
            file = new MockMultipartFile(dto.getFileName().split("\\.")[0] + ".jpg", baos.toByteArray());
            imageFilesInfo.setFileSize(String.valueOf(file.getSize()));
            DataOcrInfo dataOcrInfo = new DataOcrInfo();
            if (Boolean.parseBoolean(dto.getIsOcr())) {
                String time = taxSupervisionInfo.get("IssueTime").toString().split(" ")[0];
                //保存ocr信息
                dataOcrInfo.setId(IdUtil.simpleUUID());
                dataOcrInfo.setFileId(imageFilesInfo.getFileId());
                dataOcrInfo.setInvoiceNumber(taxSupervisionInfo.getString("InvoiceNumber"));
                dataOcrInfo.setBuyerName(BuyerInformation.getString("BuyerName"));
                dataOcrInfo.setBuyerNo(BuyerInformation.getString("BuyerIdNum"));
                dataOcrInfo.setSumAmount(NumberUtils.createBigDecimal(BasicInformation.getString("TotalAmWithoutTax")));
                dataOcrInfo.setInvoiceDate(ft.parse(time));
                dataOcrInfo.setSellerName(SellerInformation.getString("SellerName"));
                dataOcrInfo.setSellerNo(SellerInformation.getString("SellerIdNum"));
                dataOcrInfo.setTotalLowercase(NumberUtils.createBigDecimal(IssuItemInformation.getString("TotaltaxIncludedAmount")));

                List<DataOcrDetails> dataOcrDetailsList = new ArrayList<>();
                DataOcrDetails ocrDetails = new DataOcrDetails();
                ocrDetails.setId(IdUtil.simpleUUID());
                ocrDetails.setFileId(imageFilesInfo.getFileId());
                dataOcrDetailsList.add(ocrDetails);
                dataOcrInfo.setDetails(dataOcrDetailsList);

                dataOcrInfoService.insert(dataOcrInfo);
            } else {
                imageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
            }
            XmlInvoiceDTO xmlInvoiceDTO = new XmlInvoiceDTO();
            xmlInvoiceDTO.setFile(file);
            xmlInvoiceDTO.setDataImageFilesInfo(imageFilesInfo);
            xmlInvoiceDTO.setDataOcrInfo(dataOcrInfo);
            return xmlInvoiceDTO;
        } else if (jsonObject.has("xbrl")) {
            JSONObject xbrl = JSONObject.parseObject(jsonObject.get("xbrl").toString());
            String lableName = JSONObject.parseObject(xbrl.get("inv:TypeOfInvoice").toString()).get("content").toString();
            String invoiceCode = JSONObject.parseObject(xbrl.get("inv:CodeOfInvoice").toString()).get("content").toString();
            String invoiceNumber = JSONObject.parseObject(xbrl.get("inv:NumberOfInvoice").toString()).get("content").toString();
            String buyer = JSONObject.parseObject(xbrl.get("inv:NameOfPurchaser").toString()).get("content").toString();
            String buyerNum = JSONObject.parseObject(xbrl.get("inv:TaxpayerIdentificationNumberUnifiedSocialCreditCodeOfPurchaser").toString()).get("content").toString();
            String pretaxAmount = JSONObject.parseObject(xbrl.get("inv:TotalAmountExcludingTax").toString()).get("content").toString();
            String invoiceDate = JSONObject.parseObject(xbrl.get("inv:DateOfIssue").toString()).get("content").toString();
            String seller = JSONObject.parseObject(xbrl.get("inv:NameOfSeller").toString()).get("content").toString();
            String sellerNum = JSONObject.parseObject(JSONObject.parseObject(JSONObject.parseObject(xbrl.get("context").toString()).get("entity").toString()).get("identifier").toString()).get("content").toString();
            String totalLowercase = JSONObject.parseObject(xbrl.get("inv:TaxIncludedAmountInFigures").toString()).get("content").toString();
            String checkCode=JSONObject.parseObject(xbrl.get("inv:IdentifyingCode").toString()).get("content").toString();

            String strs[] = new String[8];
            strs[0] = "发票类型：" + lableName;
            strs[1] = "发票号码：" + invoiceNumber;
            strs[2] = "购买方：" + buyer;
            strs[3] = "购买方纳税人识别号：" + buyerNum;
            strs[4] = "金额：¥" + pretaxAmount;
            strs[5] = "开票日期：" + invoiceDate;
            strs[6] = "销售方：" + seller;
            strs[7] = "销售方纳税人识别号：" + sellerNum;

            // 获取BufferedImage对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(createImage(strs), "jpg", baos);
            //转换为MultipartFile
            file = new MockMultipartFile(dto.getFileName().split("\\.")[0] + ".jpg", baos.toByteArray());
            imageFilesInfo.setFileSize(String.valueOf(file.getSize()));
            if ("增值税电子普通发票".equalsIgnoreCase(lableName)) {
                imageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_INVOICE);
            } else if ("增值税电子专用发票".equalsIgnoreCase(lableName)) {
                imageFilesInfo.setFileType(InvoiceConstants.ELECTRONIC_OFD_INVOICE);
            }
            DataOcrInfo dataOcrInfo = new DataOcrInfo();
            if (Boolean.parseBoolean(dto.getIsOcr())) {
                //保存ocr信息
                dataOcrInfo.setId(IdUtil.simpleUUID());
                dataOcrInfo.setFileId(imageFilesInfo.getFileId());
                dataOcrInfo.setInvoiceNumber(invoiceNumber);
                dataOcrInfo.setBuyerName(buyer);
                dataOcrInfo.setBuyerNo(buyerNum);
                dataOcrInfo.setSumAmount(NumberUtils.createBigDecimal(pretaxAmount));
                dataOcrInfo.setInvoiceDate(ft.parse(invoiceDate));
                dataOcrInfo.setSellerName(seller);
                dataOcrInfo.setSellerNo(sellerNum);
                dataOcrInfo.setCheckCode(checkCode);
                dataOcrInfo.setInvoiceCode(invoiceCode);
                dataOcrInfo.setTotalLowercase(NumberUtils.createBigDecimal(totalLowercase));
                List<DataOcrDetails> dataOcrDetailsList = new ArrayList<>();
                DataOcrDetails ocrDetails = new DataOcrDetails();
                ocrDetails.setId(IdUtil.simpleUUID());
                ocrDetails.setFileId(imageFilesInfo.getFileId());
                dataOcrDetailsList.add(ocrDetails);
                dataOcrInfo.setDetails(dataOcrDetailsList);

                dataOcrInfoService.insert(dataOcrInfo);
            } else {
                imageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
            }
            XmlInvoiceDTO xmlInvoiceDTO = new XmlInvoiceDTO();
            xmlInvoiceDTO.setFile(file);
            xmlInvoiceDTO.setDataImageFilesInfo(imageFilesInfo);
            xmlInvoiceDTO.setDataOcrInfo(dataOcrInfo);
            return xmlInvoiceDTO;
        } else {
            throw new Exception("此xml格式文件非电票，请重新上传");
        }
    }

    public static BufferedImage createImage(String[] strs) {
        // 设置背景宽高
        int width = 1200, height = 1000;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文对象
        Graphics graphics = image.getGraphics();
        // 填充
        graphics.fillRect(0, 0, width, height);
        // 设定字体大小及样式
        graphics.setFont(new Font("宋体", Font.BOLD, 30));
        // 字体颜色
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < strs.length; i++) {
            // 描绘字符串
            graphics.drawString(strs[i], 150, 70 + (i + 1) * 50);
        }
        graphics.dispose();
        return image;
    }
    @Override
    public DataImageFilesInfoVo saveDocuments(FileUploadDTO fileUploadDTO, DataImageFilesInfo dataImageFilesInfo, MultipartFile file) throws Exception {
        //是否小程序上传
        boolean wechatUpload = StrUtil.equals(fileUploadDTO.getCip(), Constants.WECHAT_SYMBOL);
        //识别开关
        boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_OFF));
        //查验开关
        boolean checkOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CHECK_OFF));
        //是否校验源文件
        boolean sourceBol = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_INVOICE_SOURCE));
        //是否直接预览附件
        boolean imgBol = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_DOCUMENT));
        //是否外部预览附件
        boolean viewBol = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_FILE_VIEW));

        //是否自定义树节点
        String treeNode = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.TREE_NODE_CODE);

        if ("zip".equalsIgnoreCase(FileUtils.getFileSuffix(fileUploadDTO.getFileName()))) {
            dataImageFilesInfo = adaptationXml(fileUploadDTO, dataImageFilesInfo, file, wechatUpload, checkOff, treeNode);
            if(!StrUtil.equals(dataImageFilesInfo.getFileStatus(),FileStatusConstants.IMAGE_SAVE_FAILED)){
                NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
                ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
                ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
                ncImageServiceDTO.setFile(Base64.encode(dataImageFilesInfo.getTempFile().getBytes()));
                ncImageServiceDTO.setUploadBusinessType(NcConstant.NORMAL_UPLOAD_INVOICE);
                dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
                dataImageFilesInfo.updateById();
            }
            //返回
            return BeanCopyUtils.copy(dataImageFilesInfo, new DataImageFilesInfoVo());
        }
        //获取文件后缀
        String extension = FileUtils.getExtension(file);
        //存文件/树
        DocumentFactory.instance(extension).setFileType(dataImageFilesInfo);
        //存源文件
        UploadResult uploadDocument = OssFactory.instance().upload(file.getBytes(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), extension);
        dataImageFilesInfo.setIurl(uploadDocument.getUrl());
        dataImageFilesInfo.setUrl(uploadDocument.getUrl());
        byte[] bytes = null;
        if (viewBol){
            UploadResult uploadPdf = OssFactory.instance().upload(file.getBytes(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()+"."+extension), extension);
            dataImageFilesInfo.setPurl(uploadPdf.getUrl());
        }else{
            //附件转换成pdf
            bytes = DocumentFactory.instance(extension).documentToPdf(file.getBytes());
            //存预览文件
            UploadResult uploadPdf = OssFactory.instance().upload(bytes, BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "dpdf_" + dataImageFilesInfo.getFileId()), extension);
            dataImageFilesInfo.setPurl(uploadPdf.getUrl());
        }


        //存缩略图
        if (imgBol) {
            if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION, extension)) {
                UploadResult uploadImg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 50), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), extension);
                dataImageFilesInfo.setSurl(uploadImg.getUrl());
                UploadResult uploadLimg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImg(file.getBytes(), 80), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), extension);
                dataImageFilesInfo.setIurl(uploadLimg.getUrl());
            } else {
                UploadResult uploadImg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 100), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), extension);
                dataImageFilesInfo.setSurl(uploadImg.getUrl());
                UploadResult uploadLimg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImg(file.getBytes(), 100), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), extension);
                dataImageFilesInfo.setIurl(uploadLimg.getUrl());
            }
        }
        DataImageTree bigImageTree = new DataImageTree();
        //初始化图片信息和树节点
        saveOrUpdateImageTree(dataImageFilesInfo, fileUploadDTO, true, bigImageTree);

        //有发票信息的附件
        String[] invoice = ArrayUtils.addAll(MimeTypeUtils.DOCUMENT_PDF_TRANSFOTMATION, MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION);
        //处理发票信息
        if (ArrayUtil.containsIgnoreCase(invoice, extension) && Boolean.parseBoolean(fileUploadDTO.getIsOcr()) && ocrOff) {
            //判断是否是电子发票
            List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfo, file.getBytes());
            IdentificationData identificationDatum = identificationData.get(0);
            identificationDatum.t.setOcrFileId(dataImageFilesInfo.getFileId());
            identificationDatum.t.setBillSaved(dataImageFilesInfo.getIsStaging());
            dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationDatum.k, identificationDatum.t);
            dataImageFilesInfo.setFileType(identificationDatum.k);
            if (identificationDatum.k.equalsIgnoreCase(InvoiceConstants.TAX_SPECIAL_INVOICE)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.TAX_INVOICE)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ROLL_TICKET)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_OFD_INVOICE)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE_ITINERARY)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.MOTOR_VEHICLE_SALE)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.USED_CAR_SALES)
                || identificationDatum.k.equalsIgnoreCase(InvoiceConstants.ELECTRONIC_INVOICE_QUKUAILIAN)) {
                //允不允许上传源文件
                if (sourceBol && !PDFUtil.verifyEInvoice(file.getBytes())) {
                    //非源文件 结束上传
                    dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                    dataImageFilesInfo.setMessage("非电子发票源文件，请上传源文件");
                }
                //如果没有开启附件 但是电子发票仍需要图片预览
                if (!imgBol) {
                    UploadResult uploadImg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 100), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), extension);
                    dataImageFilesInfo.setSurl(uploadImg.getUrl());
                    UploadResult uploadLimg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImg(file.getBytes(), 100), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), extension);
                    dataImageFilesInfo.setIurl(uploadLimg.getUrl());
                }
                //发票查验与否
                checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfo, fileUploadDTO);
            } else if (identificationDatum.k.equalsIgnoreCase(InvoiceConstants.DIDI_ITINERARY)) {
            } else {
                DocumentFactory.instance(extension).setFileType(dataImageFilesInfo);
            }
        }
        // 判断NCC开关是否启用
        String status = NcProperties.getDefaultPropertiesStatus();
        // 与NC业务系统相关逻辑
        boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
        if (ncEnabled && !StrUtil.equals(dataImageFilesInfo.getFileStatus(),FileStatusConstants.INVOICE_SUPPLER) && !StrUtil.equals(dataImageFilesInfo.getFileStatus(),FileStatusConstants.INVOICE_ALREADY_EXISTS) && StrUtil.equals(status, Constants.CONFIG_OFF_STATUS) && Boolean.parseBoolean(fileUploadDTO.getIsOcr())) {
            // 判断是否是电子发票源文件
            if (sourceBol && ArrayUtil.containsIgnoreCase(invoice, extension) && !PDFUtil.verifyEInvoice(file.getBytes())) {
                //非源文件 结束上传
                dataImageFilesInfo.setFileStatus(FileStatusConstants.IMAGE_SAVE_FAILED);
                dataImageFilesInfo.setMessage("非电子发票源文件，请上传源文件");
            }
            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
            ncImageServiceDTO.setFile(Base64.encode(file.getBytes()));
            ncImageServiceDTO.setUploadBusinessType(NcConstant.NORMAL_UPLOAD_INVOICE);
            dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
            if (StrUtil.equals(dataImageFilesInfo.getFileType(), InvoiceConstants.ELECTRONIC_OFD_INVOICE) || StrUtil.equals(dataImageFilesInfo.getFileType(), InvoiceConstants.ELECTRONIC_INVOICE)) {
                if (StrUtil.equals(dataImageFilesInfo.getFileStatus(), FileStatusConstants.INVOICE_CHECK_SUCCESS)) {
                    bigImageTree.setParentId(dataImageFilesInfo.getFileType());
                }
                //如果没有开启附件 但是电子发票仍需要图片预览
                if (!imgBol) {
                    if (ArrayUtil.containsIgnoreCase(MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION, extension)) {
                        UploadResult uploadImg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 50), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), extension);
                        dataImageFilesInfo.setSurl(uploadImg.getUrl());
                        UploadResult uploadLimg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImg(file.getBytes(), 80), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), extension);
                        dataImageFilesInfo.setIurl(uploadLimg.getUrl());
                    }  else {
                        UploadResult uploadImg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 50), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), extension);
                        dataImageFilesInfo.setSurl(uploadImg.getUrl());
                        UploadResult uploadLimg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImg(file.getBytes(), 80), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), extension);
                        dataImageFilesInfo.setIurl(uploadLimg.getUrl());
                    }
                }
            }
        }
        boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
        if (bipEnabled && Boolean.parseBoolean(fileUploadDTO.getIsOcr()) && ArrayUtil.containsIgnoreCase(invoice, extension)) {
            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
            ncImageServiceDTO.setFile(Base64.encode(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 120)));
            ncImageServiceDTO.setPByte(bytes);
            ncImageServiceDTO.setBytes(file.getBytes());
            ncImageServiceDTO.setBarcode(fileUploadDTO.getBarCode());
            ncImageServiceDTO.setBillId(fileUploadDTO.getBusinessSerialNo());
            //ncImageServiceDTO.setYtenantId(dataImageFilesInfo.getTenantId());
            dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
        }

        if (InvoiceConstants.DOCUMENT_OFD.equals(dataImageFilesInfo.getFileType())
            && InvoiceConstants.IMAGE_OTHERS.equals(bigImageTree.getParentId())
            && "true".equals(fileUploadDTO.getIsOcr())) {
            // dataImageFilesInfo = saveOfdOcr(dataImageFilesInfo, file);
            UploadResult uploadImg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImgOne(file.getBytes(), 50), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "simg_" + dataImageFilesInfo.getFileId()), extension);
            dataImageFilesInfo.setSurl(uploadImg.getUrl());
            UploadResult uploadLimg = OssFactory.instance().upload(DocumentFactory.instance(extension).documentToImg(file.getBytes(), 80), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "img_" + dataImageFilesInfo.getFileId()), extension);
            dataImageFilesInfo.setIurl(uploadLimg.getUrl());

        }

        //重扫、补扫修改影像文件状态
        upImgStatus(dataImageFilesInfo, fileUploadDTO.getTaskStatus(), fileUploadDTO.getSupplementaryScan());
        //图片预处理
        breCheckInvoiceService.breCheckInvoice(dataImageFilesInfo, fileUploadDTO.getOrgCode());

//        if (!InvoiceConstants.INVOICE_ClASS_TYPE.containsKey(dataImageFilesInfo.getFileType())) {
//            dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
//        }
        //更新源文件,更新树节点
        saveOrUpdateImageTree(dataImageFilesInfo, fileUploadDTO, true, bigImageTree);
        //是否自定义树节点
        if (StrUtil.isNotBlank(treeNode) && StrUtil.isNotBlank(fileUploadDTO.getBillType())) {
            saveCustomize(treeNode, bigImageTree, dataImageFilesInfo, fileUploadDTO.getBillType());
        }
        //处理附件
        return BeanCopyUtils.copy(dataImageFilesInfo, DataImageFilesInfoVo.class);
    }

    /**
     * 推送附件逻辑方法
     * @param dataImageFilesInfo 图片对象
     * @param businessSerialNo 单据流水号
     */
    @Override
    public void pushFileDataToBusinessSystem(DataImageFilesInfo dataImageFilesInfo, String businessSerialNo) {
        // 是否开启推送附件至业务系统
        boolean isPushFile = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_PUSH_FILE_BUSINESS_SYSTEM));
        if(isPushFile){
            // 正常状态的影像文件才可进入推送附件流程
            if(StrUtil.equals(dataImageFilesInfo.getFileStatus(), FileStatusConstants.INVOICE_CHECK_SUCCESS) || !StrUtil.equals(dataImageFilesInfo.getFileStatus(), FileStatusConstants.SAVED_SUCCESSFULLY) || !StrUtil.equals(dataImageFilesInfo.getFileStatus(), FileStatusConstants.INVOICE_UPDATE)){
                DataCurrentTask dataCurrentTask = dataCurrentTaskService.selectDataCurrentTaskByBusinessSerialNo(businessSerialNo);
                DataSynergyAttachment dataSynergyAttachment = new DataSynergyAttachment();
                dataSynergyAttachment.setId(IdUtil.simpleUUID());
                dataSynergyAttachment.setFileId(dataImageFilesInfo.getFileId());
                dataSynergyAttachment.setFileName(dataImageFilesInfo.getFileName());
                dataSynergyAttachment.setFileUrl(dataImageFilesInfo.getUrl());
                dataSynergyAttachment.setTenementNo(LoginHelper.getTenantId());
                dataSynergyAttachment.setPushCount(0L);
                dataSynergyAttachment.setDelCount(0L);
                System.out.println(JSONUtil.toJsonStr(dataCurrentTask));
                dataSynergyAttachment.setTaskDetailInfo(JSONUtil.toJsonStr(dataCurrentTask));
                // 待推送状态
                dataSynergyAttachment.setPushStatus(PushAttachmentConstant.PUSH_STATUS);
                dataSynergyAttachment.insert();
            }
        }
    }
}
