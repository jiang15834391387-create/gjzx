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
 * @author 马旭辉
 */
@Slf4j
@Component
public class YesfpCheckStrategy extends AbstractCheckStrategy {
    @Override
    public BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws Exception {
        return null;
    }
}
