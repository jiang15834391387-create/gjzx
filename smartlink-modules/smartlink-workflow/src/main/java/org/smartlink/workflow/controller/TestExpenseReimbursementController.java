package org.smartlink.workflow.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.workflow.domain.bo.TestExpenseReimbursementBo;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;
import org.smartlink.workflow.service.ITestExpenseReimbursementService;
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
 * 费用报销申请
 *
 * @author Lion Li
 * @date 2025-01-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/expenseReimbursement")
public class TestExpenseReimbursementController extends BaseController {

    private final ITestExpenseReimbursementService testExpenseReimbursementService;

    /**
     * 查询费用报销申请列表
     */
    @SaCheckPermission("system:expenseReimbursement:list")
    @GetMapping("/list")
    public TableDataInfo<TestExpenseReimbursementVo> list(TestExpenseReimbursementBo bo, PageQuery pageQuery) {
        return testExpenseReimbursementService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取费用报销申请详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:expenseReimbursement:query")
    @GetMapping("/{id}")
    public R<TestExpenseReimbursementVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(testExpenseReimbursementService.queryById(id));
    }

    /**
     * 新增费用报销申请
     */
    @SaCheckPermission("system:expenseReimbursement:add")
    @Log(title = "费用报销申请", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TestExpenseReimbursementBo bo) {
        return toAjax(testExpenseReimbursementService.insertByBo(bo));
    }

    /**
     * 修改费用报销申请
     */
    @SaCheckPermission("system:expenseReimbursement:edit")
    @Log(title = "费用报销申请", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TestExpenseReimbursementBo bo) {
        return toAjax(testExpenseReimbursementService.updateByBo(bo));
    }

    /**
     * 删除费用报销申请
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:expenseReimbursement:remove")
    @Log(title = "费用报销申请", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(testExpenseReimbursementService.deleteWithValidByIds(List.of(ids), true));
    }
}
