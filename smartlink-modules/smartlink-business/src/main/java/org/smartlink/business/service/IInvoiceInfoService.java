package org.smartlink.business.service;

import org.smartlink.common.core.domain.R;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;
import org.smartlink.common.entity.domain.business.domain.bo.DataImageFilesInfoBo;
import org.smartlink.common.entity.domain.business.domain.vo.DataImageFilesInfoVo;

import java.util.List;

public interface IInvoiceInfoService {

    /**
     * 查询符合条件的图片文件列表
     *
     * @param bo 查询条件
     * @return 图片文件列表
     */
    R queryList(DataImageFilesInfoBo bo);

    /**
     * 查询符合条件的图片文件列表
     *
     * @param fileId 查询条件
     * @return 图片文件列表
     */
    R queryOneInfo(String fileId);

}
