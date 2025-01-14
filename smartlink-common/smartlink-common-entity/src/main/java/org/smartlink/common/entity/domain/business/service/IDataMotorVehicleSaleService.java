package org.smartlink.common.entity.domain.business.service;


import org.smartlink.common.entity.domain.business.domain.DataMotorVehicleSale;
import org.smartlink.common.entity.domain.business.domain.bo.DataMotorVehicleSaleBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataMotorVehicleSaleVo;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 机动车销售发票Service接口
 *
 * @author Lion Li
 * @date 2025-01-08
 */
public interface IDataMotorVehicleSaleService {

    /**
     * 查询机动车销售发票
     *
     * @param id 主键
     * @return 机动车销售发票
     */
    DataMotorVehicleSaleVo queryById(String id);

    /**
     * 分页查询机动车销售发票列表
     *
     * @param bo        查询条件
     * @param pageQuery 分页参数
     * @return 机动车销售发票分页列表
     */
    TableDataInfo<DataMotorVehicleSaleVo> queryPageList(DataMotorVehicleSaleBo bo, PageQuery pageQuery);

    /**
     * 查询符合条件的机动车销售发票列表
     *
     * @param bo 查询条件
     * @return 机动车销售发票列表
     */
    List<DataMotorVehicleSaleVo> queryList(DataMotorVehicleSaleBo bo);

    /**
     * 新增机动车销售发票
     *
     * @param bo 机动车销售发票
     * @return 是否新增成功
     */
    Boolean insertByBo(DataMotorVehicleSaleBo bo);

    /**
     * 修改机动车销售发票
     *
     * @param bo 机动车销售发票
     * @return 是否修改成功
     */
    Boolean updateByBo(DataMotorVehicleSaleBo bo);

    /**
     * 校验并批量删除机动车销售发票信息
     *
     * @param ids     待删除的主键集合
     * @param isValid 是否进行有效性校验
     * @return 是否删除成功
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);
    // 根据文件id查询机动车信息
    DataMotorVehicleSale getByFileId(String fileId);
}
