package org.smartlink.business.invoice.service;

import org.smartlink.common.core.domain.R;

import java.util.Collection;
//发票业务接口
public interface ICheckService {
    R deleteWithValidByIds(Collection<String> ids);

}
