package org.smartlink.server.nc.invoice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.dto.InvoiceCheckParamDTO;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.invoice.service.abstractd.AbstractCheckStrategy;
import org.springframework.stereotype.Component;

/**
 * 航信查验
 *
 * @author L
 */
@Slf4j
@Component
public class HangXinCheckStrategy extends AbstractCheckStrategy {
    @Override
    public BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws Exception {
        return null;
    }
}
