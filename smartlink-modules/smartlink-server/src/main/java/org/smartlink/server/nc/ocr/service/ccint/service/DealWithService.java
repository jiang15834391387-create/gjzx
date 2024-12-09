package org.smartlink.server.nc.ocr.service.ccint.service;


import org.smartlink.server.nc.ocr.service.bean.IdentificationData;

/**
 * @description：处理service
 * @author： L
 * @create：
 */
public interface DealWithService {
    /**
     * 二次处理服务
     * @param identificationData 识别k v
     * @return 识别 k v
     */
    IdentificationData dealIdentificationData(IdentificationData identificationData);
}
