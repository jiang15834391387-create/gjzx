package org.smartlink.server.nc.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.smartlink.common.mybatis.core.mapper.BaseMapperPlus;
import org.smartlink.server.nc.domain.DataImageFilesInfo;
import org.smartlink.server.nc.domain.imagefilesinfo.DataImageFilesInfoVo;

import java.util.List;

/**
 * 图片文件Mapper接口
 *
 * @author L
 * @date
 */
public interface DataImageFilesInfoMapper extends BaseMapperPlus<DataImageFilesInfo, DataImageFilesInfoVo> {

    List<DataImageFilesInfo> fuzzySelectAllByBarCode(@Param(Constants.WRAPPER) Wrapper<DataImageFilesInfo> queryWrapper);
}
