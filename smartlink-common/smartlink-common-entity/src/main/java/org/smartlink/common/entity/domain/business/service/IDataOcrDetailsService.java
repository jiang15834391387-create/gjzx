package org.smartlink.common.entity.domain.business.service;

import org.smartlink.common.entity.domain.business.domain.DataOcrDetails;
import org.smartlink.common.entity.domain.business.domain.bo.DataOcrDetailsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataOcrDetailsVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

public interface IDataOcrDetailsService {
    /**
     * 查询增值税发票明细
     *
     * @param id 主键
     * @return 增值税发票明细
     */
    DataOcrDetailsVo queryById(String id);

    /**
     * 分页查询增值税发票明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 增值税发票明细分页列表
     */
    TableDataInfo<DataOcrDetailsVo> queryPageList(DataOcrDetailsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的增值税发票明细列表
     *
     * @param bo 查询条件
     * @return 增值税发票明细列表
     */
    List<DataOcrDetailsVo> queryList(DataOcrDetailsBo bo);

    /**
     * 新增增值税发票明细
     *
     * @param bo 增值税发票明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataOcrDetailsBo bo);

    /**
     * 新增增值税发票明细
     *
     * @param dataOcrDetails 增值税发票明细
     * @return 是否新增成功
     */
    Boolean insert(DataOcrDetails dataOcrDetails);

    /**
     * 批量新增增值税发票明细
     *
     * @param dataOcrDetails 增值税发票明细
     * @return 是否新增成功
     */
    Boolean insertBatch(List<DataOcrDetails> dataOcrDetails);

    /**
     * 修改增值税发票明细
     *
     * @param bo 增值税发票明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataOcrDetailsBo bo);

    /**
     * 校验并批量删除增值税发票明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
