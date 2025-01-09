package org.smartlink.common.check.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.common.check.invoice.DataImageFilesInfo;
import org.smartlink.common.check.doman.InvoiceBaseEntity;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.service.abstractd.AbstractCheckStrategy;
import org.springframework.stereotype.Component;

/**
 * 睿真查验
 *
 */
@Slf4j
@Component
public class RuiZhenCheckStrategy extends AbstractCheckStrategy {


    @Override
    public InvoiceBaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) {
        return null;
    }
}
