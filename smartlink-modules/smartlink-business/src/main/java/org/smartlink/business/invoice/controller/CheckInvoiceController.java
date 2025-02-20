package org.smartlink.business.invoice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.check.doman.InvoicePageQuery;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.check.doman.vo.InvoiceVo;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.response.DataResponseDTO;
import org.smartlink.common.web.core.BaseController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/check/invoice")
public class CheckInvoiceController extends BaseController {

    private final ICheckService service;

    public CheckInvoiceController(ICheckService service) {
        this.service = service;
    }
    /**
     * 发票修改
     */
    @PostMapping("/updateInvoice")
    public R<Void> updateInvoice(@RequestBody InvoiceRequest request) throws Exception {
        return service.invoiceAlter(request);
    }
    /**
     *
     *批量删除发票
     */
    @GetMapping("/remove")
    public R<Void> remove(@RequestParam(required = false) String[] ids) {
        return toAjax(service.deleteWithValidByIds(List.of(ids)));
    }
    /**
     *发票列表查询
     *
     */
    @PostMapping("/selectPage")
    public Page<InvoiceVo> getSelectInvoices(@RequestBody InvoicePageQuery pageQuery) throws Exception {
        return service.getInvoicePage(pageQuery);
    }
    /**
     *发票详情查询
     *
     */
    @GetMapping("/invoiceDetail")
    public R<DataResponseDTO> selectInvoiceDetail(@RequestParam(value = "fileId")  @NotBlank String fileId){
        return service.selectInvoiceDetail(fileId);
    }
}
