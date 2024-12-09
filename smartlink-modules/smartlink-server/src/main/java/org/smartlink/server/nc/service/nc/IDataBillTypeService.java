package org.smartlink.server.nc.service.nc;


import org.smartlink.server.nc.domain.DataBillType;

import java.util.List;

public interface IDataBillTypeService {

    List<DataBillType> selectAll();

    /**
     * 批量插入从业务系统同步的单据类型
     * @param dataBillTypeList
     * @return
     */
    Boolean insertAllExternalBillType(List<DataBillType> dataBillTypeList);

    /**
     * 根据typeId查询单据类型列表
     * @param typeCode 单据类型code
     * @return @{@link List<DataBillType>}
     */
    List<DataBillType> listDataBillTypeByTypeCode(String typeCode);
}
