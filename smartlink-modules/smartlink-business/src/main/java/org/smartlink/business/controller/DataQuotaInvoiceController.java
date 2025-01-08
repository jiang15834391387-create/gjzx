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
import org.smartlink.business.domain.vo.DataQuotaInvoiceVo;
import org.smartlink.business.domain.bo.DataQuotaInvoiceBo;
import org.smartlink.business.service.IDataQuotaInvoiceService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 定额发票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/quotaInvoice")
public class DataQuotaInvoiceController extends BaseController {

    private final IDataQuotaInvoiceService dataQuotaInvoiceService;

    /**
     * 查询定额发票列表
     */
    @SaCheckPermission("business:quotaInvoice:list")
    @GetMapping("/list")
    public TableDataInfo<DataQuotaInvoiceVo> list(DataQuotaInvoiceBo bo, PageQuery pageQuery) {
        return dataQuotaInvoiceService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出定额发票列表
     */
    @SaCheckPermission("business:quotaInvoice:export")
    @Log(title = "定额发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataQuotaInvoiceBo bo, HttpServletResponse response) {
        List<DataQuotaInvoiceVo> list = dataQuotaInvoiceService.queryList(bo);
        ExcelUtil.exportExcel(list, "定额发票", DataQuotaInvoiceVo.class, response);
    }

    /**
     * 获取定额发票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:quotaInvoice:query")
    @GetMapping("/{id}")
    public R<DataQuotaInvoiceVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataQuotaInvoiceService.queryById(id));
    }

    /**
     * 新增定额发票
     */
    @SaCheckPermission("business:quotaInvoice:add")
    @Log(title = "定额发票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataQuotaInvoiceBo bo) {
        return toAjax(dataQuotaInvoiceService.insertByBo(bo));
    }

    /**
     * 修改定额发票
     */
    @SaCheckPermission("business:quotaInvoice:edit")
    @Log(title = "定额发票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataQuotaInvoiceBo bo) {
        return toAjax(dataQuotaInvoiceService.updateByBo(bo));
    }

    /**
     * 删除定额发票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:quotaInvoice:remove")
    @Log(title = "定额发票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataQuotaInvoiceService.deleteWithValidByIds(List.of(ids), true));
    }
}
