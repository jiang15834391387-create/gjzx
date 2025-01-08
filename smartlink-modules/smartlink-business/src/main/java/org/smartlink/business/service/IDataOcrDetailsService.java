package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataOcrDetailsVo;
import org.smartlink.business.domain.bo.DataOcrDetailsBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * ocr明细Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataOcrDetailsService {

    /**
     * 查询ocr明细
     *
     * @param id 主键
     * @return ocr明细
     */
    DataOcrDetailsVo queryById(String id);

    /**
     * 分页查询ocr明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return ocr明细分页列表
     */
    TableDataInfo<DataOcrDetailsVo> queryPageList(DataOcrDetailsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的ocr明细列表
     *
     * @param bo 查询条件
     * @return ocr明细列表
     */
    List<DataOcrDetailsVo> queryList(DataOcrDetailsBo bo);

    /**
     * 新增ocr明细
     *
     * @param bo ocr明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataOcrDetailsBo bo);

    /**
     * 修改ocr明细
     *
     * @param bo ocr明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataOcrDetailsBo bo);

    /**
     * 校验并批量删除ocr明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
