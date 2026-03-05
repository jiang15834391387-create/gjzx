// 路径: org.smartlink.workflow.controller.WfInventoryLedgerController.java
package org.smartlink.workflow.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.excel.utils.ExcelUtil;
import org.smartlink.common.log.annotation.Log;
import org.smartlink.common.log.enums.BusinessType;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.web.core.BaseController;
import org.smartlink.workflow.domain.WfInventoryLedger;
import org.smartlink.workflow.domain.vo.WfInventoryLedgerVo;
import org.smartlink.workflow.service.IWfInventoryLedgerService;
import org.smartlink.workflow.utils.pdf.PdfUtil; // 你的 PdfUtil 工具类
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/workflow/inventoryLedger")
public class WfInventoryLedgerController extends BaseController {

    private final IWfInventoryLedgerService ledgerService;

    /**
     * 查询出入库台账记录
     */
    @GetMapping("/list")
    public TableDataInfo<WfInventoryLedgerVo> list(WfInventoryLedger bo, PageQuery pageQuery) {
        return ledgerService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出出入库台账记录 (Excel)
     */
    @Log(title = "台账管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(WfInventoryLedger bo, HttpServletResponse response) {
        List<WfInventoryLedgerVo> list = ledgerService.queryList(bo);
        if(CollectionUtils.isNotEmpty( list)){
            list.forEach(item -> {
                Integer operationType = item.getOperationType();
                if(operationType!=null){
                    if(operationType==1){
                        item.setOperationTypeName("入库");
                    }else if(operationType==2){
                        item.setOperationTypeName("出库");
                    }

                }
            });
        }

        ExcelUtil.exportExcel(list, "出入库台账数据", WfInventoryLedgerVo.class, response);
    }

    /**
     * 仿造审批记录格式化导出 PDF
     */
    @Log(title = "台账管理", businessType = BusinessType.EXPORT)
    @PostMapping("/exportPdf")
    public void exportPdf(@RequestBody ArrayList<Long> ids, HttpServletResponse response) throws Exception {
        // 1. 根据选中 ID 查询台账数据
        List<WfInventoryLedgerVo> data = ledgerService.queryListByIds(ids);

        // 2. 设置响应头
        String fileName = "出入库台账记录.pdf";
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
            "attachment; filename*=UTF-8''" + java.net.URLEncoder.encode(fileName, "UTF-8"));

        // 3. 调用工具类生成 PDF
        PdfUtil.exportInventoryLedgerPdf(data, response.getOutputStream());
    }
}
