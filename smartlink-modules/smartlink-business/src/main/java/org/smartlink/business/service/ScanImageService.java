package org.smartlink.business.service;



import jakarta.mail.internet.MimeMultipart;
import org.apache.poi.ss.formula.functions.T;
import org.smartlink.common.core.domain.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author shidunkai
 * @title 文件上传
 * @description 文件上传
 * @date 2022-04
 */
public interface ScanImageService {
    /**
     * 文件上传
     *
     * @param multipartFile 文件
     * @return 图片vo
     */
    R<T> uploadImage(MultipartFile multipartFile, String uploadType) throws Exception;

    /**
     * 文件上传
     *
     * @param multipartFile 文件
     * @return 图片vo
     */
    R<T> uploadAttachments(MultipartFile multipartFile) throws Exception;

    /**
     * 邮件识别
     *
     * @return 邮件文件
     */
    List<MultipartFile> fetchFilesFromEmail() throws Exception;
//
//    /**
//     * 单据初始化
//     *
//     * @param request ...
//     * @return ...
//     */
//    InitializationResponse scanTypeOne(InitializationRequest request);
//
//    /**
//     * 获取批扫基础业务数据
//     * @return
//     */
//    BatchInitializationResponse getBatchInitData();
//
//    /**
//     * 批扫新建任务
//     * @return
//     */
//    R<Void> batchNewTask();
//
//    /**
//     * 文件上传
//     *
//     * @param fileUploadDTO 文件信息
//     * @param dataImageFilesInfo 图片文件对象
//     * @param file 文件
//     * @return 图片vo
//     */
//    DataImageFilesInfoVo saveDocuments(FileUploadDTO fileUploadDTO, DataImageFilesInfo dataImageFilesInfo, MultipartFile file) throws Exception;

}
