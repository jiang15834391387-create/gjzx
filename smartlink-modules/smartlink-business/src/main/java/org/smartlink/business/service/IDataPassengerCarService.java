package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataPassengerCarVo;
import org.smartlink.business.domain.bo.DataPassengerCarBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 客运汽车票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataPassengerCarService {

    /**
     * 查询客运汽车票
     *
     * @param id 主键
     * @return 客运汽车票
     */
    DataPassengerCarVo queryById(String id);

    /**
     * 分页查询客运汽车票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 客运汽车票分页列表
     */
    TableDataInfo<DataPassengerCarVo> queryPageList(DataPassengerCarBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的客运汽车票列表
     *
     * @param bo 查询条件
     * @return 客运汽车票列表
     */
    List<DataPassengerCarVo> queryList(DataPassengerCarBo bo);

    /**
     * 新增客运汽车票
     *
     * @param bo 客运汽车票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataPassengerCarBo bo);

    /**
     * 修改客运汽车票
     *
     * @param bo 客运汽车票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataPassengerCarBo bo);

    /**
     * 校验并批量删除客运汽车票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
