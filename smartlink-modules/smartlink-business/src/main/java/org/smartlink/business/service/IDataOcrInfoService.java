package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataOcrInfoVo;
import org.smartlink.business.domain.bo.DataOcrInfoBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * ocr信息Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataOcrInfoService {

    /**
     * 查询ocr信息
     *
     * @param id 主键
     * @return ocr信息
     */
    DataOcrInfoVo queryById(String id);

    /**
     * 分页查询ocr信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ocr信息分页列表
     */
    TableDataInfo<DataOcrInfoVo> queryPageList(DataOcrInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的ocr信息列表
     *
     * @param bo 查询条件
     * @return ocr信息列表
     */
    List<DataOcrInfoVo> queryList(DataOcrInfoBo bo);

    /**
     * 新增ocr信息
     *
     * @param bo ocr信息
     * @return 是否新增成功
     */
    Boolean insertByBo(DataOcrInfoBo bo);

    /**
     * 修改ocr信息
     *
     * @param bo ocr信息
     * @return 是否修改成功
     */
    Boolean updateByBo(DataOcrInfoBo bo);

    /**
     * 校验并批量删除ocr信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
