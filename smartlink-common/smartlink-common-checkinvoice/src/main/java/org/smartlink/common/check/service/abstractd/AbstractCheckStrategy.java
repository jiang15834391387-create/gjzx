package org.smartlink.common.check.service.abstractd;


import org.smartlink.common.check.invoice.DataImageFilesInfo;
import org.smartlink.common.check.doman.dto.InvoiceCheckParamDTO;
import org.smartlink.common.check.service.ICheckStrategy;
import org.smartlink.common.check.properties.CheckProperties;
import org.smartlink.common.mybatis.core.domain.BaseEntity;

/**
 * 查验策略：睿真、航信
 *
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
    public abstract BaseEntity checkInvoke(DataImageFilesInfo filesInfo, InvoiceCheckParamDTO dto);
}
