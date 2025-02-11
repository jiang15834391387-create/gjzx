package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataDutyPaidProofDetailsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDutyPaidProofDetailsVo;
import org.smartlink.common.entity.domain.business.service.IDataDutyPaidProofDetailsService;
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
 * 完税证明明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/dutyPaidProofDetails")
public class DataDutyPaidProofDetailsController extends BaseController {

    private final IDataDutyPaidProofDetailsService dataDutyPaidProofDetailsService;

    /**
     * 查询完税证明明细列表
     */
    @SaCheckPermission("business:dutyPaidProofDetails:list")
    @GetMapping("/list")
    public TableDataInfo<DataDutyPaidProofDetailsVo> list(DataDutyPaidProofDetailsBo bo, PageQuery pageQuery) {
        return dataDutyPaidProofDetailsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出完税证明明细列表
     */
    @SaCheckPermission("business:dutyPaidProofDetails:export")
    @Log(title = "完税证明明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataDutyPaidProofDetailsBo bo, HttpServletResponse response) {
        List<DataDutyPaidProofDetailsVo> list = dataDutyPaidProofDetailsService.queryList(bo);
        ExcelUtil.exportExcel(list, "完税证明明细", DataDutyPaidProofDetailsVo.class, response);
    }

    /**
     * 获取完税证明明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:dutyPaidProofDetails:query")
    @GetMapping("/{id}")
    public R<DataDutyPaidProofDetailsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataDutyPaidProofDetailsService.queryById(id));
    }

    /**
     * 新增完税证明明细
     */
    @SaCheckPermission("business:dutyPaidProofDetails:add")
    @Log(title = "完税证明明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataDutyPaidProofDetailsBo bo) {
        return toAjax(dataDutyPaidProofDetailsService.insertByBo(bo));
    }

    /**
     * 修改完税证明明细
     */
    @SaCheckPermission("business:dutyPaidProofDetails:edit")
    @Log(title = "完税证明明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataDutyPaidProofDetailsBo bo) {
        return toAjax(dataDutyPaidProofDetailsService.updateByBo(bo));
    }

    /**
     * 删除完税证明明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:dutyPaidProofDetails:remove")
    @Log(title = "完税证明明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataDutyPaidProofDetailsService.deleteWithValidByIds(List.of(ids), true));
    }
}
