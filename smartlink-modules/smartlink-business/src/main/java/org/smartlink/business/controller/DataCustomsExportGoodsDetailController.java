package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsExportGoodsDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsExportGoodsDetailVo;
import org.smartlink.common.entity.domain.business.service.IDataCustomsExportGoodsDetailService;
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
 * 海关出口货物明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/customsExportGoodsDetail")
public class DataCustomsExportGoodsDetailController extends BaseController {

    private final IDataCustomsExportGoodsDetailService dataCustomsExportGoodsDetailService;

    /**
     * 查询海关出口货物明细列表
     */
    @SaCheckPermission("business:customsExportGoodsDetail:list")
    @GetMapping("/list")
    public TableDataInfo<DataCustomsExportGoodsDetailVo> list(DataCustomsExportGoodsDetailBo bo, PageQuery pageQuery) {
        return dataCustomsExportGoodsDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出海关出口货物明细列表
     */
    @SaCheckPermission("business:customsExportGoodsDetail:export")
    @Log(title = "海关出口货物明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataCustomsExportGoodsDetailBo bo, HttpServletResponse response) {
        List<DataCustomsExportGoodsDetailVo> list = dataCustomsExportGoodsDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "海关出口货物明细", DataCustomsExportGoodsDetailVo.class, response);
    }

    /**
     * 获取海关出口货物明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:customsExportGoodsDetail:query")
    @GetMapping("/{id}")
    public R<DataCustomsExportGoodsDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataCustomsExportGoodsDetailService.queryById(id));
    }

    /**
     * 新增海关出口货物明细
     */
    @SaCheckPermission("business:customsExportGoodsDetail:add")
    @Log(title = "海关出口货物明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataCustomsExportGoodsDetailBo bo) {
        return toAjax(dataCustomsExportGoodsDetailService.insertByBo(bo));
    }

    /**
     * 修改海关出口货物明细
     */
    @SaCheckPermission("business:customsExportGoodsDetail:edit")
    @Log(title = "海关出口货物明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataCustomsExportGoodsDetailBo bo) {
        return toAjax(dataCustomsExportGoodsDetailService.updateByBo(bo));
    }

    /**
     * 删除海关出口货物明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:customsExportGoodsDetail:remove")
    @Log(title = "海关出口货物明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataCustomsExportGoodsDetailService.deleteWithValidByIds(List.of(ids), true));
    }
}
