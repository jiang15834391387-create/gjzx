package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
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
import org.smartlink.business.domain.vo.DataMedicalTreatmentVo;
import org.smartlink.business.domain.bo.DataMedicalTreatmentBo;
import org.smartlink.business.service.IDataMedicalTreatmentService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 非税收入类票据
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/medicalTreatment")
public class DataMedicalTreatmentController extends BaseController {

    private final IDataMedicalTreatmentService dataMedicalTreatmentService;

    /**
     * 查询非税收入类票据列表
     */
    @SaCheckPermission("business:medicalTreatment:list")
    @GetMapping("/list")
    public TableDataInfo<DataMedicalTreatmentVo> list(DataMedicalTreatmentBo bo, PageQuery pageQuery) {
        return dataMedicalTreatmentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出非税收入类票据列表
     */
    @SaCheckPermission("business:medicalTreatment:export")
    @Log(title = "非税收入类票据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataMedicalTreatmentBo bo, HttpServletResponse response) {
        List<DataMedicalTreatmentVo> list = dataMedicalTreatmentService.queryList(bo);
        ExcelUtil.exportExcel(list, "非税收入类票据", DataMedicalTreatmentVo.class, response);
    }

    /**
     * 获取非税收入类票据详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:medicalTreatment:query")
    @GetMapping("/{id}")
    public R<DataMedicalTreatmentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataMedicalTreatmentService.queryById(id));
    }

    /**
     * 新增非税收入类票据
     */
    @SaCheckPermission("business:medicalTreatment:add")
    @Log(title = "非税收入类票据", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataMedicalTreatmentBo bo) {
        return toAjax(dataMedicalTreatmentService.insertByBo(bo));
    }

    /**
     * 修改非税收入类票据
     */
    @SaCheckPermission("business:medicalTreatment:edit")
    @Log(title = "非税收入类票据", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataMedicalTreatmentBo bo) {
        return toAjax(dataMedicalTreatmentService.updateByBo(bo));
    }

    /**
     * 删除非税收入类票据
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:medicalTreatment:remove")
    @Log(title = "非税收入类票据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataMedicalTreatmentService.deleteWithValidByIds(List.of(ids), true));
    }
}
