package org.smartlink.server.nc.service.scan;

import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageFilesInfoVo;
import org.smartlink.server.nc.domain.scan.dto.FileUploadDTO;
import org.smartlink.server.nc.domain.scan.dto.XmlInvoiceDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ScanImageService {

    /**
     * 文件上传
     *
     * @param fileUploadDTO 文件信息
     * @param dataImageFilesInfo 图片文件对象
     * @param multipartFile 文件
     * @return 图片vo
     */
    DataImageFilesInfoVo uploadImage(FileUploadDTO fileUploadDTO, DataImageFilesInfo dataImageFilesInfo, MultipartFile multipartFile) throws Exception;

    /**
     * 文件上传
     *
     * @param fileUploadDTO 文件信息
     * @param dataImageFilesInfo 图片文件对象
     * @param file 文件
     * @return 图片vo
     */
    DataImageFilesInfoVo saveDocuments(FileUploadDTO fileUploadDTO,DataImageFilesInfo dataImageFilesInfo, MultipartFile file) throws Exception;

    /**
     * 推送附件逻辑方法
     * @param dataImageFilesInfo 图片对象
     * @param businessSerialNo 单据流水号
     */
    public void pushFileDataToBusinessSystem(DataImageFilesInfo dataImageFilesInfo,String businessSerialNo);

    /**
     * xml格式文件校验
     * */
    XmlInvoiceDTO xmlEInvoice(MultipartFile file, FileUploadDTO dto, DataImageFilesInfo imageFilesInfo)throws Exception;
}
