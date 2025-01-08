package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataTollRoadsVo;
import org.smartlink.business.domain.bo.DataTollRoadsBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 过路费Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataTollRoadsService {

    /**
     * 查询过路费
     *
     * @param id 主键
     * @return 过路费
     */
    DataTollRoadsVo queryById(String id);

    /**
     * 分页查询过路费列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 过路费分页列表
     */
    TableDataInfo<DataTollRoadsVo> queryPageList(DataTollRoadsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的过路费列表
     *
     * @param bo 查询条件
     * @return 过路费列表
     */
    List<DataTollRoadsVo> queryList(DataTollRoadsBo bo);

    /**
     * 新增过路费
     *
     * @param bo 过路费
     * @return 是否新增成功
     */
    Boolean insertByBo(DataTollRoadsBo bo);

    /**
     * 修改过路费
     *
     * @param bo 过路费
     * @return 是否修改成功
     */
    Boolean updateByBo(DataTollRoadsBo bo);

    /**
     * 校验并批量删除过路费信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
