package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataImageFilesInfoVo;
import org.smartlink.common.entity.domain.business.service.IDataImageFilesInfoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.smartlink.common.idempotent.annotation.RepeatSubmit;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.excel.utils.ExcelUtil;

import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 图片文件
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/imageFilesInfo")
public class DataImageFilesInfoController extends BaseController {

    private final IDataImageFilesInfoService dataImageFilesInfoService;

    /**
     * 查询图片文件列表
     */
    @SaCheckPermission("business:imageFilesInfo:list")
    @GetMapping("/list")
    public TableDataInfo<DataImageFilesInfoVo> list(DataImageFilesInfoBo bo, PageQuery pageQuery) {
        return dataImageFilesInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出图片文件列表
     */
    @SaCheckPermission("business:imageFilesInfo:export")
    @Log(title = "图片文件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataImageFilesInfoBo bo, HttpServletResponse response) {
        List<DataImageFilesInfoVo> list = dataImageFilesInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "图片文件", DataImageFilesInfoVo.class, response);
    }

    /**
     * 获取图片文件详细信息
     *
     * @param fileId 主键
     */
    @SaCheckPermission("business:imageFilesInfo:query")
    @GetMapping("/{fileId}")
    public R<DataImageFilesInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String fileId) {
        return R.ok(dataImageFilesInfoService.queryById(fileId));
    }

    /**
     * 新增图片文件
     */
    @SaCheckPermission("business:imageFilesInfo:add")
    @Log(title = "图片文件", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataImageFilesInfoBo bo) {
        return toAjax(dataImageFilesInfoService.insertByBo(bo));
    }

    /**
     * 修改图片文件
     */
    @SaCheckPermission("business:imageFilesInfo:edit")
    @Log(title = "图片文件", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataImageFilesInfoBo bo) {
        return toAjax(dataImageFilesInfoService.updateByBo(bo));
    }

    /**
     * 删除图片文件
     *
     * @param fileIds 主键串
     */
    @SaCheckPermission("business:imageFilesInfo:remove")
    @Log(title = "图片文件", businessType = BusinessType.DELETE)
    @DeleteMapping("/{fileIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] fileIds) {
        return toAjax(dataImageFilesInfoService.deleteWithValidByIds(List.of(fileIds), true));
    }
}
