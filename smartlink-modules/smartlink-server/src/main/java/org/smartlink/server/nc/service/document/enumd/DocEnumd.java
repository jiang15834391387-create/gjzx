package org.smartlink.server.nc.service.document.enumd;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.smartlink.server.nc.service.document.strategy.impl.*;
import org.smartlink.server.nc.utils.file.MimeTypeUtils;


/**
 * 附件处理枚举
 * @author L
 */
@Getter
@AllArgsConstructor
public enum DocEnumd {

    /**
     * EXCEL
     */
    EXCEL(MimeTypeUtils.DOCUMENT_EXCEL_TRANSFOTMATION, ExcelStrategy.class),

    /**
     * WORD
     */
    WORD(MimeTypeUtils.DOCUMENT_WORD_TRANSFOTMATION, WordStrategy.class),

    /**
     * PPT
     */
    PPT(MimeTypeUtils.DOCUMENT_PPT_TRANSFOTMATION, PPTStrategy.class),

    /**
     * PDF
     */
    PDF(MimeTypeUtils.DOCUMENT_PDF_TRANSFOTMATION, PDFStrategy.class),

    /**
     * OFD
     */
    OFD(MimeTypeUtils.DOCUMENT_OFD_TRANSFOTMATION, OFDStrategy.class),

    /**
     * PRES 压缩文件
     */
    PRES(MimeTypeUtils.DOCUMENT_PRES_TRANSFOTMATION, PresStrategy.class),

    /**
     * OTHER
     */
    TXT(MimeTypeUtils.DOCUMENT_OTHER_TRANSFOTMATION, OtherStrategy.class);

    private final String[] value;

    private final Class<?> beanClass;

    public static DocEnumd find(String value) {
        for (DocEnumd enumd : values()) {
            for (String s : enumd.getValue()) {
                if (s.equalsIgnoreCase(value)){
                    return enumd;
                }
            }
        }
        return null;
    }
}
