package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsImportGoodsDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsImportGoodsDetailVo;
import org.smartlink.common.entity.domain.business.service.IDataCustomsImportGoodsDetailService;
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
 * 海关进口货物明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/customsImportGoodsDetail")
public class DataCustomsImportGoodsDetailController extends BaseController {

    private final IDataCustomsImportGoodsDetailService dataCustomsImportGoodsDetailService;

    /**
     * 查询海关进口货物明细列表
     */
    @SaCheckPermission("business:customsImportGoodsDetail:list")
    @GetMapping("/list")
    public TableDataInfo<DataCustomsImportGoodsDetailVo> list(DataCustomsImportGoodsDetailBo bo, PageQuery pageQuery) {
        return dataCustomsImportGoodsDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出海关进口货物明细列表
     */
    @SaCheckPermission("business:customsImportGoodsDetail:export")
    @Log(title = "海关进口货物明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataCustomsImportGoodsDetailBo bo, HttpServletResponse response) {
        List<DataCustomsImportGoodsDetailVo> list = dataCustomsImportGoodsDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "海关进口货物明细", DataCustomsImportGoodsDetailVo.class, response);
    }

    /**
     * 获取海关进口货物明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:customsImportGoodsDetail:query")
    @GetMapping("/{id}")
    public R<DataCustomsImportGoodsDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataCustomsImportGoodsDetailService.queryById(id));
    }

    /**
     * 新增海关进口货物明细
     */
    @SaCheckPermission("business:customsImportGoodsDetail:add")
    @Log(title = "海关进口货物明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataCustomsImportGoodsDetailBo bo) {
        return toAjax(dataCustomsImportGoodsDetailService.insertByBo(bo));
    }

    /**
     * 修改海关进口货物明细
     */
    @SaCheckPermission("business:customsImportGoodsDetail:edit")
    @Log(title = "海关进口货物明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataCustomsImportGoodsDetailBo bo) {
        return toAjax(dataCustomsImportGoodsDetailService.updateByBo(bo));
    }

    /**
     * 删除海关进口货物明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:customsImportGoodsDetail:remove")
    @Log(title = "海关进口货物明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataCustomsImportGoodsDetailService.deleteWithValidByIds(List.of(ids), true));
    }
}
