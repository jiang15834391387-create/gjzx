package org.smartlink.business.invoice.controller;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.check.doman.InvoiceRequest;
import org.smartlink.common.core.domain.R;
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
        return toAjax(service.invoiceAlter(request));
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


}
