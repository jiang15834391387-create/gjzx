package org.smartlink.workflow.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.excel.utils.ExcelUtil;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.workflow.domain.bo.WfInventoryBo;
import org.smartlink.workflow.domain.vo.WfInventoryTreeVo;
import org.smartlink.workflow.domain.vo.WfInventoryVo;
import org.smartlink.workflow.service.IWfInventoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/inventory")
public class WfInventoryController extends BaseController {

    private final IWfInventoryService inventoryService;

    @GetMapping("/list")
    public TableDataInfo<WfInventoryVo> list(WfInventoryBo bo, PageQuery pageQuery) {
        return inventoryService.queryPageList(bo, pageQuery);
    }

    @Log(title = "库存管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfInventoryBo bo, HttpServletResponse response) {
        List<WfInventoryVo> list = inventoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "库存数据", WfInventoryVo.class, response);
    }

    @GetMapping("/{id}")
    public R<WfInventoryVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(inventoryService.queryById(id));
    }

    @Log(title = "库存管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@RequestBody WfInventoryBo bo) {
        return toAjax(inventoryService.insertByBo(bo));
    }

    @Log(title = "库存管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@RequestBody WfInventoryBo bo) {
        return toAjax(inventoryService.updateByBo(bo));
    }

    @Log(title = "库存管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable List<Long> ids) {
        return toAjax(inventoryService.deleteWithValidByIds(ids));
    }

    @Log(title = "入库操作", businessType = BusinessType.UPDATE)
    @PostMapping("/inbound")
    public R<Void> inbound(@RequestBody WfInventoryBo bo) {
        return toAjax(inventoryService.inbound(bo));
    }

    @Log(title = "出库操作", businessType = BusinessType.UPDATE)
    @PostMapping("/outbound")
    public R<Void> outbound(@RequestBody WfInventoryBo bo) {
        return toAjax(inventoryService.outbound(bo));
    }

    /**
     * 动态获取库存物品树形结构 (用于左侧树组件)
     */
    @GetMapping("/tree")
    public R<List<WfInventoryTreeVo>> inventoryTree() {
        return R.ok(inventoryService.queryInventoryTree());
    }
}
