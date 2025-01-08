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
import org.smartlink.business.domain.vo.DataFlightsItineraryDetailVo;
import org.smartlink.business.domain.bo.DataFlightsItineraryDetailBo;
import org.smartlink.business.service.IDataFlightsItineraryDetailService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 航空电子行程单明细
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/flightsItineraryDetail")
public class DataFlightsItineraryDetailController extends BaseController {

    private final IDataFlightsItineraryDetailService dataFlightsItineraryDetailService;

    /**
     * 查询航空电子行程单明细列表
     */
    @SaCheckPermission("business:flightsItineraryDetail:list")
    @GetMapping("/list")
    public TableDataInfo<DataFlightsItineraryDetailVo> list(DataFlightsItineraryDetailBo bo, PageQuery pageQuery) {
        return dataFlightsItineraryDetailService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出航空电子行程单明细列表
     */
    @SaCheckPermission("business:flightsItineraryDetail:export")
    @Log(title = "航空电子行程单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataFlightsItineraryDetailBo bo, HttpServletResponse response) {
        List<DataFlightsItineraryDetailVo> list = dataFlightsItineraryDetailService.queryList(bo);
        ExcelUtil.exportExcel(list, "航空电子行程单明细", DataFlightsItineraryDetailVo.class, response);
    }

    /**
     * 获取航空电子行程单明细详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:flightsItineraryDetail:query")
    @GetMapping("/{id}")
    public R<DataFlightsItineraryDetailVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataFlightsItineraryDetailService.queryById(id));
    }

    /**
     * 新增航空电子行程单明细
     */
    @SaCheckPermission("business:flightsItineraryDetail:add")
    @Log(title = "航空电子行程单明细", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataFlightsItineraryDetailBo bo) {
        return toAjax(dataFlightsItineraryDetailService.insertByBo(bo));
    }

    /**
     * 修改航空电子行程单明细
     */
    @SaCheckPermission("business:flightsItineraryDetail:edit")
    @Log(title = "航空电子行程单明细", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataFlightsItineraryDetailBo bo) {
        return toAjax(dataFlightsItineraryDetailService.updateByBo(bo));
    }

    /**
     * 删除航空电子行程单明细
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:flightsItineraryDetail:remove")
    @Log(title = "航空电子行程单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataFlightsItineraryDetailService.deleteWithValidByIds(List.of(ids), true));
    }
}
