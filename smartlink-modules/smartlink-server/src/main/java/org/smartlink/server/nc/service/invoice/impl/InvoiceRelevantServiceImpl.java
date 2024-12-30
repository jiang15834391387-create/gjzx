package org.smartlink.server.nc.service.invoice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.invoice.bo.ManuallyCheckInvoiceDTO;
import org.smartlink.server.nc.service.invoice.InvoiceRelevantService;
import org.springframework.stereotype.Service;

/**
 * 发票相关实现
 *
 * @author maxuhui
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class InvoiceRelevantServiceImpl implements InvoiceRelevantService {
    @Override
    public R<String> manuallyCheckInvoice(ManuallyCheckInvoiceDTO dto) throws Exception {
        return null;
    }
}
