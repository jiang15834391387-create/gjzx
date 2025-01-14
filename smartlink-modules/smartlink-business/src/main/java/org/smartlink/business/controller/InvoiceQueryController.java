package org.smartlink.business.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.service.IInvoiceInfoService;
import org.smartlink.business.service.ScanImageService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.bo.DataUsedCarSalesBo;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 发票识别
 *
 * @author Lion Li
 * @date 2025-01-012
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/InvoiceInfo")
public class InvoiceQueryController {

        private final IInvoiceInfoService iInvoiceInfoService;

        //@SaCheckPermission("service:scan:upload")
        @Log(title = "查询到图片内容+发票信息", businessType = BusinessType.INSERT)
        @PostMapping("/queryInvoiceInfo")
        public R queryList(@RequestBody DataImageFilesInfoBo dataImageFilesInfo) throws Exception {
            R dataImageFilesInfoVo = iInvoiceInfoService.queryList(dataImageFilesInfo);
//            return R.ok(dataImageFilesInfoVo);
            return dataImageFilesInfoVo;
        }

    //@SaCheckPermission("service:scan:upload")
    @Log(title = "查询到图片内容+发票信息", businessType = BusinessType.INSERT)
    @GetMapping("/queryOneInfo")
    public R queryOneInfo(@RequestParam(value = "fileId") String fileId) throws Exception {
            if (fileId.isEmpty()){
                return R.ok("fileId不能为空!");
            }
        R res = iInvoiceInfoService.queryOneInfo(fileId);
        return res;
    }

}
