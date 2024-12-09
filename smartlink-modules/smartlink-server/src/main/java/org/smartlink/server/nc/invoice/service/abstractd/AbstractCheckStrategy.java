package org.smartlink.server.nc.invoice.service.abstractd;


import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.dto.InvoiceCheckParamDTO;
import org.smartlink.server.nc.domain.modle.BaseEntity;
import org.smartlink.server.nc.invoice.properties.CheckProperties;
import org.smartlink.server.nc.invoice.service.ICheckStrategy;

/**
 * 查验策略：支持百望、航信
 *
 * @author 马旭辉
 */
public abstract class AbstractCheckStrategy implements ICheckStrategy {

    protected CheckProperties properties;

    public boolean isInit = false;

    public void init(CheckProperties properties) {
        this.properties = properties;
    }

    /**
     * 发票查验
     * 如果查验成功则返回OCR信息，查询失败返回NULL
     *
     * @param filesInfo {@link  DataImageFilesInfo}
     * @param dto       {@link InvoiceCheckParamDTO }
     * @return baseEntity {@link BaseEntity}
     */
    @Override
    public abstract BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto) throws Exception;
}
