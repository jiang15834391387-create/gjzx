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
import org.smartlink.business.domain.vo.DataAirlineRulesInfoVo;
import org.smartlink.business.domain.bo.DataAirlineRulesInfoBo;
import org.smartlink.business.service.IDataAirlineRulesInfoService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 飞机票仓位信息
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/airlineRulesInfo")
public class DataAirlineRulesInfoController extends BaseController {

    private final IDataAirlineRulesInfoService dataAirlineRulesInfoService;

    /**
     * 查询飞机票仓位信息列表
     */
    @SaCheckPermission("business:airlineRulesInfo:list")
    @GetMapping("/list")
    public TableDataInfo<DataAirlineRulesInfoVo> list(DataAirlineRulesInfoBo bo, PageQuery pageQuery) {
        return dataAirlineRulesInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出飞机票仓位信息列表
     */
    @SaCheckPermission("business:airlineRulesInfo:export")
    @Log(title = "飞机票仓位信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataAirlineRulesInfoBo bo, HttpServletResponse response) {
        List<DataAirlineRulesInfoVo> list = dataAirlineRulesInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "飞机票仓位信息", DataAirlineRulesInfoVo.class, response);
    }

    /**
     * 获取飞机票仓位信息详细信息
     *
     * @param airlineId 主键
     */
    @SaCheckPermission("business:airlineRulesInfo:query")
    @GetMapping("/{airlineId}")
    public R<DataAirlineRulesInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long airlineId) {
        return R.ok(dataAirlineRulesInfoService.queryById(airlineId));
    }

    /**
     * 新增飞机票仓位信息
     */
    @SaCheckPermission("business:airlineRulesInfo:add")
    @Log(title = "飞机票仓位信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataAirlineRulesInfoBo bo) {
        return toAjax(dataAirlineRulesInfoService.insertByBo(bo));
    }

    /**
     * 修改飞机票仓位信息
     */
    @SaCheckPermission("business:airlineRulesInfo:edit")
    @Log(title = "飞机票仓位信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataAirlineRulesInfoBo bo) {
        return toAjax(dataAirlineRulesInfoService.updateByBo(bo));
    }

    /**
     * 删除飞机票仓位信息
     *
     * @param airlineIds 主键串
     */
    @SaCheckPermission("business:airlineRulesInfo:remove")
    @Log(title = "飞机票仓位信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{airlineIds}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] airlineIds) {
        return toAjax(dataAirlineRulesInfoService.deleteWithValidByIds(List.of(airlineIds), true));
    }
}
