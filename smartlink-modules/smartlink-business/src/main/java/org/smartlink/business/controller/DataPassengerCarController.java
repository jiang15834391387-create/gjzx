package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataPassengerCarBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataPassengerCarVo;
import org.smartlink.common.entity.domain.business.service.IDataPassengerCarService;
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
 * 客运汽车票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/passengerCar")
public class DataPassengerCarController extends BaseController {

    private final IDataPassengerCarService dataPassengerCarService;

    /**
     * 查询客运汽车票列表
     */
    @SaCheckPermission("business:passengerCar:list")
    @GetMapping("/list")
    public TableDataInfo<DataPassengerCarVo> list(DataPassengerCarBo bo, PageQuery pageQuery) {
        return dataPassengerCarService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出客运汽车票列表
     */
    @SaCheckPermission("business:passengerCar:export")
    @Log(title = "客运汽车票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataPassengerCarBo bo, HttpServletResponse response) {
        List<DataPassengerCarVo> list = dataPassengerCarService.queryList(bo);
        ExcelUtil.exportExcel(list, "客运汽车票", DataPassengerCarVo.class, response);
    }

    /**
     * 获取客运汽车票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:passengerCar:query")
    @GetMapping("/{id}")
    public R<DataPassengerCarVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataPassengerCarService.queryById(id));
    }

    /**
     * 新增客运汽车票
     */
    @SaCheckPermission("business:passengerCar:add")
    @Log(title = "客运汽车票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataPassengerCarBo bo) {
        return toAjax(dataPassengerCarService.insertByBo(bo));
    }

    /**
     * 修改客运汽车票
     */
    @SaCheckPermission("business:passengerCar:edit")
    @Log(title = "客运汽车票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataPassengerCarBo bo) {
        return toAjax(dataPassengerCarService.updateByBo(bo));
    }

    /**
     * 删除客运汽车票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:passengerCar:remove")
    @Log(title = "客运汽车票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataPassengerCarService.deleteWithValidByIds(List.of(ids), true));
    }
}
