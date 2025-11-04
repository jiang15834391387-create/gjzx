package org.smartlink.business.service.Impl;


import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.internet.MimeUtility;
import jakarta.mail.search.SearchTerm;
import jakarta.mail.search.SubjectTerm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tika.Tika;
import org.smartlink.business.doman.entity.Invoice;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;
import org.smartlink.business.invoice.check.CheckInvoice;
import org.smartlink.business.service.IDataOcrService;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.business.util.InvoiceGeneratorXEasyPdf;
import org.smartlink.business.util.MoneyToChineseUtil;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.domain.model.LoginUser;
import org.smartlink.common.core.enums.CheckInvoiceStatusEnumd;
import org.smartlink.common.core.enums.FileStatusEnumd;
import org.smartlink.common.core.enums.InvoiceGlorityEnumd;
import org.smartlink.common.core.utils.file.Constants;
import org.smartlink.common.core.utils.file.FileUtils;
import org.smartlink.common.core.utils.file.ParamConstants;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.OtherAttachments;
import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.DataOcrInfo;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.smartlink.common.entity.domain.business.service.IOtherAttachmentsService;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.ocr.factory.OcrFactory;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.factory.OssFactory;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.domain.vo.SysUserVo;
import org.smartlink.system.service.ISysOssService;
import org.smartlink.system.service.ISysUserService;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.smartlink.common.core.utils.file.FileUtils.getFileSuffix;
import static org.smartlink.common.core.utils.file.OfdUtils.exportOfdToStream;
import static org.smartlink.common.core.utils.file.PDFUtil.pdfToImg;


/**
 * @author lqm
 * @title 文件上传实现
 * @description 文件上传实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScanImageServiceImpl implements ScanImageService {

    private final ISysOssService iSysOssService;
    private final IDataImageFilesInfoService iDataImageFilesInfoService;
    private final IDataOcrService dataOcrService;
    private final CheckInvoice checkInvoice;
    private final ISysUserService iSysUserService;
    private final IOtherAttachmentsService otherAttachmentsService;

    /**
     * 发票上传
     *
     * @param multipartFile 文件对象
     */
    @Override
    public R<T> uploadImage(MultipartFile multipartFile, String uploadType) throws Exception {

        //文件类型
        String fileSuffix ="";
        if (uploadType.equals("0")){
            fileSuffix = getFileSuffix(MimeUtility.decodeText(multipartFile.getOriginalFilename()));
        } else {
            fileSuffix = FileUtils.getFileSuffix(multipartFile.getOriginalFilename());
        }
        //是否OCR
        boolean ocrOff = Boolean.parseBoolean(RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, ParamConstants.SYS_OCR_OFF));
        if (ocrOff){
            //是否查验
            boolean checkOff = Boolean.parseBoolean(RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, ParamConstants.SYS_CHECK_OFF));
            //是否切图
            boolean isCrop = Boolean.parseBoolean(RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, ParamConstants.SYS_OCR_CUT));
            //是否单图旋转
            boolean isRotate = Boolean.parseBoolean(RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, ParamConstants.SYS_IMG_ROTATE));
            DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
            dataImageFilesInfo.setFileId(IdUtil.fastSimpleUUID());

            //文件类型是否符合参数配置
            if (isFileTypeAllowed(fileSuffix)) {
                //ocr识别
                List<IdentificationData> identificationData = OcrFactory.instance().getIdentificationData(dataImageFilesInfo, Base64.getEncoder().encodeToString(multipartFile.getBytes()), fileSuffix);
                if (identificationData == null) {
                    return handleOcrFailure(multipartFile, fileSuffix,"", FileStatusEnumd.OCR_FAILED.getCode(), FileStatusEnumd.OCR_FAILED.getDesc());
                }
                //ocr识别
                if (fileSuffix.equals("ofd")) {

                    for (IdentificationData identificationDaOfd : identificationData) {
                        DataImageFilesInfo dataImageFilesInfoOfd = new DataImageFilesInfo();
                        dataImageFilesInfoOfd.setFileId(IdUtil.simpleUUID());
                        dataImageFilesInfoOfd.setMessage(identificationDaOfd.m);
                        dataImageFilesInfoOfd.setInvoice(identificationDaOfd.k);
                        ByteArrayOutputStream outputStream = exportOfdToStream(multipartFile.getBytes(), "PNG", 20d);
                        //切割图片压缩
                        ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(outputStream.toByteArray(), "png");
                        //缩略图
                        ByteArrayOutputStream multigraphCutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                        //转换
                        InputStream multigraphCutThumbnailImgFileStream = new ByteArrayInputStream(multigraphCutThumbnailImgFile.toByteArray());
                        //源文件存储
                        UploadResult multigraphOriginalImage = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfoOfd.getFileId() + "." + "ofd", multipartFile.getSize(), multipartFile.getContentType());
                        //缩略图存储
                        UploadResult multigraphSmallImage = OssFactory.instance().upload(multigraphCutThumbnailImgFileStream, "small_" + dataImageFilesInfoOfd.getFileId() + "." + "png", (long) multigraphCutThumbnailImgFile.size(), detectContentType(multigraphCutThumbnailImgFileStream));
                        Object entity = identificationDaOfd.t;
                        dataImageFilesInfoOfd.setFileName(InvoiceGlorityEnumd.getByCode(identificationDaOfd.k).getDesc() + "_" +multigraphOriginalImage.getFilename());
                        dataImageFilesInfoOfd.setIurl(multigraphOriginalImage.getUrl());
                        dataImageFilesInfoOfd.setFileMd5(multigraphOriginalImage.getETag());
                        dataImageFilesInfoOfd.setSurl(multigraphSmallImage.getUrl());
                        dataImageFilesInfoOfd.setFileSize(String.valueOf(multipartFile.getSize()));
                        dataImageFilesInfoOfd.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                        iDataImageFilesInfoService.insert(dataImageFilesInfoOfd);
                        updateFileId(entity, dataImageFilesInfoOfd.getFileId());
                    }

                } else if (fileSuffix.equals("xml")) {

                    for (IdentificationData identificationDaXml : identificationData) {
                        DataImageFilesInfo dataImageFilesInfoXml = new DataImageFilesInfo();
                        dataImageFilesInfoXml.setFileId(IdUtil.simpleUUID());
                        //源文件存储
                        UploadResult multigraphOriginalXml = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfoXml.getFileId() + "." + "xml", multipartFile.getSize(), multipartFile.getContentType());
                        Object entity = identificationDaXml.t;
                        dataImageFilesInfoXml.setMessage(identificationDaXml.m);
                        dataImageFilesInfoXml.setInvoice(identificationDaXml.k);
                        dataImageFilesInfoXml.setFileName(InvoiceGlorityEnumd.getByCode(identificationDaXml.k).getDesc() + "_" + multigraphOriginalXml.getFilename());
                        dataImageFilesInfoXml.setIurl(multigraphOriginalXml.getUrl());
                        dataImageFilesInfoXml.setFileMd5(multigraphOriginalXml.getETag());
                        dataImageFilesInfoXml.setFileSize(String.valueOf(multipartFile.getSize()));
                        dataImageFilesInfoXml.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                        iDataImageFilesInfoService.insert(dataImageFilesInfoXml);
                        updateFileId(entity, dataImageFilesInfoXml.getFileId());
                    }

                } else if (fileSuffix.equals("pdf")) {//pdf类型

                    SysOssVo pdfUpload = iSysOssService.upload(multipartFile);
                    // 读取 PDF 文件并转换为字节数组
                    List<byte[]> images = pdfToImg(multipartFile.getBytes());
                    String orientationStr = "";
                    for (int identificationDatum = 0; identificationDatum < identificationData.size(); identificationDatum++) {

                        InputStream inputStream = new ByteArrayInputStream(images.get(identificationDatum));
                        DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
                        dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
                        dataImageFilesInfoSm.setMessage(identificationData.get(identificationDatum).m);
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

                        dataImageFilesInfoSm.setFileName(InvoiceGlorityEnumd.getByCode(identificationData.get(identificationDatum).k).getDesc() + "_" + multigraphOriginalImage.getFilename());
                        dataImageFilesInfoSm.setIurl(multigraphOriginalImage.getUrl());
                        dataImageFilesInfoSm.setPurl(pdfUpload.getUrl());
                        dataImageFilesInfoSm.setFileMd5(multigraphOriginalImage.getETag());
                        dataImageFilesInfoSm.setSurl(multigraphSmallImage.getUrl());
                        dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
                        dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                        iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
                        updateFileId(entity, dataImageFilesInfoSm.getFileId());
                    }

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
                    dataImageFilesInfo.setFileMd5(originalImage.getETag());
                    dataImageFilesInfo.setFileName(InvoiceGlorityEnumd.getByCode(dataImageFilesInfo.getInvoice()).getDesc() + "_" + originalImage.getFilename());
                    dataImageFilesInfo.setFileSize(String.valueOf(multipartFile.getSize()));

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

                            DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
                            dataImageFilesInfoSm.setParentFileId(dataImageFilesInfo.getFileId());
                            dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
                            dataImageFilesInfoSm.setMessage(identificationDatum.m);
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

                            dataImageFilesInfoSm.setFileName(InvoiceGlorityEnumd.getByCode(identificationDatum.k).getDesc() + "_" + multigraphOriginalImage.getFilename());
                            dataImageFilesInfoSm.setIurl(multigraphOriginalImage.getUrl());
                            dataImageFilesInfoSm.setFileMd5(multigraphOriginalImage.getETag());
                            dataImageFilesInfoSm.setSurl(multigraphSmallImage.getUrl());
                            dataImageFilesInfoSm.setFileSize(String.valueOf(bytes.length));
                            dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                            iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
                            updateFileId(entity, dataImageFilesInfoSm.getFileId());
                        }
                    }

                }

                //发票入库
                for (IdentificationData identificationDatum : identificationData) {
                    dataOcrService.ocrInsert(identificationDatum.k, identificationDatum.t);
                }

                //是否查验
                if (checkOff){
                    //发票查验
                    for (IdentificationData identificationDatum : identificationData) {
                        DataImageFilesInfo dataImageFilesInfoCheck = new DataImageFilesInfo();
                        Object entity = identificationDatum.t;
                        JSONObject jsonObject = JSONUtil.parseObj(cn.hutool.json.JSONUtil.toJsonStr(entity));
                        String CheckFileId = jsonObject.getStr("fileId");
                        dataImageFilesInfoCheck.setInvoice(identificationDatum.k);
                        dataImageFilesInfoCheck.setFileId(CheckFileId);
                        return checkInvoice.check(checkOff, dataImageFilesInfoCheck);
                    }
                }

            } else {
                return R.ok("文件类型不符合参数配置！");
            }

            return R.ok("操作成功!");

        } else {
            return handleOcrFailure(multipartFile, fileSuffix, InvoiceGlorityEnumd.REIMBURSABLE_OTHER_CODE.getCode(), FileStatusEnumd.UPLOADED_SUCCESSFUL_CODE.getCode(), "用户关闭了OCR！");  // 返回 null，表示跳过
        }

    }

    @Override
    public R<T> uploadAttachments(MultipartFile multipartFile) throws Exception {
        String originalFilename = multipartFile.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename) || !originalFilename.toLowerCase().endsWith(".pdf")) {
            return R.fail("只允许上传 pdf 文件");
        }
        SysOssVo upload = iSysOssService.upload(multipartFile);
//        OtherAttachments otherAttachments = new OtherAttachments();
//            otherAttachments.setUrl(upload.getUrl());
//            otherAttachments.setOriginalName(upload.getOriginalName());
//            otherAttachments.setFileSuffix(upload.getFileSuffix());
//            otherAttachments.setFileName(upload.getFileName());
        OtherAttachments otherAttachments = BeanUtil.copyProperties(upload, OtherAttachments.class);
        otherAttachmentsService.insert(otherAttachments);
        return R.ok("上传成功!");
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
     * @param entity 需要更新的主对象
     * @param fileId 要设置的 fileId 值
     * @throws Exception 如果反射操作失败
     */
    public static void updateFileId(Object entity, String fileId) throws Exception {

        if (entity == null || fileId == null) {
            throw new IllegalArgumentException("参数 entity 和 fileId 不能为空");
        }
        // 直接更新主对象的 fileId 字段（如果存在）
        Field fileIdField = getFieldIfExists(entity.getClass(), "fileId");
        if (fileIdField != null) {
            fileIdField.setAccessible(true);
            fileIdField.set(entity, fileId);
        }

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
            Field detailsField = getFieldIfExists(entity.getClass(), "details");
            if (detailsField != null) {
                detailsField.setAccessible(true);
                List<?> detailsList = (List<?>) detailsField.get(entity);
                if (detailsList != null) {
                    for (Object detail : detailsList) {
                        // 更新子对象的 fileId 字段
                        Field detailFileIdField = getFieldIfExists(detail.getClass(), "fileId");
                        if (detailFileIdField != null) {
                            detailFileIdField.setAccessible(true);
                            detailFileIdField.set(detail, fileId);
                        }
                    }
                }
            }
        }
    }

    /**
     * 检查类是否有指定字段
     */
    private static Field getFieldIfExists(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return null; // 不抛异常，直接返回 null
        }
    }

    /**
     * 检查文件类型是否符合参数配置
     *
     * @param fileSuffix 文件类型
     */
    public boolean isFileTypeAllowed(String fileSuffix) throws Exception {
        // 从 Redis 获取允许的文件类型
        String fileTypes = RedisUtils.getCacheMapValue(Constants.SYS_CONFIG_KEY, ParamConstants.SYS_INVOICE_TYPE);
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

    /**
     * 获取用户邮箱文件
     *
     */
    public List<MultipartFile> fetchFilesFromEmail() throws Exception {
        // 获取当前登录用户信息
        LoginUser sysUser = this.getLoginUserInfo();
        Long uid = sysUser.getUserId();
        if (ObjectUtils.isEmpty(uid)) {
            // 处理用户ID为空的情况
            return null;
        } else {
            // 获取用户的邮箱地址和授权码
            SysUserVo sysUserVo = iSysUserService.selectUserById(uid);
            String emailAddress = sysUserVo.getEmail();
            String emailPassword = sysUserVo.getEmailAuthorization();

            List<MultipartFile> multipartFileList = new ArrayList<>();
            // 提取域名
            String domain = emailAddress.substring(emailAddress.indexOf("@") + 1);
            // 获取邮件服务器参数
            Properties props = new Properties();
            props.setProperty("mail.store.protocol", "pop3");
            props.setProperty("mail.pop3.host", "pop." + domain);
            props.setProperty("mail.pop3.port", "995");
            props.setProperty("mail.pop3.ssl.enable", "true");
            props.setProperty("mail.pop3.connectiontimeout", "13000");
            // 登录邮箱
            Session session = Session.getDefaultInstance(props);
            Store store = session.getStore("pop3");
            store.connect(props.getProperty("mail.pop3.host"), emailAddress, emailPassword);

            // 打开收件箱
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // 搜索主题包含"发票"的邮件
            SearchTerm searchTerm = new SubjectTerm("发票");
            Message[] messages = inbox.search(searchTerm);

            for (Message message : messages) {
                // 检查邮件是否为多部分内容
                if (message.isMimeType("multipart/*")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                    int partCount = mimeMultipart.getCount();
                    for (int i = 0; i < partCount; i++) {
                        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                        // 检查是否为附件
                        if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                            String decodedFilename = MimeUtility.decodeText(bodyPart.getFileName());
                            String fileSuffix = getFileSuffix(decodedFilename);
                            try (InputStream is = bodyPart.getInputStream()) {
                                if ("zip".equalsIgnoreCase(fileSuffix)) {
                                    // 处理 ZIP 文件
                                    multipartFileList.addAll(extractFilesFromZip(is));
                                } else {
                                    // 处理其他类型的文件
                                    MultipartFile multipartFile = new MockMultipartFile(
                                        decodedFilename,
                                        decodedFilename,
                                        bodyPart.getContentType(),
                                        is
                                    );
                                    multipartFileList.add(multipartFile);
                                }
                            }
                        }
                    }
                }
            }

            // 关闭连接
            inbox.close(false);
            store.close();

            return multipartFileList;
        }
    }
    /**
     * 获取当前用户信息
     *
     */
    public LoginUser getLoginUserInfo() throws Exception {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authorization = request.getHeader("Authorization");
//                String authorization = request.getHeader("Token");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(authorization)) {
            authorization = authorization.replace("Bearer ", "");
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(authorization)) {
            SaSession session = (SaSession) RedisUtils.getCacheObject("global:Authorization:login:token-session" + ":" + authorization);
            if (session == null) {
                throw new IllegalArgumentException("Session not found for the given authorization token");
            }

            // 将 SaSession 序列化为 JSON 字符串
            String sessionJson = JSON.toJSONString(session);

            // 解析 JSON
            com.alibaba.fastjson.JSONObject sessionObject = JSON.parseObject(sessionJson);

            // 获取 dataMap 和 loginUser
            com.alibaba.fastjson.JSONObject dataMap = sessionObject.getJSONObject("dataMap");
            if (dataMap == null) {
                throw new IllegalStateException("DataMap is null in the session");
            }

            com.alibaba.fastjson.JSONObject loginUserJson = dataMap.getJSONObject("loginUser");
            if (loginUserJson == null) {
                throw new IllegalStateException("LoginUser is not present in the dataMap");
            }

            // 将 loginUserJson 转换为 LoginUser 对象
            LoginUser loginUser = loginUserJson.toJavaObject(LoginUser.class);
            return loginUser;
        }

        return null;
    }

    // 提取 ZIP 文件中的文件并转换为 MultipartFile
    private List<MultipartFile> extractFilesFromZip(InputStream zipInputStream) throws IOException {
        List<MultipartFile> multipartFiles = new ArrayList<>();

        // 创建临时文件存储 ZIP 内容
        File tempZipFile = File.createTempFile("temp", ".zip");
        try (FileOutputStream fos = new FileOutputStream(tempZipFile)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = zipInputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }

        // 使用 Apache Commons Compress 处理 ZIP 文件
        try (ZipFile zipFile = new ZipFile(tempZipFile, "GBK")) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            while (entries.hasMoreElements()) {
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    try (InputStream entryInputStream = zipFile.getInputStream(entry)) {
                        String fileName = entry.getName();
                        // 创建 MultipartFile 对象
                        MultipartFile multipartFile = new MockMultipartFile(
                            fileName,
                            fileName,
                            null,
                            entryInputStream
                        );
                        multipartFiles.add(multipartFile);
                    }
                }
            }
        } finally {
            // 删除临时文件
            if (tempZipFile.exists()) {
                tempZipFile.delete();
            }
        }

        return multipartFiles;
    }

    /*
     * 根据域名设置邮件服务器参数
     */
    public Properties getMailServerProperties(String domain) {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol", "pop3");
        switch (domain.toLowerCase()) {
            case "qq":
                props.setProperty("mail.pop3.host", "pop.qq.com");
                props.setProperty("mail.pop3.port", "995");
                break;
            case "163":
                props.setProperty("mail.pop3.host", "pop.163.com");
                props.setProperty("mail.pop3.port", "995");
                break;
            default:
                throw new IllegalArgumentException("不支持的邮箱服务提供商: " + domain);
        }
        props.setProperty("mail.pop3.ssl.enable", "true");
        props.setProperty("mail.pop3.connectiontimeout", "13000");
        return props;
    }

    public R<T> handleOcrFailure(MultipartFile multipartFile, String fileSuffix, String invoiceType, String status, String message) throws Exception{
        if (fileSuffix.equals("ofd")) {

            DataImageFilesInfo dataImageFilesInfoOfd = new DataImageFilesInfo();
            dataImageFilesInfoOfd.setFileId(IdUtil.simpleUUID());
            dataImageFilesInfoOfd.setMessage(message);
            dataImageFilesInfoOfd.setFileStatus(status);
            ByteArrayOutputStream outputStream = exportOfdToStream(multipartFile.getBytes(), "PNG", 20d);
            //切割图片压缩
            ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(outputStream.toByteArray(), "png");
            //缩略图
            ByteArrayOutputStream multigraphCutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
            //转换
            InputStream multigraphCutThumbnailImgFileStream = new ByteArrayInputStream(multigraphCutThumbnailImgFile.toByteArray());
            //源文件存储
            UploadResult multigraphOriginalImage = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfoOfd.getFileId() + "." + "ofd", multipartFile.getSize(), multipartFile.getContentType());
            //缩略图存储
            UploadResult multigraphSmallImage = OssFactory.instance().upload(multigraphCutThumbnailImgFileStream, "small_" + dataImageFilesInfoOfd.getFileId() + "." + "png", (long) multigraphCutThumbnailImgFile.size(), detectContentType(multigraphCutThumbnailImgFileStream));
            dataImageFilesInfoOfd.setInvoice(invoiceType);
            dataImageFilesInfoOfd.setFileName(multipartFile.getOriginalFilename());
            dataImageFilesInfoOfd.setIurl(multigraphOriginalImage.getUrl());
            dataImageFilesInfoOfd.setFileMd5(multigraphOriginalImage.getETag());
            dataImageFilesInfoOfd.setSurl(multigraphSmallImage.getUrl());
            dataImageFilesInfoOfd.setFileSize(String.valueOf(multipartFile.getSize()));
            dataImageFilesInfoOfd.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            iDataImageFilesInfoService.insert(dataImageFilesInfoOfd);

        } else if (fileSuffix.equals("pdf")) {//pdf类型

            SysOssVo pdfUpload = iSysOssService.upload(multipartFile);
            // 读取 PDF 文件并转换为字节数组
            List<byte[]> images = pdfToImg(multipartFile.getBytes());
            for (int identificationDatum = 0; identificationDatum < images.size(); identificationDatum++) {
                InputStream inputStream = new ByteArrayInputStream(images.get(identificationDatum));
                DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
                dataImageFilesInfoSm.setFileId(IdUtil.simpleUUID());
                dataImageFilesInfoSm.setMessage(message);
                ByteArrayOutputStream bytesArrayOutputStream = new ByteArrayOutputStream();
                bytesArrayOutputStream.write(images.get(identificationDatum));
                //切割图片压缩
                ByteArrayOutputStream cutThumbnailFile = FileUtils.thumbnailImage(images.get(identificationDatum), "JPG");
                //缩略图
                ByteArrayOutputStream multigraphCutThumbnailImgFile = FileUtils.thumbnailSmall(cutThumbnailFile.toByteArray());
                //转换
                InputStream multigraphCutThumbnailImgFileStream = new ByteArrayInputStream(multigraphCutThumbnailImgFile.toByteArray());
                //源文件存储
                UploadResult multigraphOriginalImage = OssFactory.instance().upload(inputStream, dataImageFilesInfoSm.getFileId() + "." + "jpg", (long) images.get(identificationDatum).length, "image/jpeg");
                //缩略图存储
                UploadResult multigraphSmallImage = OssFactory.instance().upload(multigraphCutThumbnailImgFileStream, "small_" + dataImageFilesInfoSm.getFileId() + "." + "jpg", (long) multigraphCutThumbnailImgFile.size(), detectContentType(multigraphCutThumbnailImgFileStream));
                dataImageFilesInfoSm.setInvoice(invoiceType);
                dataImageFilesInfoSm.setFileStatus(status);
                dataImageFilesInfoSm.setFileName(multipartFile.getOriginalFilename());
                dataImageFilesInfoSm.setIurl(multigraphOriginalImage.getUrl());
                dataImageFilesInfoSm.setPurl(pdfUpload.getUrl());
                dataImageFilesInfoSm.setFileMd5(multigraphOriginalImage.getETag());
                dataImageFilesInfoSm.setSurl(multigraphSmallImage.getUrl());
                dataImageFilesInfoSm.setFileSize(String.valueOf(images.get(identificationDatum).length));
                dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
                iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
            }

        } else if (fileSuffix.equals("xml")) {
            DataImageFilesInfo dataImageFilesInfoXml = new DataImageFilesInfo();
            dataImageFilesInfoXml.setFileId(IdUtil.simpleUUID());
            //源文件存储
            UploadResult multigraphOriginalXml = OssFactory.instance().upload(multipartFile.getInputStream(), dataImageFilesInfoXml.getFileId() + "." + "xml", multipartFile.getSize(), multipartFile.getContentType());
            dataImageFilesInfoXml.setInvoice(invoiceType);
            dataImageFilesInfoXml.setMessage(message);
            dataImageFilesInfoXml.setFileStatus(status);
            dataImageFilesInfoXml.setFileName(multipartFile.getOriginalFilename());
            dataImageFilesInfoXml.setIurl(multigraphOriginalXml.getUrl());
            dataImageFilesInfoXml.setFileMd5(multigraphOriginalXml.getETag());
            dataImageFilesInfoXml.setFileSize(String.valueOf(multipartFile.getSize()));
            dataImageFilesInfoXml.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            iDataImageFilesInfoService.insert(dataImageFilesInfoXml);
        } else {
            DataImageFilesInfo dataImageFilesInfo = new DataImageFilesInfo();
            dataImageFilesInfo.setFileId(IdUtil.simpleUUID());
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
            dataImageFilesInfo.setFileMd5(originalImage.getETag());
            dataImageFilesInfo.setFileName(originalImage.getFilename());
            dataImageFilesInfo.setInvoice(invoiceType);
            dataImageFilesInfo.setFileStatus(status);
            dataImageFilesInfo.setMessage(message);
            dataImageFilesInfo.setFileSize(String.valueOf(multipartFile.getSize()));
            dataImageFilesInfo.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
            iDataImageFilesInfoService.insert(dataImageFilesInfo);
        }

        return R.ok(message);
    }

    /**
     * 计算 MultipartFile 文件的 MD5 值
     *
     * @param file MultipartFile 文件
     * @return 文件的 MD5 哈希值
     * @throws IOException 读取文件异常
     */
    public static String getMd5(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return DigestUtils.md5Hex(inputStream);
        }
    }

    @Override
    public void lhdxImportInvoice(List<LhdxInvoiceVo> list,String mergedFilePath) {
        InvoiceGeneratorXEasyPdf invoiceGeneratorXEasyPdf = new InvoiceGeneratorXEasyPdf();

        Map<String, List<LhdxInvoiceVo>> collect = list.stream().collect(Collectors.groupingBy(LhdxInvoiceVo::getInvoiceNumberCode));
        for (String key : collect.keySet()) {
            List<LhdxInvoiceVo> lhdxInvoiceVoList = collect.get(key);
            Invoice sampleInvoice = InvoiceGeneratorXEasyPdf.createSampleInvoice(lhdxInvoiceVoList);

            String labelName = lhdxInvoiceVoList.get(0).getInvoiceType();
            String invoiceType = labelName.contains("增值税") ? "10100" : "10108";//发票类型

            UUID uuid = UUID.randomUUID();
            String outputPdfPath = mergedFilePath + uuid + ".pdf";
            int pageNum = lhdxInvoiceVoList.size() / 24 + 1;
            InvoiceGeneratorXEasyPdf.generateInvoice(sampleInvoice,outputPdfPath,pageNum,invoiceType);

            DataImageFilesInfo dataImageFilesInfoSm = dataImageFilesInsert(outputPdfPath, invoiceType);//插入发票文件

            ocrInsert(lhdxInvoiceVoList, dataImageFilesInfoSm, invoiceType);//插入发票信息
        }
    }

    public DataImageFilesInfo dataImageFilesInsert(String output, String invoiceType){
        SysOssVo pdfUpload = iSysOssService.upload(new File(output));
        DataImageFilesInfo dataImageFilesInfoSm = new DataImageFilesInfo();
        dataImageFilesInfoSm.setInvoice(invoiceType);
        dataImageFilesInfoSm.setFileStatus(FileStatusEnumd.UPLOADED_SUCCESSFUL_CODE.getCode());
        dataImageFilesInfoSm.setFileName(pdfUpload.getFileName());
        dataImageFilesInfoSm.setPurl(pdfUpload.getUrl());
        dataImageFilesInfoSm.setSurl(pdfUpload.getUrl());
        dataImageFilesInfoSm.setCheckStatus(CheckInvoiceStatusEnumd.TO_BE_VERIFIED_CODE.getCode());
        iDataImageFilesInfoService.insert(dataImageFilesInfoSm);
        return dataImageFilesInfoSm;
    }

    private void ocrInsert(List<LhdxInvoiceVo> lhdxInvoiceVoList, DataImageFilesInfo dataImageFilesInfoSm, String invoiceType) {
        try{
            DataOcrInfo dataOcrInfo = setDataOcrInfo(lhdxInvoiceVoList, dataImageFilesInfoSm.getFileId());
            dataOcrService.ocrInsert(invoiceType, dataOcrInfo);
        }catch (Exception e){
            log.error("处理发票信息时出错",e);
        }
    }

    public Map<String, Object> setMap(List<LhdxInvoiceVo> list) {
        Map<String, Object> replacements = new HashMap<>();
        LhdxInvoiceVo lhdxInvoiceVo = list.get(0);
        String labelName = lhdxInvoiceVo.getInvoiceType();
        String invoiceNumberCode = lhdxInvoiceVo.getInvoiceNumberCode();
        String invoiceDate = lhdxInvoiceVo.getInvoiceDate();
        String buyerName = lhdxInvoiceVo.getBuyerName();
        String buyerTaxId = lhdxInvoiceVo.getBuyerTaxId();
        String sellerName = lhdxInvoiceVo.getSellerName();
        String sellerTaxId = lhdxInvoiceVo.getSellerTaxId();
        String drawer = lhdxInvoiceVo.getDrawer();//开票人
        String remarks = lhdxInvoiceVo.getRemarks();//备注
        String totalAmount = lhdxInvoiceVo.getTotalAmount();//价税合计

        try{
            for (LhdxInvoiceVo vo : list) {
                String invoiceDetailLine = vo.getInvoiceDetailLine();//行号
                String invoiceItemName = vo.getInvoiceItemName();//项目名称
                String unitPrice = vo.getUnitPrice();//单价金额
                String taxRate = vo.getTaxRate();//税率
                String taxAmount = vo.getTaxAmount();//税额

                replacements.put("ItemName"+invoiceDetailLine, invoiceItemName);
                replacements.put("UnPrice"+invoiceDetailLine, unitPrice);
                replacements.put("Amount"+invoiceDetailLine, unitPrice);
                if (taxRate == null) taxRate = "*";
                if (taxRate.equals("*")) {
                    replacements.put("TaxRate"+invoiceDetailLine, taxRate);
                }else if(taxRate.contains("%")){
                    replacements.put("TaxRate"+invoiceDetailLine, taxRate);
                }else{
                    replacements.put("TaxRate"+invoiceDetailLine, Double.parseDouble(taxRate) * 100 + "%" );
                }
                replacements.put("ComTaxAm"+invoiceDetailLine, taxAmount);
            }
            replacements.put("InvoiceNumber", invoiceNumberCode);
            replacements.put("IssueTime", invoiceDate);
            replacements.put("BuyerName", buyerName);
            replacements.put("BuyerIdNum", buyerTaxId);
            replacements.put("SellerName", sellerName);
            replacements.put("SellerIdNum", sellerTaxId);
            replacements.put("totalAmount", totalAmount);
            String cleanNumber = totalAmount.replace(",", "").trim();
            BigDecimal bigDecimal = new BigDecimal(cleanNumber);
            String amountInChinese = MoneyToChineseUtil.convert(bigDecimal);
            replacements.put("amountInChinese", amountInChinese);
            replacements.put("Remark", remarks);
            replacements.put("Drawer", drawer);
        } catch (Exception e) {
            log.error("lhdxImportInvoice error:{}", e);
        }

        return replacements;
    }


    public DataOcrInfo setDataOcrInfo(List<LhdxInvoiceVo> list,String fileId) {
        DataOcrInfo dataOcrInfo = new DataOcrInfo();
        LhdxInvoiceVo invoiceVo = list.get(0);
        dataOcrInfo.setFileId(fileId);
        dataOcrInfo.setInvoiceDate(invoiceVo.getInvoiceDate());//发票日期
        dataOcrInfo.setInvoiceNumber(invoiceVo.getInvoiceNumber());//发票号码
        dataOcrInfo.setInvoiceCode(invoiceVo.getInvoiceCode());//发票代码
        dataOcrInfo.setBuyerName(invoiceVo.getBuyerName());//购买方名称
        dataOcrInfo.setBuyerNo(invoiceVo.getBuyerTaxId());//购买方纳税识别号
        dataOcrInfo.setSellerName(invoiceVo.getSellerName());//销售方名称
        dataOcrInfo.setSellerNo(invoiceVo.getSellerTaxId());//销售方纳税识别号
        dataOcrInfo.setPayee(invoiceVo.getPayee());//收款人
        dataOcrInfo.setIssuer(invoiceVo.getDrawer());//开票人
        dataOcrInfo.setRemark(invoiceVo.getRemarks());//备注

        List<DataOcrDetails> details = new ArrayList<>();
        for (LhdxInvoiceVo lhdxInvoiceVo : list) {
            DataOcrDetails dataOcrDetails = new DataOcrDetails();
            dataOcrDetails.setFileId(fileId);
            dataOcrDetails.setDetailNo(lhdxInvoiceVo.getInvoiceDetailLine());//发票明细行
            dataOcrDetails.setProjectName(lhdxInvoiceVo.getInvoiceItemName());
            dataOcrDetails.setPrice(lhdxInvoiceVo.getUnitPrice());
            dataOcrDetails.setTaxRate(lhdxInvoiceVo.getTaxRate());
            dataOcrDetails.setTax(lhdxInvoiceVo.getTaxAmount());
            dataOcrDetails.setDetailAmount(lhdxInvoiceVo.getTotalAmount());
            details.add(dataOcrDetails);
        }
        dataOcrInfo.setDetails(details);
        return dataOcrInfo;
    }

}
