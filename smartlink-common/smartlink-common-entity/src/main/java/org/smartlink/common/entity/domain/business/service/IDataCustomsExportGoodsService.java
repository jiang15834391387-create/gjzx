package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsExportGoodsBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsExportGoodsVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 海关出口货物Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataCustomsExportGoodsService {

    /**
     * 查询海关出口货物
     *
     * @param id 主键
     * @return 海关出口货物
     */
    DataCustomsExportGoodsVo queryById(String id);

    /**
     * 分页查询海关出口货物列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关出口货物分页列表
     */
    TableDataInfo<DataCustomsExportGoodsVo> queryPageList(DataCustomsExportGoodsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的海关出口货物列表
     *
     * @param bo 查询条件
     * @return 海关出口货物列表
     */
    List<DataCustomsExportGoodsVo> queryList(DataCustomsExportGoodsBo bo);

    /**
     * 新增海关出口货物
     *
     * @param bo 海关出口货物
     * @return 是否新增成功
     */
    Boolean insertByBo(DataCustomsExportGoodsBo bo);

    /**
     * 修改海关出口货物
     *
     * @param bo 海关出口货物
     * @return 是否修改成功
     */
    Boolean updateByBo(DataCustomsExportGoodsBo bo);

    /**
     * 校验并批量删除海关出口货物信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
