package org.smartlink.business.service;

import org.smartlink.business.domain.vo.DataReceiptVo;
import org.smartlink.business.domain.bo.DataReceiptBo;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.mybatis.core.page.PageQuery;

import java.util.Collection;
import java.util.List;

/**
 * 小票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataReceiptService {

    /**
     * 查询小票
     *
     * @param id 主键
     * @return 小票
     */
    DataReceiptVo queryById(String id);

    /**
     * 分页查询小票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 小票分页列表
     */
    TableDataInfo<DataReceiptVo> queryPageList(DataReceiptBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的小票列表
     *
     * @param bo 查询条件
     * @return 小票列表
     */
    List<DataReceiptVo> queryList(DataReceiptBo bo);

    /**
     * 新增小票
     *
     * @param bo 小票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataReceiptBo bo);

    /**
     * 修改小票
     *
     * @param bo 小票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataReceiptBo bo);

    /**
     * 校验并批量删除小票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
