package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataElectronicTransportationGoodsVo;
import org.smartlink.business.domain.bo.DataElectronicTransportationGoodsBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 货物运输电子收款凭证Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataElectronicTransportationGoodsService {

    /**
     * 查询货物运输电子收款凭证
     *
     * @param id 主键
     * @return 货物运输电子收款凭证
     */
    DataElectronicTransportationGoodsVo queryById(String id);

    /**
     * 分页查询货物运输电子收款凭证列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 货物运输电子收款凭证分页列表
     */
    TableDataInfo<DataElectronicTransportationGoodsVo> queryPageList(DataElectronicTransportationGoodsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的货物运输电子收款凭证列表
     *
     * @param bo 查询条件
     * @return 货物运输电子收款凭证列表
     */
    List<DataElectronicTransportationGoodsVo> queryList(DataElectronicTransportationGoodsBo bo);

    /**
     * 新增货物运输电子收款凭证
     *
     * @param bo 货物运输电子收款凭证
     * @return 是否新增成功
     */
    Boolean insertByBo(DataElectronicTransportationGoodsBo bo);

    /**
     * 修改货物运输电子收款凭证
     *
     * @param bo 货物运输电子收款凭证
     * @return 是否修改成功
     */
    Boolean updateByBo(DataElectronicTransportationGoodsBo bo);

    /**
     * 校验并批量删除货物运输电子收款凭证信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
