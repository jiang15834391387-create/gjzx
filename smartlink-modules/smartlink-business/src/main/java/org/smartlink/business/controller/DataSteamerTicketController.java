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
import org.smartlink.business.domain.vo.DataSteamerTicketVo;
import org.smartlink.business.domain.bo.DataSteamerTicketBo;
import org.smartlink.business.service.IDataSteamerTicketService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 船票
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/steamerTicket")
public class DataSteamerTicketController extends BaseController {

    private final IDataSteamerTicketService dataSteamerTicketService;

    /**
     * 查询船票列表
     */
    @SaCheckPermission("business:steamerTicket:list")
    @GetMapping("/list")
    public TableDataInfo<DataSteamerTicketVo> list(DataSteamerTicketBo bo, PageQuery pageQuery) {
        return dataSteamerTicketService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出船票列表
     */
    @SaCheckPermission("business:steamerTicket:export")
    @Log(title = "船票", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataSteamerTicketBo bo, HttpServletResponse response) {
        List<DataSteamerTicketVo> list = dataSteamerTicketService.queryList(bo);
        ExcelUtil.exportExcel(list, "船票", DataSteamerTicketVo.class, response);
    }

    /**
     * 获取船票详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:steamerTicket:query")
    @GetMapping("/{id}")
    public R<DataSteamerTicketVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataSteamerTicketService.queryById(id));
    }

    /**
     * 新增船票
     */
    @SaCheckPermission("business:steamerTicket:add")
    @Log(title = "船票", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataSteamerTicketBo bo) {
        return toAjax(dataSteamerTicketService.insertByBo(bo));
    }

    /**
     * 修改船票
     */
    @SaCheckPermission("business:steamerTicket:edit")
    @Log(title = "船票", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataSteamerTicketBo bo) {
        return toAjax(dataSteamerTicketService.updateByBo(bo));
    }

    /**
     * 删除船票
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:steamerTicket:remove")
    @Log(title = "船票", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataSteamerTicketService.deleteWithValidByIds(List.of(ids), true));
    }
}
