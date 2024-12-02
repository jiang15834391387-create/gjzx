package org.smartlink.web.service.nc;

import org.smartlink.web.domain.invoice.DataOcrInfo;

public interface IDataOcrInfoService {

    /**
     * 根据fileid删除ocr信息
     * @param fileId
     * @return
     */
    Boolean deleteByFileId(String fileId);

    DataOcrInfo getByFileId(String FileId);

    Boolean insert(DataOcrInfo ocrInfo);
}
