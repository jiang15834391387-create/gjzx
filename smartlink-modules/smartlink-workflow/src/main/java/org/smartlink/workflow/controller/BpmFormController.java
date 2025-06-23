package org.smartlink.workflow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.smartlink.common.core.domain.R;
import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.workflow.domain.BpmFormDO;
import org.smartlink.workflow.domain.vo.form.BpmFormRespVO;
import org.smartlink.workflow.domain.vo.form.BpmFormSaveReqVO;
import org.smartlink.workflow.domain.vo.form.BpmFormVo;
import org.smartlink.workflow.service.BpmFormService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Tag(name = "管理后台 - 动态表单")
@RestController
@RequestMapping("/system/bpm/form")
@Validated
public class BpmFormController {

    @Resource
    private BpmFormService formService;

    @PostMapping("/create")
    @Operation(summary = "创建动态表单")
    //@SaCheckPermission("bpm:form:create")
    public R<Long> createForm(@Valid @RequestBody BpmFormVo createReqVO) {
        return R.ok(formService.createForm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态表单")
    //@SaCheckPermission("bpm:form:update")
    public R<Boolean> updateForm(@Valid @RequestBody BpmFormVo updateReqVO) {
        formService.updateForm(updateReqVO);
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态表单")
    @Parameter(name = "id", description = "编号", required = true)
    // @SaCheckPermission("bpm:form:delete")
    public R<Boolean> deleteForm(@RequestParam("id") Long id) {
        formService.deleteForm(id);
        return R.ok(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态表单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    // @SaCheckPermission("bpm:form:query")
    public R<BpmFormVo> getForm(@RequestParam("id") Long id) {
        BpmFormVo form = formService.getForm(id);
        return R.ok(form);
    }

    @GetMapping("/getExpenseAccount")
    @Operation(summary = "取报销单键值")
    @Parameter(name = "id", description = "编号", required = true)
    // @SaCheckPermission("bpm:form:query")
    public R<Map<String, String>> getExpenseAccount(@RequestParam("id") Long id) {
        return R.ok(formService.getExpenseAccount(id));
    }

    @GetMapping({"/list-all-simple", "/simple-list"})
    @Operation(summary = "获得动态表单的精简列表", description = "用于表单下拉框")
    public R<List<BpmFormVo>> getFormSimpleList() {
        List<BpmFormVo> list = formService.getFormList();
        return R.ok(list);
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态表单分页")
    public Page<BpmFormDO> getFormPage(@Valid BpmFormDO pageVO, PageQuery pageQuery) {
        return formService.getFormPage(pageVO,pageQuery);
    }

}
