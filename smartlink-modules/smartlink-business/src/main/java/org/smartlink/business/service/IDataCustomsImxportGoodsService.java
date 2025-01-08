package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataCustomsImxportGoodsVo;
import org.smartlink.business.domain.bo.DataCustomsImxportGoodsBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 海关进口货物报关单Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataCustomsImxportGoodsService {

    /**
     * 查询海关进口货物报关单
     *
     * @param id 主键
     * @return 海关进口货物报关单
     */
    DataCustomsImxportGoodsVo queryById(String id);

    /**
     * 分页查询海关进口货物报关单列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关进口货物报关单分页列表
     */
    TableDataInfo<DataCustomsImxportGoodsVo> queryPageList(DataCustomsImxportGoodsBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的海关进口货物报关单列表
     *
     * @param bo 查询条件
     * @return 海关进口货物报关单列表
     */
    List<DataCustomsImxportGoodsVo> queryList(DataCustomsImxportGoodsBo bo);

    /**
     * 新增海关进口货物报关单
     *
     * @param bo 海关进口货物报关单
     * @return 是否新增成功
     */
    Boolean insertByBo(DataCustomsImxportGoodsBo bo);

    /**
     * 修改海关进口货物报关单
     *
     * @param bo 海关进口货物报关单
     * @return 是否修改成功
     */
    Boolean updateByBo(DataCustomsImxportGoodsBo bo);

    /**
     * 校验并批量删除海关进口货物报关单信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
