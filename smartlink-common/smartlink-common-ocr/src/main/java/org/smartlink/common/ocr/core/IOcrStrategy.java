package org.smartlink.common.ocr.core;



import org.smartlink.common.ocr.entity.IdentificationData;
import org.smartlink.common.entity.domain.business.domain.DataImageFilesInfo;

import java.util.List;

/**
 * 识别策略
 *
 * @author lqm
 */
public interface IOcrStrategy {
    /**
     *
     * @param dataImageFilesInfo 图片实体
     * @param base64 图片字节
     * @return 返回k，识别实体
     */
    List<IdentificationData> getIdentificationData(DataImageFilesInfo dataImageFilesInfo, String base64);
}
