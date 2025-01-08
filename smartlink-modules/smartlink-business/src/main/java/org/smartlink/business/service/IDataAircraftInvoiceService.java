package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataAircraftInvoiceVo;
import org.smartlink.business.domain.bo.DataAircraftInvoiceBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 机打发票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataAircraftInvoiceService {

    /**
     * 查询机打发票
     *
     * @param id 主键
     * @return 机打发票
     */
    DataAircraftInvoiceVo queryById(String id);

    /**
     * 分页查询机打发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 机打发票分页列表
     */
    TableDataInfo<DataAircraftInvoiceVo> queryPageList(DataAircraftInvoiceBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的机打发票列表
     *
     * @param bo 查询条件
     * @return 机打发票列表
     */
    List<DataAircraftInvoiceVo> queryList(DataAircraftInvoiceBo bo);

    /**
     * 新增机打发票
     *
     * @param bo 机打发票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataAircraftInvoiceBo bo);

    /**
     * 修改机打发票
     *
     * @param bo 机打发票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataAircraftInvoiceBo bo);

    /**
     * 校验并批量删除机打发票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
