package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.bo.DataDidiItineraryBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDidiItineraryVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 滴滴行程单Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataDidiItineraryService {

    /**
     * 查询滴滴行程单
     *
     * @param id 主键
     * @return 滴滴行程单
     */
    DataDidiItineraryVo queryById(String id);

    /**
     * 分页查询滴滴行程单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 滴滴行程单分页列表
     */
    TableDataInfo<DataDidiItineraryVo> queryPageList(DataDidiItineraryBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的滴滴行程单列表
     *
     * @param bo 查询条件
     * @return 滴滴行程单列表
     */
    List<DataDidiItineraryVo> queryList(DataDidiItineraryBo bo);

    /**
     * 新增滴滴行程单
     *
     * @param bo 滴滴行程单
     * @return 是否新增成功
     */
    Boolean insertByBo(DataDidiItineraryBo bo);

    /**
     * 修改滴滴行程单
     *
     * @param bo 滴滴行程单
     * @return 是否修改成功
     */
    Boolean updateByBo(DataDidiItineraryBo bo);

    /**
     * 校验并批量删除滴滴行程单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
