package org.smartlink.server.nc.service.nc;

import org.smartlink.common.core.domain.R;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.modle.BaseEntity;

import java.util.List;
import java.util.Map;

public interface IDataOcrService {

    /**
     * 全票种查询
     *
     * @param map       查询参数 type 发票类型
     * @param pageQuery 分页参数
     * @return 发票分页内容
     */
    TableDataInfo ocrQuery(Map<String, Object> map, PageQuery pageQuery) throws Exception;

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
     * 发票转图
     *
     * @param fileId 文件ID
     * @return 成功或者失败
     */
    R<Void> ocrConvertToPicture(String fileId) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

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

    R<Void> ocrUpdateByBaseEntity(String fileType, BaseEntity t) throws ClassNotFoundException;

    List<BaseEntity> multipleOcrQueryByFileId(String fileId) throws ClassNotFoundException, IllegalAccessException, InstantiationException;
}
