package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataTaxiTicketsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataTaxiTicketsVo;
import org.smartlink.common.entity.domain.business.service.IDataTaxiTicketsService;
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
 * 出租车发票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/taxiTickets")
public class DataTaxiTicketsController extends BaseController {

    private final IDataTaxiTicketsService dataTaxiTicketsService;

    /**
     * 查询出租车发票列表
     */
    @SaCheckPermission("business:taxiTickets:list")
    @GetMapping("/list")
    public TableDataInfo<DataTaxiTicketsVo> list(DataTaxiTicketsBo bo, PageQuery pageQuery) {
        return dataTaxiTicketsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出出租车发票列表
     */
    @SaCheckPermission("business:taxiTickets:export")
    @Log(title = "出租车发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataTaxiTicketsBo bo, HttpServletResponse response) {
        List<DataTaxiTicketsVo> list = dataTaxiTicketsService.queryList(bo);
        ExcelUtil.exportExcel(list, "出租车发票", DataTaxiTicketsVo.class, response);
    }

    /**
     * 获取出租车发票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:taxiTickets:query")
    @GetMapping("/{id}")
    public R<DataTaxiTicketsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataTaxiTicketsService.queryById(id));
    }

    /**
     * 新增出租车发票
     */
    @SaCheckPermission("business:taxiTickets:add")
    @Log(title = "出租车发票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataTaxiTicketsBo bo) {
        return toAjax(dataTaxiTicketsService.insertByBo(bo));
    }

    /**
     * 修改出租车发票
     */
    @SaCheckPermission("business:taxiTickets:edit")
    @Log(title = "出租车发票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataTaxiTicketsBo bo) {
        return toAjax(dataTaxiTicketsService.updateByBo(bo));
    }

    /**
     * 删除出租车发票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:taxiTickets:remove")
    @Log(title = "出租车发票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataTaxiTicketsService.deleteWithValidByIds(List.of(ids), true));
    }
}
