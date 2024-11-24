package org.smartlink.web.service.nc.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.smartlink.web.domain.DataBillType;
import org.smartlink.web.mapper.DataBillTypeMapper;
import org.smartlink.web.service.nc.IDataBillTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 单据类型Service业务层处理
 *
 * @author L
 * @date
 */
@RequiredArgsConstructor
@Service
public class DataBillTypeServiceImpl implements IDataBillTypeService {

    private final DataBillTypeMapper baseMapper;

    @Override
    public List<DataBillType> selectAll() {
        return this.baseMapper.selectList();
    }

    @Override
    public Boolean insertAllExternalBillType(List<DataBillType> dataBillTypeList) {
        //return this.baseMapper.insertOrUpdateBatch(dataBillTypeList);
        return this.baseMapper.insertBatch(dataBillTypeList);
    }

    @Override
    public List<DataBillType> listDataBillTypeByTypeCode(String typeCode) {
        LambdaQueryWrapper<DataBillType> queryWrapper = new LambdaQueryWrapper<DataBillType>();
        queryWrapper.eq(DataBillType::getTypeCode,typeCode);
        return this.baseMapper.selectList(queryWrapper);
    }
}
