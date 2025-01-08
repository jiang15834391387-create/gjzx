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
import org.smartlink.business.domain.vo.DataElectronicTransportationGoodsVo;
import org.smartlink.business.domain.bo.DataElectronicTransportationGoodsBo;
import org.smartlink.business.service.IDataElectronicTransportationGoodsService;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

/**
 * 货物运输电子收款凭证
 *
 * @author Lion Li
 * @date 2025-01-08
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/electronicTransportationGoods")
public class DataElectronicTransportationGoodsController extends BaseController {

    private final IDataElectronicTransportationGoodsService dataElectronicTransportationGoodsService;

    /**
     * 查询货物运输电子收款凭证列表
     */
    @SaCheckPermission("business:electronicTransportationGoods:list")
    @GetMapping("/list")
    public TableDataInfo<DataElectronicTransportationGoodsVo> list(DataElectronicTransportationGoodsBo bo, PageQuery pageQuery) {
        return dataElectronicTransportationGoodsService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出货物运输电子收款凭证列表
     */
    @SaCheckPermission("business:electronicTransportationGoods:export")
    @Log(title = "货物运输电子收款凭证", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(DataElectronicTransportationGoodsBo bo, HttpServletResponse response) {
        List<DataElectronicTransportationGoodsVo> list = dataElectronicTransportationGoodsService.queryList(bo);
        ExcelUtil.exportExcel(list, "货物运输电子收款凭证", DataElectronicTransportationGoodsVo.class, response);
    }

    /**
     * 获取货物运输电子收款凭证详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("business:electronicTransportationGoods:query")
    @GetMapping("/{id}")
    public R<DataElectronicTransportationGoodsVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable String id) {
        return R.ok(dataElectronicTransportationGoodsService.queryById(id));
    }

    /**
     * 新增货物运输电子收款凭证
     */
    @SaCheckPermission("business:electronicTransportationGoods:add")
    @Log(title = "货物运输电子收款凭证", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DataElectronicTransportationGoodsBo bo) {
        return toAjax(dataElectronicTransportationGoodsService.insertByBo(bo));
    }

    /**
     * 修改货物运输电子收款凭证
     */
    @SaCheckPermission("business:electronicTransportationGoods:edit")
    @Log(title = "货物运输电子收款凭证", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DataElectronicTransportationGoodsBo bo) {
        return toAjax(dataElectronicTransportationGoodsService.updateByBo(bo));
    }

    /**
     * 删除货物运输电子收款凭证
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:electronicTransportationGoods:remove")
    @Log(title = "货物运输电子收款凭证", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable String[] ids) {
        return toAjax(dataElectronicTransportationGoodsService.deleteWithValidByIds(List.of(ids), true));
    }
}
