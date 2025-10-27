package org.smartlink.business.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.smartlink.business.listener.UserImportListener;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.excel.core.ExcelResult;
import org.smartlink.common.excel.utils.ExcelUtil;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.system.domain.vo.SysUserImportVo;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/info")
public class UserInvoiceController extends BaseController {

    /**
     * 初始化用户发票数据
     *
     * @param file          导入文件
     * @param updateSupport 是否更新已存在数据
     */
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @SaCheckPermission("system:user:import")
    @PostMapping(value = "/importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<Void> importData(@RequestPart("file") MultipartFile file, boolean updateSupport) throws Exception {
        ExcelResult<SysUserImportVo> result = ExcelUtil.importExcel(file.getInputStream(), SysUserImportVo.class, new UserImportListener(updateSupport));
        return R.ok(result.getAnalysis());
    }
}
