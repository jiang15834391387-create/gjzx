package org.smartlink.business.invoice.controller;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.business.invoice.service.IDataOcrInfoServices;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrInfoBo;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/check/invoice")
public class CheckInvoiceController {

    private final IDataOcrInfoServices dataOcrInfoService;
    private final ICheckService service;

    public CheckInvoiceController(IDataOcrInfoServices dataOcrInfoService, ICheckService service) {
        this.dataOcrInfoService = dataOcrInfoService;
        this.service = service;
    }
    /**
     * 增值税专用发票修改
     * @param dto
     * @return
     */
    @PostMapping("/invoiceAlter")
    public R invoiceAlter(@RequestBody DataOcrInfoBo dto) throws IOException {
        return dataOcrInfoService.invoiceAlter(dto);
    }
    /**
     *
     *批量删除发票
     */
    @GetMapping("/remove")
    public R remove(@RequestParam(required = false) String[] ids) {
        return service.deleteWithValidByIds(List.of(ids));
    }



}
