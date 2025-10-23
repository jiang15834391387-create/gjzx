package org.smartlink.common.entity.domain.business.service;

import org.smartlink.common.entity.domain.business.domain.DataUsedCarSales;
import org.smartlink.common.entity.domain.business.domain.OtherAttachments;
import org.smartlink.common.entity.domain.business.domain.bo.DataUsedCarSalesBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataUsedCarSalesVo;

import java.util.List;

public interface IOtherAttachmentsService {

    /**
     * 新增其他附件
     *
     */
    Boolean insert(OtherAttachments otherAttachments);

    /**
     * 查询其他附件
     *
     * @param otherAttachments 查询条件
     * @return 二手车销售统一发票列表
     */
    List<OtherAttachments> queryList(OtherAttachments otherAttachments);
}
