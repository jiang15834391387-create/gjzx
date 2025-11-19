package org.smartlink.workflow.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.smartlink.business.doman.dto.StructureDataDTO;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.core.validate.AddGroup;
import org.smartlink.common.core.validate.EditGroup;
import org.smartlink.common.idempotent.annotation.RepeatSubmit;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.workflow.domain.TestFormManage;
import org.smartlink.workflow.domain.bo.TestExpenseReimbursementBo;
import org.smartlink.workflow.domain.vo.TestExpenseReimbursementVo;
import org.smartlink.workflow.service.ITestExpenseReimbursementService;
import org.smartlink.workflow.utils.pdf.TemplateFieldResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用报销申请
 *
 * @author Lion Li
 * @date 2025-01-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@SaIgnore
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
    @RepeatSubmit(interval = 100000, message = "请勿重复提交")
    @PostMapping()
    public R<TestExpenseReimbursementVo> add(@Validated(AddGroup.class) @RequestBody TestExpenseReimbursementBo bo) {
        return R.ok(testExpenseReimbursementService.insertByBo(bo));
    }

    /**
     * 修改费用报销申请
     */
    @SaCheckPermission("system:expenseReimbursement:edit")
    @Log(title = "费用报销申请", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<TestExpenseReimbursementVo> edit(@Validated(EditGroup.class) @RequestBody TestExpenseReimbursementBo bo) {
        return R.ok(testExpenseReimbursementService.updateByBo(bo));
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

    /**
     * 测试
     */
    @Autowired
    private TemplateFieldResolver resolver;
    @PostMapping("/aaaaa")
    public Map<String, Object> aaaaa(){
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("field_1", "");

        Map<String, Object> filledData = resolver.resolveTemplateFields("TEMPLATE_001", templateData);
        return filledData;
    }


    /**
     * 获取费用报销申请详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:expenseReimbursement:query")
    @GetMapping("/byFromId/{id}")
    public R<List<TestFormManage>> byFromId(@NotNull(message = "表单id为空")
                                                 @PathVariable String id) {
        return R.ok(testExpenseReimbursementService.byFromId(id));
    }


    /**
     * 上传收据回单
     */
    @PostMapping("/setReceiptUrl")
    public R<Void> setReceiptUrl(@RequestBody TestExpenseReimbursementBo bo) {
        return testExpenseReimbursementService.setReceiptUrl(bo);
    }
    /**
     *查询发票附件/结构化数据/银行回单
     */
    @GetMapping("/selectStructureData")
    public R<List<StructureDataDTO>> selectStructureData(@RequestBody List<String> workIds){
        return testExpenseReimbursementService.selectStructureData(workIds);
    }

    @GetMapping("/getUserInfo")
    public R<List<Map<String,String>>> getUserInfo(){
        List<Map<String,String>> sysUsers = testExpenseReimbursementService.selectList();
        return R.ok(sysUsers);
    }

    /**
     * 用户数据清洗
     */
    @PostMapping("/dataRinse")
    public R<Void> dataRinse(@RequestBody String[] userIds) {
        return testExpenseReimbursementService.dataRinse(List.of(userIds));
    }

}
