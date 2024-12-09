package org.smartlink.server.nc.service.document.strategy.impl;


import org.smartlink.server.nc.constant.InvoiceConstants;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.service.document.strategy.DocumentStrategy;
import org.smartlink.server.nc.utils.document.PDFUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author L
 * @title 附件处理
 * @description 附件处理
 * @date
 */
@Component
public class PDFStrategy implements DocumentStrategy {
    @Override
    public byte[] documentToPdf(byte[] fileByte) {
        return fileByte;
    }

    @Override
    public byte[] documentToImg(byte[] fileByte, int dpi) throws Exception {
        return PDFUtil.convertPdfImage(fileByte,dpi);
    }

    @Override
    public byte[] documentToImgOne(byte[] fileByte,int dpi) throws IOException {
        return PDFUtil.PDFToImg(fileByte,dpi);
    }

    @Override
    public DataImageFilesInfo setFileType(DataImageFilesInfo dataImageFilesInfo) {
        dataImageFilesInfo.setFileType(InvoiceConstants.DOCUMENT_PDF);
        return dataImageFilesInfo;
    }

    @Override
    public List<byte[]> documentToImages(byte[] byteOld,int dpi) throws IOException {
        return PDFUtil.convertPdfImages(byteOld,dpi);
    }

}
