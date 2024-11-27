package org.smartlink.web.service.nc;

public interface IDataOcrInfoService {

    /**
     * 根据fileid删除ocr信息
     * @param fileId
     * @return
     */
    Boolean deleteByFileId(String fileId);
}
