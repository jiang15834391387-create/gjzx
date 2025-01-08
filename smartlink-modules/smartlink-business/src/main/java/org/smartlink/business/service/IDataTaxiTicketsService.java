package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataTaxiTicketsVo;
import org.smartlink.business.domain.bo.DataTaxiTicketsBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 出租车发票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataTaxiTicketsService {

    /**
     * 查询出租车发票
     *
     * @param id 主键
     * @return 出租车发票
     */
    DataTaxiTicketsVo queryById(String id);

    /**
     * 分页查询出租车发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 出租车发票分页列表
     */
    TableDataInfo<DataTaxiTicketsVo> queryPageList(DataTaxiTicketsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的出租车发票列表
     *
     * @param bo 查询条件
     * @return 出租车发票列表
     */
    List<DataTaxiTicketsVo> queryList(DataTaxiTicketsBo bo);

    /**
     * 新增出租车发票
     *
     * @param bo 出租车发票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataTaxiTicketsBo bo);

    /**
     * 修改出租车发票
     *
     * @param bo 出租车发票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataTaxiTicketsBo bo);

    /**
     * 校验并批量删除出租车发票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
