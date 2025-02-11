package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsImxportGoodsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsImxportGoodsVo;
import org.smartlink.common.entity.domain.business.service.IDataCustomsImxportGoodsService;
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
 * 海关进口货物报关单
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/customsImxportGoods")
public class DataCustomsImxportGoodsController extends BaseController {

    private final IDataCustomsImxportGoodsService dataCustomsImxportGoodsService;

    /**
     * 查询海关进口货物报关单列表
     */
    @SaCheckPermission("business:customsImxportGoods:list")
    @GetMapping("/list")
    public TableDataInfo<DataCustomsImxportGoodsVo> list(DataCustomsImxportGoodsBo bo, PageQuery pageQuery) {
        return dataCustomsImxportGoodsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出海关进口货物报关单列表
     */
    @SaCheckPermission("business:customsImxportGoods:export")
    @Log(title = "海关进口货物报关单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataCustomsImxportGoodsBo bo, HttpServletResponse response) {
        List<DataCustomsImxportGoodsVo> list = dataCustomsImxportGoodsService.queryList(bo);
        ExcelUtil.exportExcel(list, "海关进口货物报关单", DataCustomsImxportGoodsVo.class, response);
    }

    /**
     * 获取海关进口货物报关单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:customsImxportGoods:query")
    @GetMapping("/{id}")
    public R<DataCustomsImxportGoodsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataCustomsImxportGoodsService.queryById(id));
    }

    /**
     * 新增海关进口货物报关单
     */
    @SaCheckPermission("business:customsImxportGoods:add")
    @Log(title = "海关进口货物报关单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataCustomsImxportGoodsBo bo) {
        return toAjax(dataCustomsImxportGoodsService.insertByBo(bo));
    }

    /**
     * 修改海关进口货物报关单
     */
    @SaCheckPermission("business:customsImxportGoods:edit")
    @Log(title = "海关进口货物报关单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataCustomsImxportGoodsBo bo) {
        return toAjax(dataCustomsImxportGoodsService.updateByBo(bo));
    }

    /**
     * 删除海关进口货物报关单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:customsImxportGoods:remove")
    @Log(title = "海关进口货物报关单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataCustomsImxportGoodsService.deleteWithValidByIds(List.of(ids), true));
    }
}
