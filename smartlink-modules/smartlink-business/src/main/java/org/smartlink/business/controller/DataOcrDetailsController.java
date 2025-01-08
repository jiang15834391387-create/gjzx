package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
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
import org.smartlink.business.domain.vo.DataOcrDetailsVo;
import org.smartlink.business.domain.bo.DataOcrDetailsBo;
import org.smartlink.business.service.IDataOcrDetailsService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * ocr明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/ocrDetails")
public class DataOcrDetailsController extends BaseController {

    private final IDataOcrDetailsService dataOcrDetailsService;

    /**
     * 查询ocr明细列表
     */
    @SaCheckPermission("business:ocrDetails:list")
    @GetMapping("/list")
    public TableDataInfo<DataOcrDetailsVo> list(DataOcrDetailsBo bo, PageQuery pageQuery) {
        return dataOcrDetailsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出ocr明细列表
     */
    @SaCheckPermission("business:ocrDetails:export")
    @Log(title = "ocr明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataOcrDetailsBo bo, HttpServletResponse response) {
        List<DataOcrDetailsVo> list = dataOcrDetailsService.queryList(bo);
        ExcelUtil.exportExcel(list, "ocr明细", DataOcrDetailsVo.class, response);
    }

    /**
     * 获取ocr明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:ocrDetails:query")
    @GetMapping("/{id}")
    public R<DataOcrDetailsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataOcrDetailsService.queryById(id));
    }

    /**
     * 新增ocr明细
     */
    @SaCheckPermission("business:ocrDetails:add")
    @Log(title = "ocr明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataOcrDetailsBo bo) {
        return toAjax(dataOcrDetailsService.insertByBo(bo));
    }

    /**
     * 修改ocr明细
     */
    @SaCheckPermission("business:ocrDetails:edit")
    @Log(title = "ocr明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataOcrDetailsBo bo) {
        return toAjax(dataOcrDetailsService.updateByBo(bo));
    }

    /**
     * 删除ocr明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:ocrDetails:remove")
    @Log(title = "ocr明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataOcrDetailsService.deleteWithValidByIds(List.of(ids), true));
    }
}
