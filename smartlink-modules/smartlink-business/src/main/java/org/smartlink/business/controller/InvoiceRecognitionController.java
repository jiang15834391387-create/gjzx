package org.smartlink.business.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.smartlink.business.doman.vo.LhdxInvoiceVo;
import org.smartlink.business.listener.LhdxInvoiceImportListener;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.enums.FileStatusEnumd;
import org.smartlink.common.entity.domain.business.domain.OtherAttachments;
import org.smartlink.common.entity.domain.business.service.IOtherAttachmentsService;
import org.smartlink.common.excel.core.ExcelResult;
import org.smartlink.common.excel.utils.ExcelUtil;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@SaIgnore
@RequestMapping("/business/InvoiceRecognition")
public class InvoiceRecognitionController {

    private final ScanImageService scanImageService;
    private final IOtherAttachmentsService iOtherAttachmentsService;

    @Value("${file-path.merged}")
    private String mergedFilePath;
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
            if (!fetchFilesFromEmail.isEmpty()){
                for (MultipartFile multipart : fetchFilesFromEmail) {
                    res = scanImageService.uploadImage(multipart, uploadType);
                }
            }
        } else {
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    res = scanImageService.uploadImage(file, uploadType);
                    if (res.getMsg().equals(FileStatusEnumd.OCR_FAILED.getDesc())) {
                        continue;  // 跳过当前文件，继续处理下一个
                    }
                }
            } else {
                return R.fail("请上传文件!");
            }
        }

        return res;
    }

    /**
     * 发票上传
     *
     * @param files 多文件对象
     */
    @SaIgnore
    @Log(title = "其他附件上传", businessType = BusinessType.INSERT)
    @PostMapping("/uploadAttachments")
    public R<T> uploadAttachments(@RequestParam(value = "files", required = false) MultipartFile[] files) throws Exception {
        if (files == null || files.length == 0) {
            return R.fail("请上传文件!");
        }
        R<T> res = null;
                for (MultipartFile multipart : files) {
                    res = scanImageService.uploadAttachments(multipart);
                }
        return res;
    }

    /**
     * 查询其他附件
     */
    @SaIgnore
    @GetMapping("/otherAttachmentsList")
    public List<OtherAttachments> otherAttachmentsList(OtherAttachments otherAttachments) {
        return iOtherAttachmentsService.queryList(otherAttachments);
    }

    /**
     * 联合大学发票导入
     */
    @Log(title = "联合大学发票导入", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/lhdxImportInvoice", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> lhdxImportInvoice(@RequestPart("file") MultipartFile file) throws Exception {
        ExcelResult<LhdxInvoiceVo> result = ExcelUtil.importExcel(file.getInputStream(), LhdxInvoiceVo.class, new LhdxInvoiceImportListener());
        if (!result.getList().isEmpty()){
            scanImageService.lhdxImportInvoice(result.getList(),mergedFilePath);
            return R.ok("发票数据导入任务已开始，请稍后查询进度。");
        }else{
            return R.ok("表格中所有发票均已入库！");
        }
    }

}
