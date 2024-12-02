package org.smartlink.web.service.document.strategy.impl;


import org.smartlink.server.task.util.WordToPdf;
import org.smartlink.web.constant.InvoiceConstants;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.service.document.DocumentFactory;
import org.smartlink.web.service.document.strategy.DocumentStrategy;
import org.smartlink.web.utils.document.PDFUtil;
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
public class WordStrategy implements DocumentStrategy {
    @Override
    public byte[] documentToPdf(byte[] fileByte) {
        return WordToPdf.wordToPdf(fileByte);
    }

    @Override
    public byte[] documentToImg(byte[] fileByte, int dpi) {
        return fileByte;
    }

    @Override
    public byte[] documentToImgOne(byte[] fileByte,int dpi) throws IOException {
        return PDFUtil.PDFToImg(fileByte,dpi);
    }

    @Override
    public DataImageFilesInfo setFileType(DataImageFilesInfo dataImageFilesInfo) {
        dataImageFilesInfo.setFileType(InvoiceConstants.DOCUMENT_WORD);
        return dataImageFilesInfo;
    }

    @Override
    public List<byte[]> documentToImages(byte[] byteOld, int dpi) throws IOException {
        //附件转pdf
        byte[] bytes = DocumentFactory.instance("doc").documentToPdf(byteOld);
        //pdf转图片
        return PDFUtil.convertPdfImages(bytes,100);
    }
}
