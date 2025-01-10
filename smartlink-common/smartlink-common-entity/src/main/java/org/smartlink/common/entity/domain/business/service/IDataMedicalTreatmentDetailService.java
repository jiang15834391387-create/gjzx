package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.bo.DataMedicalTreatmentDetailBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataMedicalTreatmentDetailVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 医疗票明细Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataMedicalTreatmentDetailService {

    /**
     * 查询医疗票明细
     *
     * @param id 主键
     * @return 医疗票明细
     */
    DataMedicalTreatmentDetailVo queryById(String id);

    /**
     * 分页查询医疗票明细列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 医疗票明细分页列表
     */
    TableDataInfo<DataMedicalTreatmentDetailVo> queryPageList(DataMedicalTreatmentDetailBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的医疗票明细列表
     *
     * @param bo 查询条件
     * @return 医疗票明细列表
     */
    List<DataMedicalTreatmentDetailVo> queryList(DataMedicalTreatmentDetailBo bo);

    /**
     * 新增医疗票明细
     *
     * @param bo 医疗票明细
     * @return 是否新增成功
     */
    Boolean insertByBo(DataMedicalTreatmentDetailBo bo);

    /**
     * 修改医疗票明细
     *
     * @param bo 医疗票明细
     * @return 是否修改成功
     */
    Boolean updateByBo(DataMedicalTreatmentDetailBo bo);

    /**
     * 校验并批量删除医疗票明细信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
}
