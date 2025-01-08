package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataAirlineRulesInfoVo;
import org.smartlink.business.domain.bo.DataAirlineRulesInfoBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 飞机票仓位信息Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataAirlineRulesInfoService {

    /**
     * 查询飞机票仓位信息
     *
     * @param airlineId 主键
     * @return 飞机票仓位信息
     */
    DataAirlineRulesInfoVo queryById(Long airlineId);

    /**
     * 分页查询飞机票仓位信息列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 飞机票仓位信息分页列表
     */
    TableDataInfo<DataAirlineRulesInfoVo> queryPageList(DataAirlineRulesInfoBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的飞机票仓位信息列表
     *
     * @param bo 查询条件
     * @return 飞机票仓位信息列表
     */
    List<DataAirlineRulesInfoVo> queryList(DataAirlineRulesInfoBo bo);

    /**
     * 新增飞机票仓位信息
     *
     * @param bo 飞机票仓位信息
     * @return 是否新增成功
     */
    Boolean insertByBo(DataAirlineRulesInfoBo bo);

    /**
     * 修改飞机票仓位信息
     *
     * @param bo 飞机票仓位信息
     * @return 是否修改成功
     */
    Boolean updateByBo(DataAirlineRulesInfoBo bo);

    /**
     * 校验并批量删除飞机票仓位信息信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
