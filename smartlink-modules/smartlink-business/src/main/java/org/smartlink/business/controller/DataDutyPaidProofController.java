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
import org.smartlink.business.domain.vo.DataDutyPaidProofVo;
import org.smartlink.business.domain.bo.DataDutyPaidProofBo;
import org.smartlink.business.service.IDataDutyPaidProofService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 完税证明
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/dutyPaidProof")
public class DataDutyPaidProofController extends BaseController {

    private final IDataDutyPaidProofService dataDutyPaidProofService;

    /**
     * 查询完税证明列表
     */
    @SaCheckPermission("business:dutyPaidProof:list")
    @GetMapping("/list")
    public TableDataInfo<DataDutyPaidProofVo> list(DataDutyPaidProofBo bo, PageQuery pageQuery) {
        return dataDutyPaidProofService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出完税证明列表
     */
    @SaCheckPermission("business:dutyPaidProof:export")
    @Log(title = "完税证明", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataDutyPaidProofBo bo, HttpServletResponse response) {
        List<DataDutyPaidProofVo> list = dataDutyPaidProofService.queryList(bo);
        ExcelUtil.exportExcel(list, "完税证明", DataDutyPaidProofVo.class, response);
    }

    /**
     * 获取完税证明详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:dutyPaidProof:query")
    @GetMapping("/{id}")
    public R<DataDutyPaidProofVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataDutyPaidProofService.queryById(id));
    }

    /**
     * 新增完税证明
     */
    @SaCheckPermission("business:dutyPaidProof:add")
    @Log(title = "完税证明", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataDutyPaidProofBo bo) {
        return toAjax(dataDutyPaidProofService.insertByBo(bo));
    }

    /**
     * 修改完税证明
     */
    @SaCheckPermission("business:dutyPaidProof:edit")
    @Log(title = "完税证明", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataDutyPaidProofBo bo) {
        return toAjax(dataDutyPaidProofService.updateByBo(bo));
    }

    /**
     * 删除完税证明
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:dutyPaidProof:remove")
    @Log(title = "完税证明", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataDutyPaidProofService.deleteWithValidByIds(List.of(ids), true));
    }
}
