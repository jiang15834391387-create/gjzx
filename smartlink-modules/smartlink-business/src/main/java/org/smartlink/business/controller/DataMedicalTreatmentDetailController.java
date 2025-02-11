package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataMedicalTreatmentDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataMedicalTreatmentDetailVo;
import org.smartlink.common.entity.domain.business.service.IDataMedicalTreatmentDetailService;
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
 * 医疗票明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/medicalTreatmentDetail")
public class DataMedicalTreatmentDetailController extends BaseController {

    private final IDataMedicalTreatmentDetailService dataMedicalTreatmentDetailService;

    /**
     * 查询医疗票明细列表
     */
    @SaCheckPermission("business:medicalTreatmentDetail:list")
    @GetMapping("/list")
    public TableDataInfo<DataMedicalTreatmentDetailVo> list(DataMedicalTreatmentDetailBo bo, PageQuery pageQuery) {
        return dataMedicalTreatmentDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出医疗票明细列表
     */
    @SaCheckPermission("business:medicalTreatmentDetail:export")
    @Log(title = "医疗票明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataMedicalTreatmentDetailBo bo, HttpServletResponse response) {
        List<DataMedicalTreatmentDetailVo> list = dataMedicalTreatmentDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "医疗票明细", DataMedicalTreatmentDetailVo.class, response);
    }

    /**
     * 获取医疗票明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:medicalTreatmentDetail:query")
    @GetMapping("/{id}")
    public R<DataMedicalTreatmentDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataMedicalTreatmentDetailService.queryById(id));
    }

    /**
     * 新增医疗票明细
     */
    @SaCheckPermission("business:medicalTreatmentDetail:add")
    @Log(title = "医疗票明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataMedicalTreatmentDetailBo bo) {
        return toAjax(dataMedicalTreatmentDetailService.insertByBo(bo));
    }

    /**
     * 修改医疗票明细
     */
    @SaCheckPermission("business:medicalTreatmentDetail:edit")
    @Log(title = "医疗票明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataMedicalTreatmentDetailBo bo) {
        return toAjax(dataMedicalTreatmentDetailService.updateByBo(bo));
    }

    /**
     * 删除医疗票明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:medicalTreatmentDetail:remove")
    @Log(title = "医疗票明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataMedicalTreatmentDetailService.deleteWithValidByIds(List.of(ids), true));
    }
}
