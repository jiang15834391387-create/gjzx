package org.smartlink.web.utils.file;

/**
 * 媒体类型工具类
 *
 * @author L
 */
public class MimeTypeUtils
{
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    /**
     * 所有支持的图片类型
     */
    public static final String[] IMAGE_EXTENSION = { "AVIF","WMF","EMF","JPEG","FPX","BMP","GIF","SVG","ICO","PNG","JPG","TIF","TIFF" };
    /**
     * 需要转换格式的图片类型
     */
    public static final String[] IMAGE_TRANS_EXTENSION={"AVIF","WMF","EMF","FPX","BMP","GIF","SVG","ICO","TIF","TIFF"};
    /**
     * 转换图片类型为jpg类型
     */
    public static final String[] IMAGE_EXTENSION_TRANSFORMATION={"TIF","TIFF"};
    /**
     * pdf
     */
    public static final String[] DOCUMENT_PDF_TRANSFOTMATION={"PDF"};
    /**
     * ofd
     */
    public static final String[] DOCUMENT_OFD_TRANSFOTMATION={"OFD"};
    /**
     * xml
     */
    public static final String[] DOCUMENT_XML_TRANSFOTMATION={"XML"};
    /**
     * word
     */
    public static final String[] DOCUMENT_WORD_TRANSFOTMATION={"doc","docm","docx","dotm","dotm","dot","dotx"};
    /**
     * excel
     */
    public static final String[] DOCUMENT_EXCEL_TRANSFOTMATION={"xls","xla","xlsb","xlsm","xlsx","xltx","csv"};
    /**
     * ppt
     */
    public static final String[] DOCUMENT_PPT_TRANSFOTMATION={"ppt", "pptx"};

    /**
     * pres 压缩
     */
    public static final String[] DOCUMENT_PRES_TRANSFOTMATION={"rar", "zip","arj","z","lzh","jar","7z","apz","ar","bz","car","dar","cpgz","f","ha","hbc","hbc2","hbe","hpk","hyp"};

    /**
     * other
     */
    public static final String[] DOCUMENT_OTHER_TRANSFOTMATION={"txt"};
}
