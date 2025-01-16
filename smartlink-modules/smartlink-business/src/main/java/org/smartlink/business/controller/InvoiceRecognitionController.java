package org.smartlink.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.undertow.server.handlers.form.FormData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.formula.functions.T;
import org.smartlink.business.scan.dto.FileUploadDTO;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.utils.file.FileUtils;
import org.smartlink.common.log.annotation.Log;
import org.springframework.mock.web.MockMultipartFile;
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
    @Log(title = "扫描图片上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public R<T> upload(@RequestParam(value = "file", required = false) MultipartFile file,
                       @RequestParam(value = "fileUrl", required = false) String fileUrl) throws Exception {
        R<T> dataImageFilesInfo = null;
        // 参数校验：确保至少提供文件或参数
        if (file == null && (fileUrl == null || fileUrl.isEmpty())) {
            return R.ok("上传文件和文件地址参数不能同时为空");
        }
        if (!fileUrl.isEmpty()){
            String fileUrlMatche = FileUtils.extractFileUrlFromEmail(fileUrl);
            if (fileUrl == null) {
                return R.fail("无法从邮件内容中提取有效的文件链接");
            }
            // 下载文件并转换为 MultipartFile
            MultipartFile multipartFile = FileUtils.downloadFileFromUrl(fileUrlMatche);
            dataImageFilesInfo = scanImageService.uploadImage(multipartFile);
        }
        if (!file.isEmpty()){
            dataImageFilesInfo = scanImageService.uploadImage(file);
        }

        return dataImageFilesInfo;
    }

}
