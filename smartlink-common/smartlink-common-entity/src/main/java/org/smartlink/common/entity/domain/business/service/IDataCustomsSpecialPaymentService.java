package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataCustomsSpecialPayment;
import org.smartlink.common.entity.domain.business.domain.bo.DataCustomsSpecialPaymentBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataCustomsSpecialPaymentVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 海关专用缴款书Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataCustomsSpecialPaymentService {

    /**
     * 查询海关专用缴款书
     *
     * @param id 主键
     * @return 海关专用缴款书
     */
    DataCustomsSpecialPaymentVo queryById(String id);

    /**
     * 分页查询海关专用缴款书列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 海关专用缴款书分页列表
     */
    TableDataInfo<DataCustomsSpecialPaymentVo> queryPageList(DataCustomsSpecialPaymentBo bo, PageQuery pageQuery);

    /**
     * 查询发票
     *
     * @param fileId 文件id
     * @return 是否新增成功
     */
    DataCustomsSpecialPayment selectOneByFileId(String fileId);

    /**
     * 新增发票
     */
    Boolean insert(DataCustomsSpecialPayment dataOcrInfo);

    /**
     * 查询符合条件的海关专用缴款书列表
     *
     * @param bo 查询条件
     * @return 海关专用缴款书列表
     */
    List<DataCustomsSpecialPaymentVo> queryList(DataCustomsSpecialPaymentBo bo);

    /**
     * 新增海关专用缴款书
     *
     * @param bo 海关专用缴款书
     * @return 是否新增成功
     */
    Boolean insertByBo(DataCustomsSpecialPaymentBo bo);

    /**
     * 修改海关专用缴款书
     *
     * @param bo 海关专用缴款书
     * @return 是否修改成功
     */
    Boolean updateByBo(DataCustomsSpecialPaymentBo bo);

    /**
     * 校验并批量删除海关专用缴款书信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
