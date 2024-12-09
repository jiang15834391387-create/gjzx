package org.smartlink.server.nc.mapper;


import org.apache.ibatis.annotations.Select;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.server.nc.domain.DataCmInfo;
import org.smartlink.server.nc.domain.vo.DataCmInfoVo;


/**
 * 任务、图片中间关联Mapper接口
 *
 * @author L
 * @date
 */
public interface DataCmInfoMapper extends BaseMapperPlus<DataCmInfo, DataCmInfoVo> {

    @Select("SELECT count(1) FROM data_cm_info dci ,data_image_files_info difi \n" +
            "where dci.batch_id=difi.batch_id\n" +
            "and dci.business_serial_no='1001ZZ100000001CJZ94'")
    Integer selectEnclosure(String businessSerialNo);
}
