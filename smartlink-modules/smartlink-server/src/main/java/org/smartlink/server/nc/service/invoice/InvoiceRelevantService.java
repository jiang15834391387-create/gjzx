package org.smartlink.server.nc.service.invoice;


import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.invoice.bo.ManuallyCheckInvoiceDTO;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 发票相关接口
 *
 * @author 马旭辉
 */
public interface InvoiceRelevantService {

    /**
     * 手动查验
     *
     * @param dto {@link ManuallyCheckInvoiceDTO}
     * @return {@link R<String>}
     */
    R<String> manuallyCheckInvoice(@RequestBody ManuallyCheckInvoiceDTO dto) throws Exception;
}
