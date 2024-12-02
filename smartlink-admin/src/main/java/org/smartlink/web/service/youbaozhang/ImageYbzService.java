package org.smartlink.web.service.youbaozhang;

import org.smartlink.web.domain.ybz.request.YbzDeleteImageRequest;
import org.smartlink.web.domain.ybz.request.YbzGetImageListRequest;
import org.smartlink.web.domain.ybz.request.YbzUploadImageRequest;
import org.smartlink.web.domain.ybz.response.YbzReturnDTO;

public interface ImageYbzService {

    /**
     * 根据单据号查询影像数据
     * @param imageInfo 影像信息
     * @return String  文件信息集合
     */
    YbzReturnDTO imageList(YbzGetImageListRequest imageInfo);

    /**
     * 上传影像
     * <p>
     *
     * @param imageInfo 影像信息
     *
     * @return String  文件信息集合
     */
    YbzReturnDTO imageUpload(YbzUploadImageRequest imageInfo) throws Exception;

    /**
     * 改变影像状态(包括删除)
     * <p>
     *
     * @param imageInfo 影像信息
     *
     * @return String  文件信息集合
     */
    YbzReturnDTO updateImageState(YbzDeleteImageRequest imageInfo);
}
