package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataUsedCarSalesVo;
import org.smartlink.business.domain.bo.DataUsedCarSalesBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 二手车销售统一发票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataUsedCarSalesService {

    /**
     * 查询二手车销售统一发票
     *
     * @param  id 主键
     * @return 二手车销售统一发票
     */
    DataUsedCarSalesVo queryById(String  id);

    /**
     * 分页查询二手车销售统一发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 二手车销售统一发票分页列表
     */
    TableDataInfo<DataUsedCarSalesVo> queryPageList(DataUsedCarSalesBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的二手车销售统一发票列表
     *
     * @param bo 查询条件
     * @return 二手车销售统一发票列表
     */
    List<DataUsedCarSalesVo> queryList(DataUsedCarSalesBo bo);

    /**
     * 新增二手车销售统一发票
     *
     * @param bo 二手车销售统一发票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataUsedCarSalesBo bo);

    /**
     * 修改二手车销售统一发票
     *
     * @param bo 二手车销售统一发票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataUsedCarSalesBo bo);

    /**
     * 校验并批量删除二手车销售统一发票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
