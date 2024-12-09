package org.smartlink.server.nc.domain.scan.dto;


import lombok.Data;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.invoice.DataOcrInfo;
import org.springframework.web.multipart.MultipartFile;

@Data
public class XmlInvoiceDTO {
    private MultipartFile file;
    private DataImageFilesInfo dataImageFilesInfo;
    private DataOcrInfo dataOcrInfo;
}
