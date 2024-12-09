package org.smartlink.server.nc.domain.hardwaremessage.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("硬件授权数量表")
@ExcelIgnoreUnannotated
public class HardWareMessageVo {
    private static final long serialVersionUID = 1L;


    /**
     *主键id
     * */
    @TableId(value = "id")
    private int id;

    /**
     *查验数量
     * */
    private int maxCheckNum;

    /**
     *ocr授权数量
     * */
    private int maxOcrNum;

    /**
     *扫描台授权数量
     * */
    private int macSumNum;

    /**
     *小程序用户数量
     * */
    private int miniSumNum;

    /**
     *主板序列号
     * */
    private String mainBoardSerial;

    /**
     *扫描台mac地址list
     * */
    private String macIpList;

    /**
     *小程序使用用户list
     * */
    private String miniUserList;

    /**
     *实际已产生的ocr数量
     * */
    private int realOcrNum;

    /**
     *实际已产生的查验数量
     * */
    private int realCheckNum;

    /**
     *授权型号
     * */
    private String authorizeNum;

    /**
     *授权序列号list
     * */
    private String authorizeList;

    /**
     * 是否公网ocr（0_否；1_是）
     * */
    private int isWaiOcr;

    /**
     * 授权起止时间
     * */
    private String delDate;
}
