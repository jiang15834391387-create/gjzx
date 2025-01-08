package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataFlightsItineraryDetailVo;
import org.smartlink.business.domain.bo.DataFlightsItineraryDetailBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 航空电子行程单明细Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataFlightsItineraryDetailService {

    /**
     * 查询航空电子行程单明细
     *
     * @param id 主键
     * @return 航空电子行程单明细
     */
    DataFlightsItineraryDetailVo queryById(String id);

    /**
     * 分页查询航空电子行程单明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 航空电子行程单明细分页列表
     */
    TableDataInfo<DataFlightsItineraryDetailVo> queryPageList(DataFlightsItineraryDetailBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的航空电子行程单明细列表
     *
     * @param bo 查询条件
     * @return 航空电子行程单明细列表
     */
    List<DataFlightsItineraryDetailVo> queryList(DataFlightsItineraryDetailBo bo);

    /**
     * 新增航空电子行程单明细
     *
     * @param bo 航空电子行程单明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataFlightsItineraryDetailBo bo);

    /**
     * 修改航空电子行程单明细
     *
     * @param bo 航空电子行程单明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataFlightsItineraryDetailBo bo);

    /**
     * 校验并批量删除航空电子行程单明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
