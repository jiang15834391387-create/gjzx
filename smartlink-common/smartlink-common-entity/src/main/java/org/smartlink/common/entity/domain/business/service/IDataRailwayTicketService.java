package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataRailwayTicket;
import org.smartlink.common.entity.domain.business.domain.DataReceipt;
import org.smartlink.common.entity.domain.business.domain.bo.DataRailwayTicketBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataRailwayTicketVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 火车票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataRailwayTicketService {

    /**
     * 查询火车票
     *
     * @param id 主键
     * @return 火车票
     */
    DataRailwayTicketVo queryById(String id);

    /**
     * 分页查询火车票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 火车票分页列表
     */
    TableDataInfo<DataRailwayTicketVo> queryPageList(DataRailwayTicketBo bo, PageQuery pageQuery);

    Boolean insert(DataRailwayTicket dataOcrInfo);

    DataRailwayTicket selectOneByFileId(String fileId);

    /**
     * 查询符合条件的火车票列表
     *
     * @param bo 查询条件
     * @return 火车票列表
     */
    List<DataRailwayTicketVo> queryList(DataRailwayTicketBo bo);

    /**
     * 新增火车票
     *
     * @param bo 火车票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataRailwayTicketBo bo);

    /**
     * 修改火车票
     *
     * @param bo 火车票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataRailwayTicketBo bo);

    /**
     * 校验并批量删除火车票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
