package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrInfoVo;
import org.smartlink.common.entity.domain.business.service.IDataOcrInfoService;
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
 * 增值税发票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/ocrInfo")
public class DataOcrInfoController extends BaseController {

    private final IDataOcrInfoService dataOcrInfoService;

    /**
     * 查询增值税发票列表
     */
    @SaCheckPermission("business:ocrInfo:list")
    @GetMapping("/list")
    public TableDataInfo<DataOcrInfoVo> list(DataOcrInfoBo bo, PageQuery pageQuery) {
        return dataOcrInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出增值税发票列表
     */
    @SaCheckPermission("business:ocrInfo:export")
    @Log(title = "增值税发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataOcrInfoBo bo, HttpServletResponse response) {
        List<DataOcrInfoVo> list = dataOcrInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "增值税发票", DataOcrInfoVo.class, response);
    }

    /**
     * 获取增值税发票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:ocrInfo:query")
    @GetMapping("/{id}")
    public R<DataOcrInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataOcrInfoService.queryById(id));
    }

    /**
     * 新增增值税发票
     */
    @SaCheckPermission("business:ocrInfo:add")
    @Log(title = "增值税发票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataOcrInfoBo bo) {
        return toAjax(dataOcrInfoService.insertByBo(bo));
    }

    /**
     * 修改增值税发票
     */
    @SaCheckPermission("business:ocrInfo:edit")
    @Log(title = "增值税发票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataOcrInfoBo bo) {
        return toAjax(dataOcrInfoService.updateByBo(bo));
    }

    /**
     * 删除增值税发票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:ocrInfo:remove")
    @Log(title = "增值税发票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataOcrInfoService.deleteWithValidByIds(List.of(ids), true));
    }
}
