package org.smartlink.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.undertow.server.handlers.form.FormData;
import jakarta.mail.BodyPart;
import jakarta.mail.Multipart;
import jakarta.mail.internet.MimeMultipart;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.formula.functions.T;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.utils.file.FileUtils;
import org.smartlink.common.log.annotation.Log;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.smartlink.common.log.enums.BusinessType;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.hutool.http.HttpUtil.downloadFileFromUrl;

/**
 * 发票识别
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/InvoiceRecognition")
public class InvoiceRecognitionController {

    private final ScanImageService scanImageService;

    //@SaCheckPermission("service:scan:upload")
    /**
     * 发票上传
     *
     * @param file 文件对象
     * @param uploadType 上传类型 0 邮件 1 手动
     */
    @Log(title = "发票上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public R<T> upload(@RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam(value = "uploadType") String uploadType) throws Exception {
        R dataImageFilesInfo = null;
            if (uploadType.equals("0")){
                List<MultipartFile> fetchFilesFromEmail = scanImageService.fetchFilesFromEmail();
                if (fetchFilesFromEmail.size()!= 0){
                    for (MultipartFile multipart : fetchFilesFromEmail) {
                        dataImageFilesInfo = scanImageService.uploadImage(multipart, uploadType);
                    }
                }
            } else {
                    dataImageFilesInfo = scanImageService.uploadImage(file, uploadType);
            }

        return dataImageFilesInfo;
    }

}
