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
import org.smartlink.business.domain.vo.DataCustomsSpecialPaymentVo;
import org.smartlink.business.domain.bo.DataCustomsSpecialPaymentBo;
import org.smartlink.business.service.IDataCustomsSpecialPaymentService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 海关专用缴款书
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/customsSpecialPayment")
public class DataCustomsSpecialPaymentController extends BaseController {

    private final IDataCustomsSpecialPaymentService dataCustomsSpecialPaymentService;

    /**
     * 查询海关专用缴款书列表
     */
    @SaCheckPermission("business:customsSpecialPayment:list")
    @GetMapping("/list")
    public TableDataInfo<DataCustomsSpecialPaymentVo> list(DataCustomsSpecialPaymentBo bo, PageQuery pageQuery) {
        return dataCustomsSpecialPaymentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出海关专用缴款书列表
     */
    @SaCheckPermission("business:customsSpecialPayment:export")
    @Log(title = "海关专用缴款书", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataCustomsSpecialPaymentBo bo, HttpServletResponse response) {
        List<DataCustomsSpecialPaymentVo> list = dataCustomsSpecialPaymentService.queryList(bo);
        ExcelUtil.exportExcel(list, "海关专用缴款书", DataCustomsSpecialPaymentVo.class, response);
    }

    /**
     * 获取海关专用缴款书详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:customsSpecialPayment:query")
    @GetMapping("/{id}")
    public R<DataCustomsSpecialPaymentVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataCustomsSpecialPaymentService.queryById(id));
    }

    /**
     * 新增海关专用缴款书
     */
    @SaCheckPermission("business:customsSpecialPayment:add")
    @Log(title = "海关专用缴款书", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataCustomsSpecialPaymentBo bo) {
        return toAjax(dataCustomsSpecialPaymentService.insertByBo(bo));
    }

    /**
     * 修改海关专用缴款书
     */
    @SaCheckPermission("business:customsSpecialPayment:edit")
    @Log(title = "海关专用缴款书", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataCustomsSpecialPaymentBo bo) {
        return toAjax(dataCustomsSpecialPaymentService.updateByBo(bo));
    }

    /**
     * 删除海关专用缴款书
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:customsSpecialPayment:remove")
    @Log(title = "海关专用缴款书", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataCustomsSpecialPaymentService.deleteWithValidByIds(List.of(ids), true));
    }
}
