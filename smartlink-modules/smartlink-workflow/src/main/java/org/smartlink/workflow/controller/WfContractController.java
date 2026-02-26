package org.smartlink.workflow.controller;

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
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.workflow.domain.bo.WfContractBo;
import org.smartlink.workflow.domain.vo.WfContractTreeVo;
import org.smartlink.workflow.domain.vo.WfContractVo;
import org.smartlink.workflow.service.IWfContractService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合同管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/contract")
public class WfContractController extends BaseController {

    private final IWfContractService wfContractService;

    @GetMapping("/list")
    public TableDataInfo<WfContractVo> list(WfContractBo bo, PageQuery pageQuery) {
        return wfContractService.queryPageList(bo, pageQuery);
    }

    @GetMapping("/listAll")
    public R<List<WfContractVo>> listAll(WfContractBo bo) {
        return R.ok(wfContractService.queryList(bo));
    }

    @GetMapping("/tree")
    public R<List<WfContractTreeVo>> tree(WfContractBo bo) {
        return R.ok(wfContractService.treeList(bo));
    }

    @PostMapping("/export")
    public void export(WfContractBo bo, HttpServletResponse response) {
        List<WfContractVo> list = wfContractService.queryList(bo);
        ExcelUtil.exportExcel(list, "合同管理", WfContractVo.class, response);
    }

    @GetMapping("/{id}")
    public R<WfContractVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(wfContractService.queryById(id));
    }

    @Log(title = "合同管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfContractBo bo) {
        return toAjax(wfContractService.insertByBo(bo));
    }

    @Log(title = "合同管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfContractBo bo) {
        return toAjax(wfContractService.updateByBo(bo));
    }

    @Log(title = "合同管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(wfContractService.deleteWithValidByIds(List.of(ids), true));
    }
}
