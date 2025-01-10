package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataDidiItineraryBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDidiItineraryVo;
import org.smartlink.common.entity.domain.business.service.IDataDidiItineraryService;
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
 * 滴滴行程单
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/didiItinerary")
public class DataDidiItineraryController extends BaseController {

    private final IDataDidiItineraryService dataDidiItineraryService;

    /**
     * 查询滴滴行程单列表
     */
    @SaCheckPermission("business:didiItinerary:list")
    @GetMapping("/list")
    public TableDataInfo<DataDidiItineraryVo> list(DataDidiItineraryBo bo, PageQuery pageQuery) {
        return dataDidiItineraryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出滴滴行程单列表
     */
    @SaCheckPermission("business:didiItinerary:export")
    @Log(title = "滴滴行程单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataDidiItineraryBo bo, HttpServletResponse response) {
        List<DataDidiItineraryVo> list = dataDidiItineraryService.queryList(bo);
        ExcelUtil.exportExcel(list, "滴滴行程单", DataDidiItineraryVo.class, response);
    }

    /**
     * 获取滴滴行程单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:didiItinerary:query")
    @GetMapping("/{id}")
    public R<DataDidiItineraryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataDidiItineraryService.queryById(id));
    }

    /**
     * 新增滴滴行程单
     */
    @SaCheckPermission("business:didiItinerary:add")
    @Log(title = "滴滴行程单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataDidiItineraryBo bo) {
        return toAjax(dataDidiItineraryService.insertByBo(bo));
    }

    /**
     * 修改滴滴行程单
     */
    @SaCheckPermission("business:didiItinerary:edit")
    @Log(title = "滴滴行程单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataDidiItineraryBo bo) {
        return toAjax(dataDidiItineraryService.updateByBo(bo));
    }

    /**
     * 删除滴滴行程单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:didiItinerary:remove")
    @Log(title = "滴滴行程单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataDidiItineraryService.deleteWithValidByIds(List.of(ids), true));
    }
}
