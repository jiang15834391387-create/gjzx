package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataSteamerTicketVo;
import org.smartlink.business.domain.bo.DataSteamerTicketBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 船票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataSteamerTicketService {

    /**
     * 查询船票
     *
     * @param id 主键
     * @return 船票
     */
    DataSteamerTicketVo queryById(String id);

    /**
     * 分页查询船票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 船票分页列表
     */
    TableDataInfo<DataSteamerTicketVo> queryPageList(DataSteamerTicketBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的船票列表
     *
     * @param bo 查询条件
     * @return 船票列表
     */
    List<DataSteamerTicketVo> queryList(DataSteamerTicketBo bo);

    /**
     * 新增船票
     *
     * @param bo 船票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataSteamerTicketBo bo);

    /**
     * 修改船票
     *
     * @param bo 船票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataSteamerTicketBo bo);

    /**
     * 校验并批量删除船票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
