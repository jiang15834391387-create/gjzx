package org.smartlink.business.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.smartlink.common.entity.domain.business.domain.bo.DataRailwayTicketBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataRailwayTicketVo;
import org.smartlink.common.entity.domain.business.service.IDataRailwayTicketService;
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
 * 火车票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/railwayTicket")
public class DataRailwayTicketController extends BaseController {

    private final IDataRailwayTicketService dataRailwayTicketService;

    /**
     * 查询火车票列表
     */
    @SaCheckPermission("business:railwayTicket:list")
    @GetMapping("/list")
    public TableDataInfo<DataRailwayTicketVo> list(DataRailwayTicketBo bo, PageQuery pageQuery) {
        return dataRailwayTicketService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出火车票列表
     */
    @SaCheckPermission("business:railwayTicket:export")
    @Log(title = "火车票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataRailwayTicketBo bo, HttpServletResponse response) {
        List<DataRailwayTicketVo> list = dataRailwayTicketService.queryList(bo);
        ExcelUtil.exportExcel(list, "火车票", DataRailwayTicketVo.class, response);
    }

    /**
     * 获取火车票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:railwayTicket:query")
    @GetMapping("/{id}")
    public R<DataRailwayTicketVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataRailwayTicketService.queryById(id));
    }

    /**
     * 新增火车票
     */
    @SaCheckPermission("business:railwayTicket:add")
    @Log(title = "火车票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataRailwayTicketBo bo) {
        return toAjax(dataRailwayTicketService.insertByBo(bo));
    }

    /**
     * 修改火车票
     */
    @SaCheckPermission("business:railwayTicket:edit")
    @Log(title = "火车票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataRailwayTicketBo bo) {
        return toAjax(dataRailwayTicketService.updateByBo(bo));
    }

    /**
     * 删除火车票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:railwayTicket:remove")
    @Log(title = "火车票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataRailwayTicketService.deleteWithValidByIds(List.of(ids), true));
    }
}
