package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataReceiptBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataReceiptVo;
import org.smartlink.common.entity.domain.business.service.IDataReceiptService;
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
 * 小票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/receipt")
public class DataReceiptController extends BaseController {

    private final IDataReceiptService dataReceiptService;

    /**
     * 查询小票列表
     */
    @SaCheckPermission("business:receipt:list")
    @GetMapping("/list")
    public TableDataInfo<DataReceiptVo> list(DataReceiptBo bo, PageQuery pageQuery) {
        return dataReceiptService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出小票列表
     */
    @SaCheckPermission("business:receipt:export")
    @Log(title = "小票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataReceiptBo bo, HttpServletResponse response) {
        List<DataReceiptVo> list = dataReceiptService.queryList(bo);
        ExcelUtil.exportExcel(list, "小票", DataReceiptVo.class, response);
    }

    /**
     * 获取小票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:receipt:query")
    @GetMapping("/{id}")
    public R<DataReceiptVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataReceiptService.queryById(id));
    }

    /**
     * 新增小票
     */
    @SaCheckPermission("business:receipt:add")
    @Log(title = "小票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataReceiptBo bo) {
        return toAjax(dataReceiptService.insertByBo(bo));
    }

    /**
     * 修改小票
     */
    @SaCheckPermission("business:receipt:edit")
    @Log(title = "小票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataReceiptBo bo) {
        return toAjax(dataReceiptService.updateByBo(bo));
    }

    /**
     * 删除小票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:receipt:remove")
    @Log(title = "小票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataReceiptService.deleteWithValidByIds(List.of(ids), true));
    }
}
