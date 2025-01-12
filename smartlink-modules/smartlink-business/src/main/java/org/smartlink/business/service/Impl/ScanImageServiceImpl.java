package org.smartlink.business.service.Impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.smartlink.business.service.IDataOcrService;
import org.smartlink.common.core.constant.CacheNames;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.ocr.constant.OcrConstant;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.enumd.OcrEnumd;
import org.smartlink.common.ocr.factory.OcrFactory;


import org.smartlink.business.service.ScanImageService;

import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.file.Constants;
import org.smartlink.common.core.utils.file.FileUtils;

import org.smartlink.common.core.utils.file.ParamConstants;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.factory.OssFactory;
import org.smartlink.common.redis.utils.CacheUtils;
import org.smartlink.common.redis.utils.QueueUtils;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
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

    @Autowired
    public IDataImageFilesInfoService iDataImageFilesInfoService;

    private final IDataOcrService dataOcrService;

    // 文件大小上限 (8MB)
    private static final long MAX_FILE_SIZE = 8 * 1024 * 1024;

    @Override
    public DataImageFilesInfoVo uploadImage(MultipartFile multipartFile) throws Exception {

        //文件为空
        if (multipartFile.isEmpty()){
            return null;
        }
        DataImageFilesInfoBo dataImageFilesInfoBo = new DataImageFilesInfoBo();
        dataImageFilesInfoBo.setFileId(IdUtil.fastSimpleUUID());
        //文件类型
        String fileSuffix = FileUtils.getFileSuffix(multipartFile.getOriginalFilename());
        //是否切图
        boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_CUT));
        //是否单图旋转
        boolean isRotate = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_IMG_ROTATE));

        //ocr识别
        if (fileSuffix.equals("jpg") || fileSuffix.equals("jpeg") || fileSuffix.equals("png") || fileSuffix.equals("tiff")){
            //文件最大支持8M
            if (multipartFile.getSize() > MAX_FILE_SIZE){
                return null;
            }
            //压缩图片
            ByteArrayOutputStream byteArrayOutputStream = FileUtils.thumbnailImage(multipartFile, FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
            //缩略图
            ByteArrayOutputStream cutThumbnailImgFile = FileUtils.thumbnailSmall(byteArrayOutputStream.toByteArray());
            //转换
            InputStream cutThumbnailImgFileStream = new ByteArrayInputStream(cutThumbnailImgFile.toByteArray());
            //源文件存储
            UploadResult originalImage = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfoBo.getFileId() + "." + fileSuffix, multipartFile.getSize(), multipartFile.getContentType());
            //缩略图存储
            UploadResult smallImage = OssFactory.instance().upload(cutThumbnailImgFileStream, "small_" + dataImageFilesInfoBo.getFileId() + "." + fileSuffix, (long) cutThumbnailImgFile.size(), detectContentType(cutThumbnailImgFileStream));

            //设置图片存储信息
            dataImageFilesInfoBo.setIurl(originalImage.getUrl());
            dataImageFilesInfoBo.setSurl(smallImage.getUrl());
            dataImageFilesInfoBo.setFileMd5(originalImage.getETag());
            dataImageFilesInfoBo.setFileName(originalImage.getFilename());
            dataImageFilesInfoBo.setFileSize(String.valueOf(multipartFile.getSize()));

            iDataImageFilesInfoService.insertByBo(dataImageFilesInfoBo);
            DataImageFilesInfo dataImageFilesInfos = MapstructUtils.convert(dataImageFilesInfoBo, DataImageFilesInfo.class);
            //图片识别
            List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfos, Base64.getEncoder().encodeToString(multipartFile.getBytes()));

            iDataImageFilesInfoService.insertByBo(dataImageFilesInfoBo);
            for (IdentificationData identificationDatum : identificationData) {
                //识别入库
                R result = dataOcrService.ocrInsertOrUpdateByBaseEntity(identificationDatum.k, identificationDatum.t);
            }

            //是否多图
            boolean multigraph = CollectionUtil.isNotEmpty(identificationData) && identificationData.size() > 1;
            //单图旋转
//            if (!multigraph && isRotate) {
//                //单图且旋转
//                //切图
//                byte[] bytes = FileUtils.imageCut(byteArrayOutputStream.toByteArray(), identificationData.get(0).t.getCoordinate(), identificationData.get(0).t.getBase64(), identificationData.get(0).t.getOption(), identificationData.get(0).t.getOrientation());
//                //切割图片压缩
//                ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
//                //切割缩略图
//                ByteArrayOutputStream cutThumbnailSmallFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
//                //切割压缩图片存储
//                UploadResult cutOriginalImage = OssFactory.instance().upload(cutThumbnailFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), dataImageFilesInfo.getFileId()), "JPG");
//                //切割缩略图存储
//                UploadResult cutSmallImage = OssFactory.instance().upload(cutThumbnailSmallFile.toByteArray(), BatchIdUtils.getBatchIdPath(fileUploadDTO.getBatchId(), "small" + dataImageFilesInfo.getFileId()), "JPG");
//                dataImageFilesInfoBo.setIurl(cutOriginalImage.getUrl());
//                dataImageFilesInfoBo.setSurl(cutSmallImage.getUrl());
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
//                    dataImageFilesInfoSm.setFileStatus(FileStatusConstants.SAVED_SUCCESSFULLY);
//                    dataImageFilesInfoSm.setBatchId(dataImageFilesInfo.getBatchId());
//                    dataImageFilesInfoSm.setMessage("上传成功");
//                    //切图
//                    byte[] bytes = FileUtils.imageCut(byteArrayOutputStream.toByteArray(), identificationDatum.t.getCoordinate(), identificationDatum.t.getBase64(), identificationDatum.t.getOption(), identificationDatum.t.getOrientation());
//                    //切割图片压缩
//                    ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
//                    //切割缩略图
//                    ByteArrayOutputStream cutThumbnailSmallFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
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

            //手动解析
        } else if (fileSuffix.equals("OFD") || fileSuffix.equals("XML")){

        } else if (fileSuffix.equals("pdf")){

        } else {}


        //是否OCR
//        boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_OFF)) && !wechatUpload && Boolean.valueOf(fileUploadDTO.getIsOcr());
        //是否查验
//        boolean checkOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CHECK_OFF));
        //是否自定义树节点
//        String treeNode = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.TREE_NODE_CODE);





        //更新源文件,保存树节点
//        saveOrUpdateImageTree(dataImageFilesInfo,fileUploadDTO,wechatUpload,bigImageTree);



        //返回
        return null;
    }

    public static String detectContentType(InputStream inputStream) {
        try {
            Tika tika = new Tika();
            return tika.detect(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
