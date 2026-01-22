package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsExportGoodsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsExportGoodsVo;
import org.smartlink.common.entity.domain.business.service.IDataCustomsExportGoodsService;
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
 * 海关出口货物
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/customsExportGoods")
public class DataCustomsExportGoodsController extends BaseController {

    private final IDataCustomsExportGoodsService dataCustomsExportGoodsService;

    /**
     * 查询海关出口货物列表
     */
    @SaCheckPermission("business:customsExportGoods:list")
    @GetMapping("/list")
    public TableDataInfo<DataCustomsExportGoodsVo> list(DataCustomsExportGoodsBo bo, PageQuery pageQuery) {
        return dataCustomsExportGoodsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出海关出口货物列表
     */
    @SaCheckPermission("business:customsExportGoods:export")
    @Log(title = "海关出口货物", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataCustomsExportGoodsBo bo, HttpServletResponse response) {
        List<DataCustomsExportGoodsVo> list = dataCustomsExportGoodsService.queryList(bo);
        ExcelUtil.exportExcel(list, "海关出口货物", DataCustomsExportGoodsVo.class, response);
    }

    /**
     * 获取海关出口货物详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:customsExportGoods:query")
    @GetMapping("/{id}")
    public R<DataCustomsExportGoodsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataCustomsExportGoodsService.queryById(id));
    }

    /**
     * 新增海关出口货物
     */
    @SaCheckPermission("business:customsExportGoods:add")
    @Log(title = "海关出口货物", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataCustomsExportGoodsBo bo) {
        return toAjax(dataCustomsExportGoodsService.insertByBo(bo));
    }

    /**
     * 修改海关出口货物
     */
    @SaCheckPermission("business:customsExportGoods:edit")
    @Log(title = "海关出口货物", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataCustomsExportGoodsBo bo) {
        return toAjax(dataCustomsExportGoodsService.updateByBo(bo));
    }

    /**
     * 删除海关出口货物
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:customsExportGoods:remove")
    @Log(title = "海关出口货物", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataCustomsExportGoodsService.deleteWithValidByIds(List.of(ids), true));
    }
}
