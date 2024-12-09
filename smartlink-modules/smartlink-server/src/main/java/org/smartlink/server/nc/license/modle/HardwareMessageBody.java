package org.smartlink.server.nc.license.modle;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author L
 * @title 硬件信息
 * @description 硬件信息
 * @date
 */
@Data
@Component
public class HardwareMessageBody {
    /**
     * 可被允许的主板序列号
     */
    private String mainBoardSerial;

    /**
     * 硬件授权数量
     */
    private String macSum;

    /**
     * 小程序授权数量
     */
    private String miniSum;

    /**
     * 查验次数-针对公网
     */
    private String maxCheck;

    /**
     * ocr次数-针对公网
     */
    private String maxOcr;

    /**
     * 是否是公网OCR 0_否；1_是）
     */
    private int isWaiOcr;

    /**
     * 授权起止时间
     * */
    private String delDate;
}
