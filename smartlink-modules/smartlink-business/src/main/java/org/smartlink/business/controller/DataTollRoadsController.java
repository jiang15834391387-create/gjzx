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
import org.smartlink.business.domain.vo.DataTollRoadsVo;
import org.smartlink.business.domain.bo.DataTollRoadsBo;
import org.smartlink.business.service.IDataTollRoadsService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 过路费
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/tollRoads")
public class DataTollRoadsController extends BaseController {

    private final IDataTollRoadsService dataTollRoadsService;

    /**
     * 查询过路费列表
     */
    @SaCheckPermission("business:tollRoads:list")
    @GetMapping("/list")
    public TableDataInfo<DataTollRoadsVo> list(DataTollRoadsBo bo, PageQuery pageQuery) {
        return dataTollRoadsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出过路费列表
     */
    @SaCheckPermission("business:tollRoads:export")
    @Log(title = "过路费", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataTollRoadsBo bo, HttpServletResponse response) {
        List<DataTollRoadsVo> list = dataTollRoadsService.queryList(bo);
        ExcelUtil.exportExcel(list, "过路费", DataTollRoadsVo.class, response);
    }

    /**
     * 获取过路费详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:tollRoads:query")
    @GetMapping("/{id}")
    public R<DataTollRoadsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataTollRoadsService.queryById(id));
    }

    /**
     * 新增过路费
     */
    @SaCheckPermission("business:tollRoads:add")
    @Log(title = "过路费", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataTollRoadsBo bo) {
        return toAjax(dataTollRoadsService.insertByBo(bo));
    }

    /**
     * 修改过路费
     */
    @SaCheckPermission("business:tollRoads:edit")
    @Log(title = "过路费", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataTollRoadsBo bo) {
        return toAjax(dataTollRoadsService.updateByBo(bo));
    }

    /**
     * 删除过路费
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:tollRoads:remove")
    @Log(title = "过路费", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataTollRoadsService.deleteWithValidByIds(List.of(ids), true));
    }
}
