package org.smartlink.common.check.controller;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.check.doman.BO.DataOcrInfoBo;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.service.IDataOcrInfoService;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/check/invoice")
public class CheckInvoiceController {

     private final IDataOcrInfoService dataOcrInfoService;

    public CheckInvoiceController(IDataOcrInfoService dataOcrInfoService) {
        this.dataOcrInfoService = dataOcrInfoService;
    }
    /**
     * 发票查验
     * @param invoiceCheckParamDTO
     * @return
     */
    @PostMapping("/result")
    public BaseEntity checkInvoice(@RequestBody InvoiceCheckParamDTO invoiceCheckParamDTO){
        return dataOcrInfoService.checkInvoice(invoiceCheckParamDTO);
    }

    /**
     * 增值税专用发票修改
     * @param dto
     * @return
     */
    @PostMapping("/invoiceAlter")
    public R invoiceAlter(@RequestBody DataOcrInfoBo dto){
        return dataOcrInfoService.invoiceAlter(dto);
    }
}
