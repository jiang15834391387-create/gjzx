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
import org.smartlink.business.domain.vo.DataAircraftInvoiceVo;
import org.smartlink.business.domain.bo.DataAircraftInvoiceBo;
import org.smartlink.business.service.IDataAircraftInvoiceService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 机打发票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/aircraftInvoice")
public class DataAircraftInvoiceController extends BaseController {

    private final IDataAircraftInvoiceService dataAircraftInvoiceService;

    /**
     * 查询机打发票列表
     */
    @SaCheckPermission("business:aircraftInvoice:list")
    @GetMapping("/list")
    public TableDataInfo<DataAircraftInvoiceVo> list(DataAircraftInvoiceBo bo, PageQuery pageQuery) {
        return dataAircraftInvoiceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出机打发票列表
     */
    @SaCheckPermission("business:aircraftInvoice:export")
    @Log(title = "机打发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataAircraftInvoiceBo bo, HttpServletResponse response) {
        List<DataAircraftInvoiceVo> list = dataAircraftInvoiceService.queryList(bo);
        ExcelUtil.exportExcel(list, "机打发票", DataAircraftInvoiceVo.class, response);
    }

    /**
     * 获取机打发票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:aircraftInvoice:query")
    @GetMapping("/{id}")
    public R<DataAircraftInvoiceVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataAircraftInvoiceService.queryById(id));
    }

    /**
     * 新增机打发票
     */
    @SaCheckPermission("business:aircraftInvoice:add")
    @Log(title = "机打发票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataAircraftInvoiceBo bo) {
        return toAjax(dataAircraftInvoiceService.insertByBo(bo));
    }

    /**
     * 修改机打发票
     */
    @SaCheckPermission("business:aircraftInvoice:edit")
    @Log(title = "机打发票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataAircraftInvoiceBo bo) {
        return toAjax(dataAircraftInvoiceService.updateByBo(bo));
    }

    /**
     * 删除机打发票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:aircraftInvoice:remove")
    @Log(title = "机打发票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataAircraftInvoiceService.deleteWithValidByIds(List.of(ids), true));
    }
}
