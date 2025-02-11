package org.smartlink.business.service;



import org.apache.poi.ss.formula.functions.T;
import org.smartlink.common.core.domain.R;


/**
 * @author Zhang Shuai
 */
public interface IDataOcrService {

    /**
     * ocr信息插入或保存  （插入会根据FileID修改文件表的type为传入type，修改根据ID修改）
     *
     * @param key        发票类型
     * @param obj 发票实体
     * @return 0 失败 1 成功
     */
    R<T> ocrInsert(String key, Object obj) throws Exception;




}
