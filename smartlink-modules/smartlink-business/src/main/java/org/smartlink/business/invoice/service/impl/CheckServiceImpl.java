package org.smartlink.business.invoice.service.impl;

import org.smartlink.business.invoice.check.CheckInvoice;
import org.smartlink.business.invoice.service.ICheckService;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Service
public class CheckServiceImpl implements ICheckService {
    private final CheckInvoice invoice;

    public CheckServiceImpl(CheckInvoice invoice) {
        this.invoice = invoice;
    }


}
