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
import org.smartlink.business.domain.vo.DataMotorVehicleSaleVo;
import org.smartlink.business.domain.bo.DataMotorVehicleSaleBo;
import org.smartlink.business.service.IDataMotorVehicleSaleService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 机动车销售发票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/motorVehicleSale")
public class DataMotorVehicleSaleController extends BaseController {

    private final IDataMotorVehicleSaleService dataMotorVehicleSaleService;

    /**
     * 查询机动车销售发票列表
     */
    @SaCheckPermission("business:motorVehicleSale:list")
    @GetMapping("/list")
    public TableDataInfo<DataMotorVehicleSaleVo> list(DataMotorVehicleSaleBo bo, PageQuery pageQuery) {
        return dataMotorVehicleSaleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出机动车销售发票列表
     */
    @SaCheckPermission("business:motorVehicleSale:export")
    @Log(title = "机动车销售发票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataMotorVehicleSaleBo bo, HttpServletResponse response) {
        List<DataMotorVehicleSaleVo> list = dataMotorVehicleSaleService.queryList(bo);
        ExcelUtil.exportExcel(list, "机动车销售发票", DataMotorVehicleSaleVo.class, response);
    }

    /**
     * 获取机动车销售发票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:motorVehicleSale:query")
    @GetMapping("/{id}")
    public R<DataMotorVehicleSaleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataMotorVehicleSaleService.queryById(id));
    }

    /**
     * 新增机动车销售发票
     */
    @SaCheckPermission("business:motorVehicleSale:add")
    @Log(title = "机动车销售发票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataMotorVehicleSaleBo bo) {
        return toAjax(dataMotorVehicleSaleService.insertByBo(bo));
    }

    /**
     * 修改机动车销售发票
     */
    @SaCheckPermission("business:motorVehicleSale:edit")
    @Log(title = "机动车销售发票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataMotorVehicleSaleBo bo) {
        return toAjax(dataMotorVehicleSaleService.updateByBo(bo));
    }

    /**
     * 删除机动车销售发票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:motorVehicleSale:remove")
    @Log(title = "机动车销售发票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataMotorVehicleSaleService.deleteWithValidByIds(List.of(ids), true));
    }
}
