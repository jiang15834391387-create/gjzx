package org.smartlink.web.service.document.strategy;

import org.smartlink.web.domain.DataImageFilesInfo;

import java.io.IOException;
import java.util.List;

public interface DocumentStrategy {

    /**
     * 附件转换PDF
     * @param fileByte 源文件
     * @return pdf字节
     */
    byte[] documentToPdf(byte[] fileByte);
    /**
     * 附件转换图片
     * @param fileByte 源文件PDF格式/OFD格式
     * @return pdf字节
     */
    byte[] documentToImg(byte[] fileByte, int dpi) throws Exception;
    /**
     * 附件转换图片 只获取第一张
     * @param fileByte 源文件PDF格式
     * @return pdf字节
     */
    byte[] documentToImgOne(byte[] fileByte,int dpi) throws Exception;

    /**
     * 设置文件类型
     * @param dataImageFilesInfo 文件对象
     * @return 文件对象
     */
    DataImageFilesInfo setFileType(DataImageFilesInfo dataImageFilesInfo);

    /**
     * 附件转换图片并上传
     * @param fileByte 源文件PDF格式
     * @return pdf字节
     */
    List<byte[]> documentToImages(byte[] fileByte, int dpi) throws IOException;
}
