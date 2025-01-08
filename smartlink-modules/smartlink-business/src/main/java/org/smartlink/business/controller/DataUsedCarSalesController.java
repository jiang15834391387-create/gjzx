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
import org.smartlink.business.domain.vo.DataUsedCarSalesVo;
import org.smartlink.business.domain.bo.DataUsedCarSalesBo;
import org.smartlink.business.service.IDataUsedCarSalesService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 二手车销售统一发票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/usedCarSales")
public class DataUsedCarSalesController extends BaseController {

    private final IDataUsedCarSalesService dataUsedCarSalesService;

    /**
     * 查询二手车销售统一发票列表
     */
    @SaCheckPermission("business:usedCarSales:list")
    @GetMapping("/list")
    public TableDataInfo<DataUsedCarSalesVo> list(DataUsedCarSalesBo bo, PageQuery pageQuery) {
        return dataUsedCarSalesService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出二手车销售统一发票列表
     */
    @SaCheckPermission("business:usedCarSales:export")
    @Log(title = "二手车销售统一发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataUsedCarSalesBo bo, HttpServletResponse response) {
        List<DataUsedCarSalesVo> list = dataUsedCarSalesService.queryList(bo);
        ExcelUtil.exportExcel(list, "二手车销售统一发票", DataUsedCarSalesVo.class, response);
    }

    /**
     * 获取二手车销售统一发票详细信息
     *
     * @param  id 主键
     */
    @SaCheckPermission("business:usedCarSales:query")
    @GetMapping("/{ id}")
    public R<DataUsedCarSalesVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String  id) {
        return R.ok(dataUsedCarSalesService.queryById( id));
    }

    /**
     * 新增二手车销售统一发票
     */
    @SaCheckPermission("business:usedCarSales:add")
    @Log(title = "二手车销售统一发票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataUsedCarSalesBo bo) {
        return toAjax(dataUsedCarSalesService.insertByBo(bo));
    }

    /**
     * 修改二手车销售统一发票
     */
    @SaCheckPermission("business:usedCarSales:edit")
    @Log(title = "二手车销售统一发票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataUsedCarSalesBo bo) {
        return toAjax(dataUsedCarSalesService.updateByBo(bo));
    }

    /**
     * 删除二手车销售统一发票
     *
     * @param  ids 主键串
     */
    @SaCheckPermission("business:usedCarSales:remove")
    @Log(title = "二手车销售统一发票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[]  ids) {
        return toAjax(dataUsedCarSalesService.deleteWithValidByIds(List.of( ids), true));
    }
}
