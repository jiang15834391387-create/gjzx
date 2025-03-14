package org.smartlink.workflow.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.workflow.domain.TestFormManage;
import org.springframework.data.repository.query.Param;
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
import org.smartlink.workflow.domain.vo.TestFormManageVo;
import org.smartlink.workflow.domain.bo.TestFormManageBo;
import org.smartlink.workflow.service.ITestFormManageService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 单管理
 *
 * @author Lion Li
 * @date 2025-01-11
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/formManage")
public class TestFormManageController extends BaseController {

    private final ITestFormManageService testFormManageService;

    /**
     * 查询单管理列表
     */
    @SaCheckPermission("system:formManage:list")
    @GetMapping("/list")
    public TableDataInfo<TestFormManageVo> list(TestFormManageBo bo, PageQuery pageQuery) {
        return testFormManageService.queryPageList(bo, pageQuery);
    }


    /**
     * 查询报销单分组
     */
    @SaCheckPermission("system:formManage:list")
    @GetMapping("/listByGroup")
    public R<List<TestFormManageVo>> listByGroup() {
        return R.ok(testFormManageService.queryPageListGroup());
    }



    /**
     * 获取单管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:formManage:query")
    @GetMapping("/{id}")
    public R<TestFormManageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(testFormManageService.queryById(id));
    }

    /**
     * 新增单管理
     */
    @SaCheckPermission("system:formManage:add")
    @Log(title = "单管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody TestFormManageBo bo) {
        return testFormManageService.insertByBo(bo);
    }

    /**
     * 修改单管理
     */
    @SaCheckPermission("system:formManage:edit")
    @Log(title = "单管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody TestFormManageBo bo) {
        return testFormManageService.updateByBo(bo);
    }

    /**
     * 删除单管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:formManage:remove")
    @Log(title = "单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return testFormManageService.deleteWithValidByIds(List.of(ids));
    }

    /**
     * 查询
     * @param type
     * @return
     */
    @SaCheckPermission("system:formManage:selectBy")
    @GetMapping("/selectBy/{type}")
    public R<List<TestFormManageVo>> selectBy(@NotNull(message = "值不能为空")
                                       @PathVariable String type) {
        return R.ok(testFormManageService.selectBy(type));
    }


    /**
     * 表单
     * @return
     */
    @GetMapping("/selectCategory")
    public R<List<TestFormManageVo>> selectCategory() {
        return R.ok(testFormManageService.selectCategory());
    }

    /**
     * 表单
     * @param categoryId
     * @return
     */
    @GetMapping("/selectFrom")
    public R<List<TestFormManageVo>> selectFrom(@Param("categoryId") Long categoryId) {
        return R.ok(testFormManageService.selectFrom(categoryId));
    }

}
