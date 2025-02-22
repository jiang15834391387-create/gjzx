package org.smartlink.business.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.poi.ss.formula.functions.T;
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

import java.util.List;

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

    /**
     * 发票上传
     *
     * @param files 多文件对象
     * @param uploadType 上传类型 0 邮件 1 手动
     */
    @Log(title = "发票上传", businessType = BusinessType.INSERT)
    @PostMapping("/upload")
    public R<T> upload(@RequestParam(value = "files", required = false) MultipartFile[] files,
                       @RequestParam(value = "uploadType") String uploadType) throws Exception {
        R<T> res = null;
            if (uploadType.equals("0")){
                List<MultipartFile> fetchFilesFromEmail = scanImageService.fetchFilesFromEmail();
                if (fetchFilesFromEmail.size()!= 0){
                    for (MultipartFile multipart : fetchFilesFromEmail) {
                        res = scanImageService.uploadImage(multipart, uploadType);
                    }
                }
            } else {
                if (files != null && files.length > 0) {
                    for (MultipartFile file : files) {
                        res = scanImageService.uploadImage(file, uploadType);
                        if (res.getMsg().equals("OCR识别为空")) {
                            continue;  // 跳过当前文件，继续处理下一个
                        }
                    }
                } else {
                    return R.fail("请上传文件!");
                }
            }

        return res;
    }

}
