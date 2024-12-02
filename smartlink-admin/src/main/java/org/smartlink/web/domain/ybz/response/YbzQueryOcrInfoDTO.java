package org.smartlink.web.domain.ybz.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 友报账发票信息
 *
 * @author maxuhui
 */
@Data
public class YbzQueryOcrInfoDTO implements Serializable {
    /**
     * 是否已经发票验真,1-验证通过;2-验证不通过或未验证;
     */
    private String ischecked;
    /**
     * 购方税号
     */
    private String gfsh;
    /**
     * 销方税号
     */
    private String xfsh;
    /**
     * 发票代码
     */
    private String fpdm;
    /**
     * 发票号码
     */
    private String fphm;
    /**
     * 金额
     */
    private String je;
    /**
     * 税额
     */
    private String se;
    /**
     * 开票日期
     */
    private String kprq;
    /**
     * 发票类型，01  专票，04  普票，10  电子普票，11 卷式普票
     */
    private String invtype;
    /**
     * 发票校验码
     */
    private String fpjym;
    /**
     * 开票金额
     */
    private String totalamount;
}
