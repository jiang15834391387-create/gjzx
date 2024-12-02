package org.smartlink.web.service.document.strategy.impl;


import org.smartlink.server.task.util.OfdUtils;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.service.document.strategy.DocumentStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author L
 * @title 附件处理
 * @description 附件处理
 * @date
 */
@Component
public class OFDStrategy implements DocumentStrategy {
    @Override
    public byte[] documentToPdf(byte[] fileByte) {
        return OfdUtils.ofdToPdf(fileByte);
    }

    @Override
    public byte[] documentToImg(byte[] fileByte, int dpi) throws Exception {
        return OfdUtils.documentToImg(fileByte);
    }

    @Override
    public byte[] documentToImgOne(byte[] fileByte,int dpi) throws Exception {
        return OfdUtils.ofdToJpgOne(fileByte);
    }

    @Override
    public DataImageFilesInfo setFileType(DataImageFilesInfo dataImageFilesInfo) {
        dataImageFilesInfo.setFileType(InvoiceConstants.DOCUMENT_OFD);
        return dataImageFilesInfo;
    }

    @Override
    public List<byte[]> documentToImages(byte[] byteOld, int dpi) {
        return null;
    }
}
