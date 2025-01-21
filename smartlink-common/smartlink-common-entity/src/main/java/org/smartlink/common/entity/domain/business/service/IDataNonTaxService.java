package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataNonTax;
import org.smartlink.common.entity.domain.business.domain.bo.DataNonTaxBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataNonTaxVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 非税收入类票据Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataNonTaxService {

    /**
     * 查询非税收入类票据
     *
     * @param id 主键
     * @return 非税收入类票据
     */
    DataNonTaxVo queryById(String id);

    /**
     * 分页查询非税收入类票据列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 非税收入类票据分页列表
     */
    TableDataInfo<DataNonTaxVo> queryPageList(DataNonTaxBo bo, PageQuery pageQuery);


    /**
     * 新增发票
     */
    Boolean insert(DataNonTax dataOcrInfo);

    /**
     * 新增发票
     */
    DataNonTax selectOneByFileId(String fileId);
    /**
     * 查询符合条件的非税收入类票据列表
     *
     * @param bo 查询条件
     * @return 非税收入类票据列表
     */
    List<DataNonTaxVo> queryList(DataNonTaxBo bo);

    /**
     * 新增非税收入类票据
     *
     * @param bo 非税收入类票据
     * @return 是否新增成功
     */
    Boolean insertByBo(DataNonTaxBo bo);

    /**
     * 修改非税收入类票据
     *
     * @param bo 非税收入类票据
     * @return 是否修改成功
     */
    Boolean updateByBo(DataNonTaxBo bo);

    /**
     * 校验并批量删除非税收入类票据信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
