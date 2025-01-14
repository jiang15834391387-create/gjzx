package org.smartlink.business.service.Impl;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.smartlink.business.service.IDataOcrService;
import org.smartlink.common.core.constant.CacheNames;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
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
import java.lang.reflect.Field;
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
    public R uploadImage(MultipartFile multipartFile) throws Exception {

        try {
            //文件为空
            if (multipartFile.isEmpty()) {
                return R.ok("文件为空！");
            }
            DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
            dataImageFilesInfo.setFileId(IdUtil.fastSimpleUUID());
            //文件类型
            String fileSuffix = FileUtils.getFileSuffix(multipartFile.getOriginalFilename());
            //是否切图
            boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_OCR_CUT));
            //是否单图旋转
            boolean isRotate = Boolean.parseBoolean(RedisUtils.getCacheObject(Constants.SYS_CONFIG_KEY + ParamConstants.SYS_IMG_ROTATE));

            //ocr识别
            if (fileSuffix.equals("jpg") || fileSuffix.equals("jpeg") || fileSuffix.equals("png") || fileSuffix.equals("tiff")) {
                //文件最大支持8M
                if (multipartFile.getSize() > MAX_FILE_SIZE) {
                    return R.fail("文件超过8MB!");
                }
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
                dataImageFilesInfo.setFileMd5(originalImage.getETag().replaceAll("^\"+|\"+$", ""));
                dataImageFilesInfo.setFileName(originalImage.getFilename());
                dataImageFilesInfo.setFileSize(String.valueOf(multipartFile.getSize()));

                //图片识别
                List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfo, Base64.getEncoder().encodeToString(multipartFile.getBytes()));
                if (identificationData.size() == 0){
                    return R.fail("识别失败!");
                }
                //是否多图
                boolean multigraph = CollectionUtil.isNotEmpty(identificationData) && identificationData.size() > 1;
                String orientationStr = "0";
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
                JSONObject codeObject = (JSONObject) identificationData.get(0).c;
                // qrcode
                if (codeObject.containsKey("qrcode")) {
                    dataImageFilesInfo.setQrCode(codeObject.getStr("qrcode"));
                }
                // barcode
                if (codeObject.containsKey("barcode")) {
                    dataImageFilesInfo.setQrCode(codeObject.getStr("barcode"));
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
                    byte[] bytes = FileUtils.imageCut(multipartFile.getBytes(),  jsonObject.getStr("region") != null ? jsonObject.getStr("region").split(",") : null, Base64.getEncoder().encodeToString(multipartFile.getBytes()), "false",
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
                    dataImageFilesInfoSm.setFileMd5(multigraphOriginalImage.getETag().replaceAll("^\"+|\"+$", ""));
                    dataImageFilesInfoSm.setSurl(multigraphSmallImage.getUrl());
                    dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
                    dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                    JSONObject inCodeObject = (JSONObject) identificationDatum.c;
                    // qrcode
                    if (inCodeObject.containsKey("qrcode")) {
                        dataImageFilesInfoSm.setQrCode(inCodeObject.getStr("qrcode"));
                    }
                    // barcode
                    if (inCodeObject.containsKey("barcode")) {
                        dataImageFilesInfoSm.setQrCode(inCodeObject.getStr("barcode"));
                    }
                    iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
                    Field field = entity.getClass().getDeclaredField("fileId");
                    field.setAccessible(true); // 允许访问私有字段
                    field.set(entity, dataImageFilesInfoSm.getFileId()); // 修改字段值
                    // 获取所有声明的字段
                    Field[] fields = entity.getClass().getDeclaredFields();
                    boolean hasDetailsField = false;
                    // 检查是否有名为 'details' 的字段
                    for (Field fieldd : fields) {
                        if ("details".equals(fieldd.getName())) {
                            hasDetailsField = true;
                             }
                    }
                    if (hasDetailsField) {
                        // 获取 details 字段
                        Field detailsField = entity.getClass().getDeclaredField("details");
                        detailsField.setAccessible(true);
                        List<?> detailsList = (List<?>) detailsField.get(entity);
                        // 遍历子对象并修改 fileId
                        if (detailsList != null) {
                            for (Object detail : detailsList) {
                                try {
                                    // 修改子对象中的 fileId
                                    Field fileIdField = detail.getClass().getDeclaredField("fileId");
                                    fileIdField.setAccessible(true);
                                    fileIdField.set(detail, dataImageFilesInfoSm.getFileId()); // 设置新的值
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    log.info("无法修改字段: " + e.getMessage());
                                }
                            }
                        }
                    }

                }
                    //识别入库
                    R result = dataOcrService.ocrInsert(identificationDatum.k, identificationDatum.t);
                }
                //手动解析
            } else if (fileSuffix.equals("OFD") || fileSuffix.equals("XML")) {

            } else if (fileSuffix.equals("pdf")) {

            } else {
            }
        } catch (Exception e){
                 e.printStackTrace();
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

}
