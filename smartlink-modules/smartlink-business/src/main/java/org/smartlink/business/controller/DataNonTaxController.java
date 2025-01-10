package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataNonTaxBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataNonTaxVo;
import org.smartlink.common.entity.domain.business.service.IDataNonTaxService;
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
 * 非税收入类票据
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/nonTax")
public class DataNonTaxController extends BaseController {

    private final IDataNonTaxService dataNonTaxService;

    /**
     * 查询非税收入类票据列表
     */
    @SaCheckPermission("business:nonTax:list")
    @GetMapping("/list")
    public TableDataInfo<DataNonTaxVo> list(DataNonTaxBo bo, PageQuery pageQuery) {
        return dataNonTaxService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出非税收入类票据列表
     */
    @SaCheckPermission("business:nonTax:export")
    @Log(title = "非税收入类票据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataNonTaxBo bo, HttpServletResponse response) {
        List<DataNonTaxVo> list = dataNonTaxService.queryList(bo);
        ExcelUtil.exportExcel(list, "非税收入类票据", DataNonTaxVo.class, response);
    }

    /**
     * 获取非税收入类票据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:nonTax:query")
    @GetMapping("/{id}")
    public R<DataNonTaxVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataNonTaxService.queryById(id));
    }

    /**
     * 新增非税收入类票据
     */
    @SaCheckPermission("business:nonTax:add")
    @Log(title = "非税收入类票据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataNonTaxBo bo) {
        return toAjax(dataNonTaxService.insertByBo(bo));
    }

    /**
     * 修改非税收入类票据
     */
    @SaCheckPermission("business:nonTax:edit")
    @Log(title = "非税收入类票据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataNonTaxBo bo) {
        return toAjax(dataNonTaxService.updateByBo(bo));
    }

    /**
     * 删除非税收入类票据
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:nonTax:remove")
    @Log(title = "非税收入类票据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataNonTaxService.deleteWithValidByIds(List.of(ids), true));
    }
}
