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
import org.smartlink.business.domain.vo.DataFlightItineraryVo;
import org.smartlink.business.domain.bo.DataFlightItineraryBo;
import org.smartlink.business.service.IDataFlightItineraryService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 航空电子行程单
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/flightItinerary")
public class DataFlightItineraryController extends BaseController {

    private final IDataFlightItineraryService dataFlightItineraryService;

    /**
     * 查询航空电子行程单列表
     */
    @SaCheckPermission("business:flightItinerary:list")
    @GetMapping("/list")
    public TableDataInfo<DataFlightItineraryVo> list(DataFlightItineraryBo bo, PageQuery pageQuery) {
        return dataFlightItineraryService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出航空电子行程单列表
     */
    @SaCheckPermission("business:flightItinerary:export")
    @Log(title = "航空电子行程单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataFlightItineraryBo bo, HttpServletResponse response) {
        List<DataFlightItineraryVo> list = dataFlightItineraryService.queryList(bo);
        ExcelUtil.exportExcel(list, "航空电子行程单", DataFlightItineraryVo.class, response);
    }

    /**
     * 获取航空电子行程单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:flightItinerary:query")
    @GetMapping("/{id}")
    public R<DataFlightItineraryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataFlightItineraryService.queryById(id));
    }

    /**
     * 新增航空电子行程单
     */
    @SaCheckPermission("business:flightItinerary:add")
    @Log(title = "航空电子行程单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataFlightItineraryBo bo) {
        return toAjax(dataFlightItineraryService.insertByBo(bo));
    }

    /**
     * 修改航空电子行程单
     */
    @SaCheckPermission("business:flightItinerary:edit")
    @Log(title = "航空电子行程单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataFlightItineraryBo bo) {
        return toAjax(dataFlightItineraryService.updateByBo(bo));
    }

    /**
     * 删除航空电子行程单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:flightItinerary:remove")
    @Log(title = "航空电子行程单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataFlightItineraryService.deleteWithValidByIds(List.of(ids), true));
    }
}
