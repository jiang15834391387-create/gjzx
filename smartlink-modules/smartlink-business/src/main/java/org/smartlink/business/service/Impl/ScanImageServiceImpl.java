package org.smartlink.business.service.Impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tika.Tika;
import org.smartlink.business.service.IDataOcrService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.factory.OcrFactory;


import org.smartlink.business.service.ScanImageService;

import org.smartlink.common.core.utils.file.Constants;
import org.smartlink.common.core.utils.file.FileUtils;

import org.smartlink.common.core.utils.file.ParamConstants;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.factory.OssFactory;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.service.ISysOssService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.smartlink.common.core.utils.file.OfdUtils.exportOfdToStream;
import static org.smartlink.common.core.utils.file.PDFUtil.pdfToImg;


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

    private final ISysOssService iSysOssService;
    private final IDataImageFilesInfoService iDataImageFilesInfoService;
    private final IDataOcrService dataOcrService;

    @Override
    public R<T> uploadImage(MultipartFile multipartFile) throws Exception {

            //是否OCR
            boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_OFF));
            //是否查验
            boolean checkOff = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_CHECK_OFF));
            DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
            dataImageFilesInfo.setFileId(IdUtil.fastSimpleUUID());
            //文件类型
            String fileSuffix = FileUtils.getFileSuffix(multipartFile.getOriginalFilename());
            //是否切图
            boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_CUT));
            //是否单图旋转
            boolean isRotate = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_IMG_ROTATE));
            //ocr识别
            List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfo, Base64.getEncoder().encodeToString(multipartFile.getBytes()), fileSuffix);
            //文件类型是否符合参数配置
            if (isFileTypeAllowed(fileSuffix)){
                    //ocr识别
                    if (fileSuffix.equals("ofd")) {

                     /////////////////
                        dataImageFilesInfo.setMessage(dataImageFilesInfo.getMessage());
                        dataImageFilesInfo.setInvoice(identificationData.get(0).k);

                        ByteArrayOutputStream outputStream = exportOfdToStream(multipartFile.getBytes(), "PNG", 20d);
//                        Path outputPath = Paths.get("D:/HuaChuang_LqmWork/output.png");
//                        Files.write(outputPath, outputStream.toByteArray());
                        //切割图片压缩
                        ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(outputStream.toByteArray(), "png");
                        //缩略图
                        ByteArrayOutputStream multigraphCutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                        //转换
                        InputStream multigraphCutThumbnailImgFileStream = new ByteArrayInputStream(multigraphCutThumbnailImgFile.toByteArray());
                        //源文件存储
                        UploadResult multigraphOriginalImage = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfo.getFileId() + "." + "ofd",  multipartFile.getSize(), multipartFile.getContentType());
                        //缩略图存储
                        UploadResult multigraphSmallImage = OssFactory.instance().upload(multigraphCutThumbnailImgFileStream, "small_" + dataImageFilesInfo.getFileId() + "." + "png", (long) multigraphCutThumbnailImgFile.size(), detectContentType(multigraphCutThumbnailImgFileStream));
                        Object entity = identificationData.get(0).t;
                        dataImageFilesInfo.setFileName(multigraphOriginalImage.getFilename());
                        dataImageFilesInfo.setIurl(multigraphOriginalImage.getUrl());
                        dataImageFilesInfo.setFileMd5(multigraphOriginalImage.getETag());
                        dataImageFilesInfo.setSurl(multigraphSmallImage.getUrl());
                        dataImageFilesInfo.setFileSize(String.valueOf(multipartFile.getSize()));
                        dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                        iDataImageFilesInfoService.insert(dataImageFilesInfo);
                        updateFileId(entity, dataImageFilesInfo.getFileId());
//                        //发票入库
//                         R<T> result = dataOcrService.ocrInsert(identificationData.get(0).k, identificationData.get(0).t);
//                        //发票查验
//                        for (int identificationDatum  = 0; identificationDatum  < identificationData.size(); identificationDatum ++) {
//                            R<T> result = dataOcrService.ocrInsert(identificationData.get(identificationDatum).k, identificationData.get(identificationDatum).t);
//                        }

                    } else if (fileSuffix.equals("xml")) {
                        //源文件存储
                        UploadResult multigraphOriginalXml = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfo.getFileId() + "." + "xml", multipartFile.getSize(), multipartFile.getContentType());
                        ////////////////
                        Object entity = identificationData.get(0).t;
                        dataImageFilesInfo.setMessage(dataImageFilesInfo.getMessage());
                        dataImageFilesInfo.setInvoice(identificationData.get(0).k);
                        dataImageFilesInfo.setFileName(multigraphOriginalXml.getFilename());
                        dataImageFilesInfo.setIurl(multigraphOriginalXml.getUrl());
                        dataImageFilesInfo.setFileMd5(multigraphOriginalXml.getETag());
                        dataImageFilesInfo.setFileSize(String.valueOf(multipartFile.getSize()));
                        dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                        iDataImageFilesInfoService.insert(dataImageFilesInfo);
                        updateFileId(entity, dataImageFilesInfo.getFileId());
//                        //发票入库
//                        R<T> result = dataOcrService.ocrInsert(identificationData.get(0).k, identificationData.get(0).t);
                        //                //发票查验
                        //                for (int identificationDatum  = 0; identificationDatum  < identificationData.size(); identificationDatum ++) {
                        //                    R<T> result = dataOcrService.ocrInsert(identificationData.get(identificationDatum).k, identificationData.get(identificationDatum).t);
                        //                }

                    } else if (fileSuffix.equals("pdf")) {//pdf类型

                        SysOssVo pdfUpload = iSysOssService.upload(multipartFile);
                        // 读取 PDF 文件并转换为字节数组
                        List<byte[]> images = pdfToImg(multipartFile.getBytes());
                        /////////
                        String orientationStr = "";
                        for (int identificationDatum = 0; identificationDatum < identificationData.size(); identificationDatum++) {

                            InputStream inputStream = new ByteArrayInputStream(images.get(identificationDatum));
                            DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
                            dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
                            dataImageFilesInfoSm.setMessage(dataImageFilesInfo.getMessage());
                            dataImageFilesInfoSm.setInvoice(identificationData.get(identificationDatum).k);

                            //取旋转角度和坐标
                            Object entity = identificationData.get(identificationDatum).t;
                            String jsonString = cn.hutool.json.JSONUtil.toJsonStr(entity);
                            JSONObject jsonObject = JSONUtil.parseObj(jsonString);
                            orientationStr = jsonObject.getStr("orientation");
                            //旋转
                            byte[] bytes = FileUtils.imageCut(images.get(identificationDatum), jsonObject.getStr("region") != null ? jsonObject.getStr("region").split(",") : null, Base64.getEncoder().encodeToString(images.get(identificationDatum)), "false",
                                (orientationStr != null && !orientationStr.isEmpty()) ? Integer.parseInt(orientationStr) : 0);
                            ByteArrayOutputStream bytesArrayOutputStream = new ByteArrayOutputStream();
                            bytesArrayOutputStream.write(bytes);
                            //切割图片压缩
                            ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
                            //缩略图
                            ByteArrayOutputStream multigraphCutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                            //转换
                            InputStream multigraphCutThumbnailImgFileStream = new ByteArrayInputStream(multigraphCutThumbnailImgFile.toByteArray());
                            //源文件存储
                            UploadResult multigraphOriginalImage = OssFactory.instance().upload(inputStream, dataImageFilesInfoSm.getFileId() + "." + "jpg", (long) images.get(identificationDatum).length, "image/jpeg");
                            //缩略图存储
                            UploadResult multigraphSmallImage = OssFactory.instance().upload(multigraphCutThumbnailImgFileStream, "small_" + dataImageFilesInfoSm.getFileId() + "." + "jpg", (long) multigraphCutThumbnailImgFile.size(), detectContentType(multigraphCutThumbnailImgFileStream));

                            dataImageFilesInfoSm.setFileName(multigraphOriginalImage.getFilename());
                            dataImageFilesInfoSm.setIurl(multigraphOriginalImage.getUrl());
                            dataImageFilesInfoSm.setPurl(pdfUpload.getUrl());
                            dataImageFilesInfoSm.setFileMd5(multigraphOriginalImage.getETag());
                            dataImageFilesInfoSm.setSurl(multigraphSmallImage.getUrl());
                            dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
                            dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                            iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
                            updateFileId(entity, dataImageFilesInfoSm.getFileId());
                        }
//                        //发票入库
//                        for (int identificationDatum = 0; identificationDatum < identificationData.size(); identificationDatum++) {
//                            R<T> result = dataOcrService.ocrInsert(identificationData.get(identificationDatum).k, identificationData.get(identificationDatum).t);
//                        }
                        //                //发票查验
                        //                for (int identificationDatum  = 0; identificationDatum  < identificationData.size(); identificationDatum ++) {
                        //                    R<T> result = dataOcrService.ocrInsert(identificationData.get(identificationDatum).k, identificationData.get(identificationDatum).t);
                        //                }

                    } else {//图片相关类型

                        //压缩图片
                        ByteArrayOutputStream byteArrayOutputStream = FileUtils.thumbnailImage(multipartFile, fileSuffix);
                        //缩略图
                        ByteArrayOutputStream cutThumbnailImgFile = FileUtils.thumbnailSmall(byteArrayOutputStream.toByteArray());
                        //转换
                        InputStream cutThumbnailImgFileStream = new ByteArrayInputStream(cutThumbnailImgFile.toByteArray());
                        //源文件存储
                        UploadResult originalImage = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfo.getFileId() + "." + fileSuffix, multipartFile.getSize(), multipartFile.getContentType());
                        //缩略图存储
                        UploadResult smallImage = OssFactory.instance().upload(cutThumbnailImgFileStream, "small_" + dataImageFilesInfo.getFileId() + "." + fileSuffix, (long) cutThumbnailImgFile.size(), detectContentType(cutThumbnailImgFileStream));

                        //设置图片存储信息
                        dataImageFilesInfo.setIurl(originalImage.getUrl());
                        dataImageFilesInfo.setSurl(smallImage.getUrl());
                        // 移除首尾多余的引号
                        dataImageFilesInfo.setFileMd5(originalImage.getETag());
                        dataImageFilesInfo.setFileName(originalImage.getFilename());
                        dataImageFilesInfo.setFileSize(String.valueOf(multipartFile.getSize()));

                        /////////////////////
                      if (identificationData.size() == 0) {
                            return R.fail("识别失败!");
                        }
                        //是否多图
                        boolean multigraph = CollectionUtil.isNotEmpty(identificationData) && identificationData.size() > 1;
                        String orientationStr = "";
                        //单图旋转
                        if (!multigraph && isRotate) {
                            //取旋转角度和坐标
                            Object entity = identificationData.get(0).t;
                            String jsonString = cn.hutool.json.JSONUtil.toJsonStr(entity);
                            JSONObject jsonObject = JSONUtil.parseObj(jsonString);
                            orientationStr = jsonObject.getStr("orientation");
                            //单图且旋转
                            //切图
                            byte[] bytes = FileUtils.imageCut(multipartFile.getBytes(), jsonObject.getStr("region") != null ? jsonObject.getStr("region").split(",") : null, Base64.getEncoder().encodeToString(multipartFile.getBytes()), "false",
                                (orientationStr != null && !orientationStr.isEmpty()) ? Integer.parseInt(orientationStr) : 0);
                            //切割图片压缩
                            ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
                            //缩略图
                            ByteArrayOutputStream isRotatecutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                            //转换
                            InputStream isRotatecutThumbnailImgFileStream = new ByteArrayInputStream(isRotatecutThumbnailImgFile.toByteArray());
                            //缩略图存储
                            UploadResult cutSmallImage = OssFactory.instance().upload(isRotatecutThumbnailImgFileStream, "small_" + dataImageFilesInfo.getFileId() + "." + fileSuffix, (long) isRotatecutThumbnailImgFile.size(), detectContentType(isRotatecutThumbnailImgFileStream));
                            //单图且旋转缩略图路径
                            dataImageFilesInfo.setSurl(cutSmallImage.getUrl());
                        }

                        iDataImageFilesInfoService.insert(dataImageFilesInfo);
                        for (IdentificationData identificationDatum : identificationData) {

                            if (multigraph && isCrop) {
                                //取旋转角度和坐标
                                Object entity = identificationDatum.t;
                                String jsonString = cn.hutool.json.JSONUtil.toJsonStr(entity);
                                JSONObject jsonObject = JSONUtil.parseObj(jsonString);
                                //切图
                                DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
                                dataImageFilesInfoSm.setParentFileId(dataImageFilesInfo.getFileId());
                                dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
                                dataImageFilesInfoSm.setMessage(dataImageFilesInfo.getMessage());
                                dataImageFilesInfoSm.setInvoice(identificationDatum.k);
                                //切图
                                byte[] bytes = FileUtils.imageCut(multipartFile.getBytes(), jsonObject.getStr("region") != null ? jsonObject.getStr("region").split(",") : null, Base64.getEncoder().encodeToString(multipartFile.getBytes()), "false",
                                    (orientationStr != null && !orientationStr.isEmpty()) ? Integer.parseInt(orientationStr) : 0);
                                ByteArrayOutputStream bytesArrayOutputStream = new ByteArrayOutputStream();
                                bytesArrayOutputStream.write(bytes);
                                //切割图片压缩
                                ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(bytes, "JPG");
                                //缩略图
                                ByteArrayOutputStream multigraphCutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                                //转换
                                InputStream multigraphCutThumbnailImgFileStream = new ByteArrayInputStream(multigraphCutThumbnailImgFile.toByteArray());
                                //源文件存储
                                UploadResult multigraphOriginalImage = OssFactory.instance().upload(new ByteArrayInputStream(bytes), dataImageFilesInfoSm.getFileId() + "." + fileSuffix, (long) bytesArrayOutputStream.size(), multipartFile.getContentType());
                                //缩略图存储
                                UploadResult multigraphSmallImage = OssFactory.instance().upload(multigraphCutThumbnailImgFileStream, "small_" + dataImageFilesInfoSm.getFileId() + "." + fileSuffix, (long) multigraphCutThumbnailImgFile.size(), detectContentType(multigraphCutThumbnailImgFileStream));

                                dataImageFilesInfoSm.setFileName(multigraphOriginalImage.getFilename());
                                dataImageFilesInfoSm.setIurl(multigraphOriginalImage.getUrl());
                                dataImageFilesInfoSm.setFileMd5(multigraphOriginalImage.getETag());
                                dataImageFilesInfoSm.setSurl(multigraphSmallImage.getUrl());
                                dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
                                dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                                iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
                                updateFileId(entity, dataImageFilesInfoSm.getFileId());
                            }
                        }
//                        //发票入库
//                        for (IdentificationData identificationDatum : identificationData) {
//                            R<T> result = dataOcrService.ocrInsert(identificationDatum.k, identificationDatum.t);
//                        }
                        //                //查验发票
                        //                for (IdentificationData identificationDatum : identificationData) {
                        //                    R result = dataOcrService.ocrInsert(identificationDatum.k, identificationDatum.t);
                        //                }
                    }

                    //发票入库
                    for (IdentificationData identificationDatum : identificationData) {
                        R<T> result = dataOcrService.ocrInsert(identificationDatum.k, identificationDatum.t);
                    }

//                    //发票查验
//                    for (IdentificationData identificationDatum : identificationData) {
//                        Object entity = identificationDatum.t;
//                        String jsonString = cn.hutool.json.JSONUtil.toJsonStr(entity);
//                        JSONObject jsonObject = JSONUtil.parseObj(jsonString);
//                        String CheckFileId = jsonObject.getStr("fileId");
//                        R<T> result = dataOcrService.ocrInsert(identificationDatum.k, identificationDatum.t);
//                    }

            } else {
                return R.ok("文件类型不符合参数配置！");
            }
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

    /**
     * 更新对象及其子对象的 fileId 字段
     *
     * @param entity   需要更新的主对象
     * @param fileId   要设置的 fileId 值
     * @throws Exception 如果反射操作失败
     */
    public static void updateFileId(Object entity, String fileId) throws Exception {
        if (entity == null || fileId == null) {
            throw new IllegalArgumentException("参数 entity 和 fileId 不能为空");
        }
        // 更新主对象的 fileId 字段
        Field fileIdField = entity.getClass().getDeclaredField("fileId");
        fileIdField.setAccessible(true);
        fileIdField.set(entity, fileId);

        // 检查是否有名为 'details' 的字段
        Field[] fields = entity.getClass().getDeclaredFields();
        boolean hasDetailsField = false;
        for (Field field : fields) {
            if ("details".equals(field.getName())) {
                hasDetailsField = true;
                break;
            }
        }
        if (hasDetailsField) {
            // 获取 details 字段并处理子对象
            Field detailsField = entity.getClass().getDeclaredField("details");
            detailsField.setAccessible(true);
            List<?> detailsList = (List<?>) detailsField.get(entity);

            if (detailsList != null) {
                for (Object detail : detailsList) {
                    // 更新子对象的 fileId 字段
                    Field detailFileIdField = detail.getClass().getDeclaredField("fileId");
                    detailFileIdField.setAccessible(true);
                    detailFileIdField.set(detail, fileId);
                }
            }
        }
    }

    /**
     * 检查文件类型是否符合参数配置
     * @param fileSuffix   文件类型
     */
    public boolean isFileTypeAllowed(String fileSuffix) throws Exception {
        String a = ".AVIF,.WMF,.EMF,.JPEG,.FPX,.BMP,.GIF,.SVG,.ICO,.PNG,.JPG,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.ofd,.xml";
        RedisUtils.setCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_INVOICE_TYPE, a);
        // 从 Redis 获取允许的文件类型
        String fileTypes = RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_INVOICE_TYPE);
        if (StringUtils.hasText(fileTypes)) {
            // 转换为 Set，并去掉类型前的 `.`
            Set<String> allowedTypes = Arrays.stream(fileTypes.split(","))
                .map(type -> type.startsWith(".") ? type.substring(1).toLowerCase() : type.toLowerCase()) // 去掉前缀 `.`
                .collect(Collectors.toSet());

            // 判断文件后缀是否在允许的类型中
            return allowedTypes.contains(fileSuffix.toLowerCase());
        }

        // 如果未获取到配置，默认不允许
        return false;
    }



}
