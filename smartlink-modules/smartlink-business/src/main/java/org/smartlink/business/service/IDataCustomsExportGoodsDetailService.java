package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataCustomsExportGoodsDetailVo;
import org.smartlink.business.domain.bo.DataCustomsExportGoodsDetailBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 海关出口货物明细Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataCustomsExportGoodsDetailService {

    /**
     * 查询海关出口货物明细
     *
     * @param id 主键
     * @return 海关出口货物明细
     */
    DataCustomsExportGoodsDetailVo queryById(String id);

    /**
     * 分页查询海关出口货物明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关出口货物明细分页列表
     */
    TableDataInfo<DataCustomsExportGoodsDetailVo> queryPageList(DataCustomsExportGoodsDetailBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的海关出口货物明细列表
     *
     * @param bo 查询条件
     * @return 海关出口货物明细列表
     */
    List<DataCustomsExportGoodsDetailVo> queryList(DataCustomsExportGoodsDetailBo bo);

    /**
     * 新增海关出口货物明细
     *
     * @param bo 海关出口货物明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataCustomsExportGoodsDetailBo bo);

    /**
     * 修改海关出口货物明细
     *
     * @param bo 海关出口货物明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataCustomsExportGoodsDetailBo bo);

    /**
     * 校验并批量删除海关出口货物明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
