package org.smartlink.web.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.web.domain.DataImageFilesInfo;
import org.smartlink.web.domain.modle.BaseEntity;

import java.util.List;
import java.util.Map;

public interface IDataOcrService {

    /**
     * 删除多个
     *
     * @param fileIds 文件ID数组
     * @return String
     */
    R<String> deleteMultipleFile(List<String> fileIds) throws Exception;

    /**
     * 删除ocr信息
     *
     * @param type   发票类型
     * @param fileId 文件id
     */

    void deleteInvoiceDataByType(String type, String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 删除ocr信息
     *
     * @param baseEntity 发票实体
     * @param fileId     文件id
     */

    void deleteInvoiceData(BaseEntity baseEntity, String fileId);

    /**
     * 根据文件ID查询发票信息(仅供删除使用)
     *
     * @return 发票内容
     */
    List<BaseEntity> ocrQueryByFileIdAll(DataImageFilesInfo filesInfo) throws ClassNotFoundException, InstantiationException, IllegalAccessException ;

    R<Void> ocrInsertBaseEntity(String k, BaseEntity t) throws ClassNotFoundException;

    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param key        发票类型
     * @param baseEntity 发票实体
     * @return 0 失败 1 成功
     */
    int ocrInsertOrUpdateByBaseEntity(String key, BaseEntity baseEntity) throws Exception;
    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param map 查询参数  fileId 文件Id , type 发票类型 ,operate 操作类型
     * @return 插入或保存结果
     */
    R<Void> ocrInsertOrUpdate(Map<String, Object> map) throws Exception;

    /**
     * 根据fileId和type查询发票表信息
     * @param invoiceType
     * @param fileId
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    List<BaseEntity> queryInvoiceInfoByTypeAndFileId(String invoiceType,String fileId) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

    /**
     * 根据文件ID查询发票信息
     *
     * @param fileId 文件Id
     * @return 发票内容
     */
    BaseEntity ocrQueryByFileId(String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException;

}
