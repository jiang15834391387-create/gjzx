package org.smartlink.server.nc.service.hardwaremessage;

import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.hardwaremessage.HardWareMessage;

/**
 * 硬件授权数量接口
 */
public interface HardWareMessageService {

    /**
     * 进行ocr和查验次数、是否还有扫描台授权数量校验
     * */
    HardWareMessage whetherToAuthorize(String macIp) throws Exception;

    R<Void> haveSurplusMacIpList(String mac, HardWareMessage hardWareMessage);
    /**
     * 查询硬件授权数量表
     */
    HardWareMessage selectMessage();
}
