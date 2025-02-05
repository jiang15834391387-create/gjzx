package org.smartlink.business.invoice.service;

import org.smartlink.common.check.doman.InvoiceRequest;

import java.util.Collection;
//发票夹业务接口
public interface ICheckService {
    int deleteWithValidByIds(Collection<String> ids);

    int invoiceAlter(InvoiceRequest request) throws Exception;
}
