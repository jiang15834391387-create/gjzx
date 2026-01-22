package org.smartlink.workflow.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.excel.utils.ExcelUtil;
import org.smartlink.common.idempotent.annotation.RepeatSubmit;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.workflow.domain.bo.WfCategoryBo;
import org.smartlink.workflow.domain.vo.WfCategoryVo;
import org.smartlink.workflow.service.IWfCategoryService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程分类
 *
 * @author may
 * @date 2023-06-28
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/category")
public class WfCategoryController extends BaseController {

    private final IWfCategoryService wfCategoryService;

    /**
     * 查询流程分类列表
     */
    //@SaCheckPermission("workflow:category:list")
    @GetMapping("/list")
    public R<List<WfCategoryVo>> list(WfCategoryBo bo) {
        List<WfCategoryVo> list = wfCategoryService.queryList(bo);
        return R.ok(list);

    }

    /**
     * 导出流程分类列表
     */
    //@SaCheckPermission("workflow:category:export")
    @Log(title = "流程分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfCategoryBo bo, HttpServletResponse response) {
        List<WfCategoryVo> list = wfCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "流程分类", WfCategoryVo.class, response);
    }

    /**
     * 获取流程分类详细信息
     *
     * @param id 主键
     */
    //@SaCheckPermission("workflow:category:query")
    @GetMapping("/{id}")
    public R<WfCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                   @PathVariable Long id) {
        return R.ok(wfCategoryService.queryById(id));
    }

    /**
     * 新增流程分类
     */
    @SaCheckPermission("workflow:category:add")
    @Log(title = "流程分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfCategoryBo bo) {
        return toAjax(wfCategoryService.insertByBo(bo));
    }

    /**
     * 修改流程分类
     */
    @SaCheckPermission("workflow:category:edit")
    @Log(title = "流程分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfCategoryBo bo) {
        return toAjax(wfCategoryService.updateByBo(bo));
    }

    /**
     * 删除流程分类
     *
     * @param ids 主键串
     */
    @SaCheckPermission("workflow:category:remove")
    @Log(title = "流程分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(wfCategoryService.deleteWithValidByIds(List.of(ids), true));
    }
}
