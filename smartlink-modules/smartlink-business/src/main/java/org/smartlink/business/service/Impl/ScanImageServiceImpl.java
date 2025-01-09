package org.smartlink.business.service.Impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.dromara.common.ocr.entity.IdentificationData;
import org.dromara.common.ocr.factory.OcrFactory;
import org.smartlink.business.domain.DataImageFilesInfo;
import org.smartlink.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.business.enumd.FileStatusEnumd;
import org.smartlink.business.scan.dto.FileUploadDTO;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.business.utils.BatchIdUtils;
import org.smartlink.business.utils.Constants;
import org.smartlink.business.utils.FilesUtils;
import org.smartlink.common.core.utils.file.FileUtils;
import org.smartlink.business.utils.ParamConstants;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.factory.OssFactory;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;

/**
 * @author shidunkai
 * @title 文件上传实现
 * @description 文件上传实现
 * @date 2022-04
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScanImageServiceImpl implements ScanImageService {
//    private final CurrentTaskService currentTaskService;
//    private final IDataImageTreeService imageTreeService;
//    private final IDataOcrService dataOcrService;
//    private final IDataImageFilesInfoService dataImageFilesInfoService;
//
//    private final IDataCmInfoService dataCmInfoService;
//    private final IDataCurrentTaskService dataCurrentTaskService;
//    private final DataCurrentTaskMapper dataCurrentTaskMapper;
//    private final DataImageFilesInfoMapper imageFilesInfoMapper;
//
//    private final BreCheckInvoiceService breCheckInvoiceService;
//    private final HardWareMessageService hardWareMessageService;
    @Autowired
    public ISysOssService iSysOssService;

    @Override
    public DataImageFilesInfoVo uploadImage(MultipartFile multipartFile) throws Exception {
//
//        //是否切图
//        boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_CUT));
//        //是否单图旋转
//        boolean isRotate = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_IMG_ROTATE));
//        //是否OCR
//        boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_OFF)) && !wechatUpload && Boolean.valueOf(fileUploadDTO.getIsOcr());
//        //是否查验
//        boolean checkOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CHECK_OFF));
//        //是否自定义树节点
//        String treeNode = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.TREE_NODE_CODE);
//
//        //压缩图片
//        ByteArrayOutputStream byteArrayOutputStream = FilesUtils.thumbnailImage(multipartFile, FilenameUtils.getExtension(fileUploadDTO.getFileName()));
//        //缩略图
//        ByteArrayOutputStream cutThumbnailImgFile = FilesUtils.thumbnailSmall(byteArrayOutputStream.toByteArray());
//        iSysOssService.upload(cutThumbnailImgFile);
//        //压缩图片存储
//        UploadResult originalImage = OssFactory.instance().upload(byteArrayOutputStream.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), "JPG");
//        //缩略图存储
//        UploadResult smallImage = OssFactory.instance().upload(cutThumbnailImgFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small_" + dataImageFilesInfo.getFileId()), "JPG");
//
//        //设置图片存储信息
//        dataImageFilesInfo.setIurl(originalImage.getUrl());
//        dataImageFilesInfo.setUrl(originalImage.getUrl());
//        dataImageFilesInfo.setSurl(smallImage.getUrl());
//
//        DataImageTree bigImageTree = new DataImageTree();
//        //初始化图片信息和树节点
//        saveOrUpdateImageTree(dataImageFilesInfo,fileUploadDTO,wechatUpload,bigImageTree);
//
//        //业务参数判断
//        if (ocrOff || (wechatUpload && Boolean.parseBoolean(fileUploadDTO.getIsOcr()))) {
//            //图片识别
//            List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfo, byteArrayOutputStream.toByteArray());
//            //是否多图
//            boolean multigraph = CollectionUtil.isNotEmpty(identificationData) && identificationData.size() > 1;
//            //单图旋转
//            if (!multigraph && isRotate) {
//                //单图且旋转
//                //切图
//                byte[] bytes = FilesUtils.imageCut(byteArrayOutputStream.toByteArray(), identificationData.get(0).t.getCoordinate(), identificationData.get(0).t.getBase64(), identificationData.get(0).t.getOption(), identificationData.get(0).t.getOrientation());
//                //切割图片压缩
//                ByteArrayOutputStream cutThumbnailFile = FilesUtils.thumbnailImage(bytes, "JPG");
//                //切割缩略图
//                ByteArrayOutputStream cutThumbnailSmallFile = FilesUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
//                //切割压缩图片存储
//                UploadResult cutOriginalImage = OssFactory.instance().upload(cutThumbnailFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), "JPG");
//                //切割缩略图存储
//                UploadResult cutSmallImage = OssFactory.instance().upload(cutThumbnailSmallFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small" + dataImageFilesInfo.getFileId()), "JPG");
//                dataImageFilesInfo.setIurl(cutOriginalImage.getUrl());
//                dataImageFilesInfo.setSurl(cutSmallImage.getUrl());
//            }
//            int i = 1;
//            HashSet<String> typeSet = new HashSet<>();
//            for (IdentificationData identificationDatum : identificationData) {
//                typeSet.add(identificationDatum.k);
//                if (multigraph && isCrop) {
//                    //切图
//                    DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
//                    dataImageFilesInfoSm.setParentFileId(dataImageFilesInfo.getFileId());
//                    dataImageFilesInfoSm.setFolderId(fileUploadDTO.getFolderId());
//                    dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
//                    dataImageFilesInfoSm.setFileStatus(FileStatusEnumd.UPLOADED_SUCCESSFUL_CODE.getCode());
//                    dataImageFilesInfoSm.setBatchId(dataImageFilesInfo.getBatchId());
//                    dataImageFilesInfoSm.setMessage("上传成功");
//                    //切图
//                    byte[] bytes = FilesUtils.imageCut(byteArrayOutputStream.toByteArray(), identificationDatum.t.getCoordinate(), identificationDatum.t.getBase64(), identificationDatum.t.getOption(), identificationDatum.t.getOrientation());
//                    //切割图片压缩
//                    ByteArrayOutputStream cutThumbnailFile = FilesUtils.thumbnailImage(bytes, "JPG");
//                    //切割缩略图
//                    ByteArrayOutputStream cutThumbnailSmallFile = FilesUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
//                    //切割压缩图片存储
//                    UploadResult cutOriginalImage = OssFactory.instance().upload(cutThumbnailFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfoSm.getFileId()), "JPG");
//                    //切割缩略图存储
//                    UploadResult cutSmallImage = OssFactory.instance().upload(cutThumbnailSmallFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small_" + dataImageFilesInfoSm.getFileId()), "JPG");
//                    dataImageFilesInfoSm.setFileName(i + "_" + dataImageFilesInfo.getFileName());
//                    dataImageFilesInfoSm.setUserId(dataImageFilesInfo.getUserId());
//                    dataImageFilesInfoSm.setIurl(cutOriginalImage.getUrl());
//                    dataImageFilesInfoSm.setUrl(cutOriginalImage.getUrl());
//                    dataImageFilesInfoSm.setSurl(cutSmallImage.getUrl());
//                    dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
//                    dataImageFilesInfoSm.setFileType(identificationDatum.k);
//                    identificationDatum.t.setOcrFileId(dataImageFilesInfoSm.getFileId());
//                    dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationDatum.k, identificationDatum.t);
//
//                    //查验方法
//                    checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfoSm, fileUploadDTO);
//                    //图片预处理
//                    breCheckInvoiceService.breCheckInvoice(dataImageFilesInfoSm, fileUploadDTO.getOrgCode());
//                    //重扫、补扫修改影像文件状态
//                    upImgStatus(dataImageFilesInfoSm, fileUploadDTO.getTaskStatus(), fileUploadDTO.getSupplementaryScan());
//                    DataImageTree dataImageTreeSm = new DataImageTree();
//                    //更新源文件,保存树节点
//                    saveOrUpdateImageTree(dataImageFilesInfoSm, fileUploadDTO, true,dataImageTreeSm);
//                    //原图是附件
//                    dataImageFilesInfo.setFileType(InvoiceConstants.IMAGE_OTHERS);
//                    //是否自定义节点
//                    if (StrUtil.isNotBlank(treeNode) && StrUtil.isNotBlank(fileUploadDTO.getBillType())) {
//                        saveCustomize(treeNode, dataImageTreeSm, dataImageFilesInfoSm, fileUploadDTO.getBillType());
//                    }
//                    i++;
//                } else if (!isCrop && multigraph) {
//                    dataOcrService.ocrInsertBaseEntity(identificationDatum.k, identificationDatum.t);
//                    checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfo, fileUploadDTO);
//                    //原图是多票据
//                    dataImageFilesInfo.setFileType(InvoiceConstants.INVOICE_MUCH_NCC);
//                } else {
//                    dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationDatum.k, identificationDatum.t);
//                    checkInvoice(checkOff, wechatUpload, identificationDatum, dataImageFilesInfo, fileUploadDTO);
//                    //单图类型
//                    dataImageFilesInfo.setFileType(identificationDatum.k);
//                }
//            }
//            String typeArr = String.join(Constants.CONNECT_COMMA_SYMBOL, typeSet);
//            dataImageFilesInfo.setIncludeTypeArr(typeArr);
//        }
//        // 判断NCC开关是否启用
//        String status = NcProperties.getDefaultPropertiesStatus();
//        // 与NC业务系统相关逻辑
//        boolean ncEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_NC_BUSINESS));
//        if (ncEnabled && StrUtil.equals(status, Constants.CONFIG_OFF_STATUS) && Boolean.parseBoolean(fileUploadDTO.getIsOcr()) && !wechatUpload) {
//            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
//            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
//            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
//            ncImageServiceDTO.setFile(Base64.encode(multipartFile.getBytes()));
//            ncImageServiceDTO.setUploadBusinessType(NcConstant.NORMAL_UPLOAD_INVOICE);
//            dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
//        }
//        boolean bipEnabled = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_BIP_BUSINESS));
//        if (bipEnabled && Boolean.parseBoolean(fileUploadDTO.getIsOcr())) {
//            NcImageServiceDTO ncImageServiceDTO = new NcImageServiceDTO();
//            ncImageServiceDTO.setDataImageFilesInfo(dataImageFilesInfo);
//            ncImageServiceDTO.setBusinessSerialNo(fileUploadDTO.getBusinessSerialNo());
//            ncImageServiceDTO.setFile(Base64.encode(multipartFile.getBytes()));
//            ncImageServiceDTO.setBarcode(fileUploadDTO.getBarCode());
//            ncImageServiceDTO.setBillId(fileUploadDTO.getBusinessSerialNo());
//            //ncImageServiceDTO.setYtenantId(dataImageFilesInfo.getTenantId());
//            dataImageFilesInfo = NcConfigFactory.instance().doBusinessService(ncImageServiceDTO);
//        }
//
//
//        //重扫、补扫修改影像文件状态
//        upImgStatus(dataImageFilesInfo, fileUploadDTO.getTaskStatus(), fileUploadDTO.getSupplementaryScan());
//        //图片预处理
//        breCheckInvoiceService.breCheckInvoice(dataImageFilesInfo, fileUploadDTO.getOrgCode());
//        //更新源文件,保存树节点
//        saveOrUpdateImageTree(dataImageFilesInfo,fileUploadDTO,wechatUpload,bigImageTree);
//
//        //是否自定义树节点
//        if (StrUtil.isNotBlank(treeNode) && StrUtil.isNotBlank(fileUploadDTO.getBillType())) {
//            saveCustomize(treeNode,bigImageTree, dataImageFilesInfo, fileUploadDTO.getBillType());
//        }
//
//        //返回
//        return BeanCopyUtils.copy(dataImageFilesInfo, new DataImageFilesInfoVo());
        return null;
    }


}
