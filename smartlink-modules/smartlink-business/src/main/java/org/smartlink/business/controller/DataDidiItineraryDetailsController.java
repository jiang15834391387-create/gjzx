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
import org.smartlink.business.domain.vo.DataDidiItineraryDetailsVo;
import org.smartlink.business.domain.bo.DataDidiItineraryDetailsBo;
import org.smartlink.business.service.IDataDidiItineraryDetailsService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 滴滴行程单明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/didiItineraryDetails")
public class DataDidiItineraryDetailsController extends BaseController {

    private final IDataDidiItineraryDetailsService dataDidiItineraryDetailsService;

    /**
     * 查询滴滴行程单明细列表
     */
    @SaCheckPermission("business:didiItineraryDetails:list")
    @GetMapping("/list")
    public TableDataInfo<DataDidiItineraryDetailsVo> list(DataDidiItineraryDetailsBo bo, PageQuery pageQuery) {
        return dataDidiItineraryDetailsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出滴滴行程单明细列表
     */
    @SaCheckPermission("business:didiItineraryDetails:export")
    @Log(title = "滴滴行程单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataDidiItineraryDetailsBo bo, HttpServletResponse response) {
        List<DataDidiItineraryDetailsVo> list = dataDidiItineraryDetailsService.queryList(bo);
        ExcelUtil.exportExcel(list, "滴滴行程单明细", DataDidiItineraryDetailsVo.class, response);
    }

    /**
     * 获取滴滴行程单明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:didiItineraryDetails:query")
    @GetMapping("/{id}")
    public R<DataDidiItineraryDetailsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataDidiItineraryDetailsService.queryById(id));
    }

    /**
     * 新增滴滴行程单明细
     */
    @SaCheckPermission("business:didiItineraryDetails:add")
    @Log(title = "滴滴行程单明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataDidiItineraryDetailsBo bo) {
        return toAjax(dataDidiItineraryDetailsService.insertByBo(bo));
    }

    /**
     * 修改滴滴行程单明细
     */
    @SaCheckPermission("business:didiItineraryDetails:edit")
    @Log(title = "滴滴行程单明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataDidiItineraryDetailsBo bo) {
        return toAjax(dataDidiItineraryDetailsService.updateByBo(bo));
    }

    /**
     * 删除滴滴行程单明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:didiItineraryDetails:remove")
    @Log(title = "滴滴行程单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataDidiItineraryDetailsService.deleteWithValidByIds(List.of(ids), true));
    }
}
