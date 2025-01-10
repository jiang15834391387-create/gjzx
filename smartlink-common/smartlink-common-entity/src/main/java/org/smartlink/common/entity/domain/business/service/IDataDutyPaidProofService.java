package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.bo.DataDutyPaidProofBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataDutyPaidProofVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 完税证明Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataDutyPaidProofService {

    /**
     * 查询完税证明
     *
     * @param id 主键
     * @return 完税证明
     */
    DataDutyPaidProofVo queryById(String id);

    /**
     * 分页查询完税证明列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 完税证明分页列表
     */
    TableDataInfo<DataDutyPaidProofVo> queryPageList(DataDutyPaidProofBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的完税证明列表
     *
     * @param bo 查询条件
     * @return 完税证明列表
     */
    List<DataDutyPaidProofVo> queryList(DataDutyPaidProofBo bo);

    /**
     * 新增完税证明
     *
     * @param bo 完税证明
     * @return 是否新增成功
     */
    Boolean insertByBo(DataDutyPaidProofBo bo);

    /**
     * 修改完税证明
     *
     * @param bo 完税证明
     * @return 是否修改成功
     */
    Boolean updateByBo(DataDutyPaidProofBo bo);

    /**
     * 校验并批量删除完税证明信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
