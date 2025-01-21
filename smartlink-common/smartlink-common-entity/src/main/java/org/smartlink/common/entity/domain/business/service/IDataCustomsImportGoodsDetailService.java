package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataCustomsImportGoodsDetail;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsImportGoodsDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsImportGoodsDetailVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 海关进口货物明细Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataCustomsImportGoodsDetailService {

    /**
     * 查询海关进口货物明细
     *
     * @param id 主键
     * @return 海关进口货物明细
     */
    DataCustomsImportGoodsDetailVo queryById(String id);
    Boolean insertBatch(List<DataCustomsImportGoodsDetail> dataOcrDetails);

    /**
     * 分页查询海关进口货物明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关进口货物明细分页列表
     */
    TableDataInfo<DataCustomsImportGoodsDetailVo> queryPageList(DataCustomsImportGoodsDetailBo bo, PageQuery pageQuery);

    /**
     * 新增发票
     */
    DataCustomsImportGoodsDetail selectOneByFileId(String fileId);

    /**
     * 查询符合条件的海关进口货物明细列表
     *
     * @param bo 查询条件
     * @return 海关进口货物明细列表
     */
    List<DataCustomsImportGoodsDetailVo> queryList(DataCustomsImportGoodsDetailBo bo);

    /**
     * 新增海关进口货物明细
     *
     * @param bo 海关进口货物明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataCustomsImportGoodsDetailBo bo);

    /**
     * 修改海关进口货物明细
     *
     * @param bo 海关进口货物明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataCustomsImportGoodsDetailBo bo);

    /**
     * 校验并批量删除海关进口货物明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
