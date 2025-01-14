package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataFlightItinerary;
import org.smartlink.common.entity.domain.business.domain.bo.DataFlightItineraryBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataFlightItineraryVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 航空电子行程单Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataFlightItineraryService {

    /**
     * 查询航空电子行程单
     *
     * @param id 主键
     * @return 航空电子行程单
     */
    DataFlightItineraryVo queryById(String id);

    Boolean insert(DataFlightItinerary dataFlightItinerary);

    /**
     * 分页查询航空电子行程单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 航空电子行程单分页列表
     */
    TableDataInfo<DataFlightItineraryVo> queryPageList(DataFlightItineraryBo bo, PageQuery pageQuery);

    DataFlightItinerary selectOneByFileId(String fileId);

    /**
     * 查询符合条件的航空电子行程单列表
     *
     * @param bo 查询条件
     * @return 航空电子行程单列表
     */
    List<DataFlightItineraryVo> queryList(DataFlightItineraryBo bo);

    /**
     * 新增航空电子行程单
     *
     * @param bo 航空电子行程单
     * @return 是否新增成功
     */
    Boolean insertByBo(DataFlightItineraryBo bo);

    /**
     * 修改航空电子行程单
     *
     * @param bo 航空电子行程单
     * @return 是否修改成功
     */
    Boolean updateByBo(DataFlightItineraryBo bo);

    /**
     * 校验并批量删除航空电子行程单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
