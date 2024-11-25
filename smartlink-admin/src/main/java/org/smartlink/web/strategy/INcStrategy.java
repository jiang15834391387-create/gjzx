package org.smartlink.web.strategy;

import org.smartlink.web.domain.dto.NcDeleteServiceDTO;

public interface INcStrategy {
    /**
     * NC业务删除发票逻辑
     * @param ncDeleteServiceDTO
     */
    void deleteNcInvoiceDataBusinessService(NcDeleteServiceDTO ncDeleteServiceDTO) throws Exception;
}
