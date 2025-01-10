package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.bo.DataQuotaInvoiceBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataQuotaInvoiceVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 定额发票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataQuotaInvoiceService {

    /**
     * 查询定额发票
     *
     * @param id 主键
     * @return 定额发票
     */
    DataQuotaInvoiceVo queryById(String id);

    /**
     * 分页查询定额发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 定额发票分页列表
     */
    TableDataInfo<DataQuotaInvoiceVo> queryPageList(DataQuotaInvoiceBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的定额发票列表
     *
     * @param bo 查询条件
     * @return 定额发票列表
     */
    List<DataQuotaInvoiceVo> queryList(DataQuotaInvoiceBo bo);

    /**
     * 新增定额发票
     *
     * @param bo 定额发票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataQuotaInvoiceBo bo);

    /**
     * 修改定额发票
     *
     * @param bo 定额发票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataQuotaInvoiceBo bo);

    /**
     * 校验并批量删除定额发票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
