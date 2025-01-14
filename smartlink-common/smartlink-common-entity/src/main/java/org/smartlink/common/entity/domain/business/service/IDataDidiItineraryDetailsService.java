package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataDidiItineraryDetails;
import org.smartlink.common.entity.domain.business.domain.bo.DataDidiItineraryDetailsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDidiItineraryDetailsVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 滴滴行程单明细Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataDidiItineraryDetailsService {

    /**
     * 查询滴滴行程单明细
     *
     * @param id 主键
     * @return 滴滴行程单明细
     */
    DataDidiItineraryDetailsVo queryById(String id);

    Boolean insertBatch(List<DataDidiItineraryDetails> dataOcrDetails);

    /**
     * 分页查询滴滴行程单明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 滴滴行程单明细分页列表
     */
    TableDataInfo<DataDidiItineraryDetailsVo> queryPageList(DataDidiItineraryDetailsBo bo, PageQuery pageQuery);

    DataDidiItineraryDetails selectOneByFileId(String fileId);

    /**
     * 查询符合条件的滴滴行程单明细列表
     *
     * @param bo 查询条件
     * @return 滴滴行程单明细列表
     */
    List<DataDidiItineraryDetailsVo> queryList(DataDidiItineraryDetailsBo bo);

    /**
     * 新增滴滴行程单明细
     *
     * @param bo 滴滴行程单明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataDidiItineraryDetailsBo bo);

    /**
     * 修改滴滴行程单明细
     *
     * @param bo 滴滴行程单明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataDidiItineraryDetailsBo bo);

    /**
     * 校验并批量删除滴滴行程单明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
