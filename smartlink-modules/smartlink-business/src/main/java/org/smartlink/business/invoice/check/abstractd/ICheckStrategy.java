package org.smartlink.business.invoice.check.abstractd;


import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

import java.io.IOException;


/**
 * 查验策略
 */
public interface ICheckStrategy {

    /**
     * 查验发票
     * 如果查验成功则返回OCR信息，查验失败返回NULL
     * 注意：此方法不会对数据库进行任何操作，包括但不限于删除OCR信息、修改OCR信息等操作
     *  filesInfo 改变 FileType、FileStatus
     *
     * @param filesInfo {@link DataImageFilesInfo}
     * @param dto       {@link InvoiceCheckParamDTO }
     * @return baseEntity {@link BaseEntity}
     */
    BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws IOException;
}
