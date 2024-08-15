package org.smartlink.server.nodeType.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.server.nodeType.domain.DataNodeType;
import org.smartlink.server.nodeType.domain.bo.DataNodeTypeBo;
import org.smartlink.server.nodeType.domain.vo.DataNodeTypeVo;
import org.smartlink.server.nodeType.service.IDataNodeTypeService;
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

/**
 * 树节点
 *
 * @author Lion Li
 * @date 2024-08-15
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/nodeType")
public class DataNodeTypeController extends BaseController {

    private final IDataNodeTypeService dataNodeTypeService;

    /**
     * 查询树节点列表
     */
    @SaCheckPermission("system:nodeType:list")
    @GetMapping("/list")
    public TableDataInfo<DataNodeTypeVo> list(DataNodeTypeBo bo, PageQuery pageQuery) {
        return dataNodeTypeService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出树节点列表
     */
    @SaCheckPermission("system:nodeType:export")
    @Log(title = "树节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataNodeTypeBo bo, HttpServletResponse response) {
        List<DataNodeTypeVo> list = dataNodeTypeService.queryList(bo);
        ExcelUtil.exportExcel(list, "树节点", DataNodeTypeVo.class, response);
    }

    /**
     * 获取树节点详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:nodeType:query")
    @GetMapping("/{id}")
    public R<DataNodeTypeVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataNodeTypeService.queryById(id));
    }

    /**
     * 新增树节点
     */
    @SaCheckPermission("system:nodeType:add")
    @Log(title = "树节点", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataNodeTypeBo bo) {
        return toAjax(dataNodeTypeService.insertByBo(bo));
    }

    /**
     * 修改树节点
     */
    @SaCheckPermission("system:nodeType:edit")
    @Log(title = "树节点", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataNodeTypeBo bo) {
        return toAjax(dataNodeTypeService.updateByBo(bo));
    }

    /**
     * 删除树节点
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:nodeType:remove")
    @Log(title = "树节点", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataNodeTypeService.deleteWithValidByIds(List.of(ids), true));
    }


    /**
     * 同步树节点
     */
    @Log(title = "树节点", businessType = BusinessType.INSERT)
    @PostMapping("/syncTreeNodes")
    public R<Void> syncTreeNodes(@RequestBody List<DataNodeType> bo) {
        return dataNodeTypeService.syncTreeNodes(bo);
    }

}
