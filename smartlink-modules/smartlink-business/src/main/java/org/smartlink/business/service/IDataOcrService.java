package org.smartlink.business.service;

import cn.hutool.core.util.StrUtil;

import org.apache.poi.ss.formula.functions.T;
import org.smartlink.common.core.domain.R;
import org.smartlink.common.mybatis.core.domain.BaseEntity;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.ocr.entity.IdentificationData;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
    R ocrInsert(String key, Object obj) throws Exception;




}
