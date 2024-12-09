package org.smartlink.server.nc.invoice.service;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.dto.InvoiceCheckParamDTO;
import org.smartlink.server.nc.domain.modle.BaseEntity;

/**
 * 查验策略
 *
 * @author 马旭辉
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
    BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws Exception;
}
