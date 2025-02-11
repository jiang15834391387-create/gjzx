package org.smartlink.common.core.utils.file;

/**
 * 媒体类型工具类
 *
 * @author smartlink
 */
public class MimeTypeUtils {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = {"bmp", "gif", "jpg", "jpeg", "png"};

    public static final String[] FLASH_EXTENSION = {"swf", "flv"};

    public static final String[] MEDIA_EXTENSION = {"swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
        "asf", "rm", "rmvb"};

    /**
     * 需要转换格式的图片类型
     */
    public static final String[] IMAGE_TRANS_EXTENSION={"AVIF","WMF","EMF","FPX","BMP","GIF","SVG","ICO","TIF","TIFF"};

    /**
     * 转换图片类型为jpg类型
     */
    public static final String[] IMAGE_EXTENSION_TRANSFORMATION={"TIF","TIFF"};

    public static final String[] VIDEO_EXTENSION = {"mp4", "avi", "rmvb"};

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
        // 图片
        "bmp", "gif", "jpg", "jpeg", "png",
        // word excel powerpoint
        "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
        // 压缩文件
        "rar", "zip", "gz", "bz2",
        // 视频格式
        "mp4", "avi", "rmvb",
        // pdf
        "pdf"};

}
