package org.smartlink.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.smartlink.business.scan.dto.FileUploadDTO;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.log.annotation.Log;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.smartlink.common.log.enums.BusinessType;

import java.io.File;

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
    public R upload(@NotNull(message = "上传文件不能为空") @RequestParam("file") MultipartFile file) throws Exception {
        R dataImageFilesInfoVo = scanImageService.uploadImage(file);
        return R.ok(dataImageFilesInfoVo);
    }


}
